package com.fly;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 每一个注解处理器继承抽象处理器
 * 写一个测试的处理器
 */
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {

    //    每一个注解处理器类都必须有一个空的构造函数

    // 一个用来处理TypeMirror的工具类
    private Types mTypeUtils;

    // 一个用来处理Element的工具类，源代码的每一个部分都是一个特定类型的Element
    private Elements mElementUtils;

    // 正如这个名字所示，使用Filer你可以创建文件
    private Filer mFiler;
    private Messager mMessager;

    /**
     * 这里有一个特殊的init()方法，它会被注解处理工具调用，并输入ProcessingEnviroment参数。
     * ProcessingEnviroment提供很多有用的工具类Elements,Types和Filer
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        //初始化我们需要的基础工具
        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    /**
     * 用来指定你使用的Java版本。通常这里返回SourceVersion.latestSupported()。
     * 然而，如果你有足够的理由只支持Java 7的话，你也可以返回SourceVersion.RELEASE_7
     * 推荐使用前者
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        //支持的java版本
//        return SourceVersion.RELEASE_7;
        return SourceVersion.latestSupported();
    }

    /**
     * 这里你必须指定，这个注解处理器是注册给哪个注解的。
     * 注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。换句话说，你在这里定义你的注解处理器注册到哪些注解上
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(TestAnnotation.class.getCanonicalName());
        return annotations;
    }

    /**
     * 这相当于每个处理器的主函数main()。你在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。
     * 输入参数RoundEnviroment，可以让你查询出包含特定注解的被注解元素。后面我们将看到详细的内容
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //这里开始处理我们的注解解析了，以及生成Java文件
        if (set == null || set.isEmpty()) {
            info(">>> set is null... <<<");
            return true;
        }

        info(">>> Found field, start... <<<");

        // 返回所有被注解了@TestAnnotation的元素的列表    Element(元素)可以是类、方法、变量等
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TestAnnotation.class);

        if (elements == null || elements.isEmpty()) {
            info(">>> elements is null... <<<");
            return true;
        }

        // 遍历所有被注解了@TestAnnotation的元素   这里就配合了TypeMirror使用EmentKind或者TypeKind进行元素类型判断
        for (Element annotatedElement : elements) {

            // 检查被注解为@Factory的元素是否是一个类
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with @%s",
                        TestAnnotation.class.getSimpleName());
                return true; // 退出处理
            }
            //解析，并生成代码
            analysisAnnotated(annotatedElement);
        }

        return true;
    }

    // 类名后缀
    private static final String SUFFIX = "$$Fly";

    /**
     * 编写解析和生成的代码格式
     * @param classElement
     */
    private void analysisAnnotated(Element classElement) {
        TestAnnotation annotation = classElement.getAnnotation(TestAnnotation.class);
        String name = annotation.name();
        String text = annotation.text();
        Name simpleName = classElement.getSimpleName();  // 使用了@TestAnnotation注解的类名
        System.out.println("名称 = " + simpleName);

//        TypeElement superClassName = mElementUtils.getTypeElement(name);
        String newClassName = name + SUFFIX;

        StringBuilder builder = new StringBuilder()
                .append("package com.fly.demoprocessor.auto;\n\n")
                .append("public class ")
                .append(newClassName)
                .append(" {\n\n") // open class
                .append("\tpublic String getMessage() {\n") // open method
                .append("\t\treturn \"");

        // this is appending to the return statement
        builder.append(text).append(name).append(" !\\n");

        builder.append("\";\n") // end return
                .append("\t}\n") // close method
                .append("}\n"); // close class

        // 把生成的这个类写入文件中
        try { // write the file
            JavaFileObject source = mFiler.createSourceFile("com.fly.demoprocessor.auto." + newClassName);
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
        info(">>> analysisAnnotated is finish... <<<");
    }

    /**
     * 日志和错误信息打印
     * @param e
     * @param msg
     * @param args
     */
    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

    /**
     * 日志信息打印
     * @param msg
     * @param args
     */
    private void info(String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.NOTE,
                String.format(msg, args));
    }
}

package com.drouter.compiler;

import com.drouter.base.annotation.Action;
import com.google.auto.service.AutoService;

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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * 每一个注解处理器继承抽象处理器
 * description: 写一个测试的处理器
 * https://www.jianshu.com/p/07ef8ba80562
 */
@AutoService(Processor.class)  // 这是一个注解处理器，是Google开发的，用来生成META-INF/services/javax.annotation.processing.Processor文件的
public class TestProcessor extends AbstractProcessor {

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
     * 这相当于每个处理器的主函数main()。你在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。
     * 输入参数RoundEnviroment，可以让你查询出包含特定注解的被注解元素。后面我们将看到详细的内容
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //这里开始处理我们的注解解析了，以及生成Java文件


        // 遍历所有被注解了@Factory的元素   Element可以是类、方法、变量等
        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Action.class)) {

        }

        return false;
    }

    /**
     * 这里你必须指定，这个注解处理器是注册给哪个注解的。
     * 注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。换句话说，你在这里定义你的注解处理器注册到哪些注解上
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
//        return super.getSupportedAnnotationTypes();

        //支持的注解
        Set<String> annotations = new LinkedHashSet<>();
//        annotations.add(ZyaoAnnotation.class.getCanonicalName());
        return annotations;
    }

    /**
     * 用来指定你使用的Java版本。通常这里返回SourceVersion.latestSupported()。
     * 然而，如果你有足够的理由只支持Java 7的话，你也可以返回SourceVersion.RELEASE_7
     * 推荐使用前者
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
//        return super.getSupportedSourceVersion();
        //支持的java版本
        return SourceVersion.latestSupported();
    }
}

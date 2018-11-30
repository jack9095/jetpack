package com.drouter.compiler;

import com.drouter.base.annotation.Action;
import com.drouter.compiler.util.CommonUtils;
import com.drouter.compiler.util.TextUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * description: 模块的处理器
 * https://blog.csdn.net/qq_30379689/article/details/82345625
 */
@AutoService(Processor.class)
public class ModuleProcessor extends AbstractProcessor {
    private Elements mElementUtils;   // 操作Element工具类
    private Filer mFiler;    // 文件写入
    private final String KEY_MODULE_NAME = "moduleName";
    // TypeMirror是一个接口，表示Java编程语言中的类型。这些类型包括基本类型、引用类型、数组类型、类型变量和null类型等等
    private TypeMirror iRouterAction = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        iRouterAction = mElementUtils.getTypeElement(Consts.ROUTERACTION).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // 1. 有没配置 modelName 防止 class 类冲突
        String moduleName = "";

        // 额外配置参数 在grade中配置的
        Map<String, String> options = processingEnv.getOptions();
        if (CommonUtils.isNotEmpty(options)) {
            moduleName = options.get(KEY_MODULE_NAME); // 获取模块的名称  login
        }

        System.out.println("moduleName = " + moduleName);

        if (!TextUtils.isEmpty(moduleName)) {
            moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");
        } else {  // 模块名为空抛出运行异常
            String errorMessage = "These no module name, at 'build.gradle', like :\n" +
                    "apt {\n" +
                    "    arguments {\n" +
                    "        moduleName project.getName();\n" +
                    "    }\n" +
                    "}\n";
            throw new RuntimeException("DRouter::Compiler >>> No module name, for more information, look at gradle log.\n" + errorMessage);
        }

        // 3. 生成 Java 类，效果如下
        /*public class DRouter$$Assist implements IRouterAssist {

            Map<String, String> modules = new HashMap<>();

            public DRouter$$Assist() {
                modules.put("login/module", "com.login.module.LoginModule");
            }

            @Override
            public String findModuleClassName(String moduleName) {
                return modules.get(moduleName);
            }
        }*/
        // 生成类继承和实现接口
        // ClassName可以识别任何声明类
        ClassName routerAssistClassName = ClassName.get("com.drouter.api.action", "IRouterModule");
        ClassName mapClassName = ClassName.get("java.util", "Map");


        // 生成一个类  https://blog.csdn.net/coderder/article/details/79989061 有详细介绍
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("DRouter$$Module$$" + moduleName)
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addSuperinterface(routerAssistClassName)
                .addField(mapClassName, "actions", Modifier.PRIVATE);

        // 构造函数
        MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);
        constructorMethodBuilder.addStatement("actions = new $T<>()", ClassName.bestGuess("java.util.HashMap"));

        // 2. 解析到所有的 Action 信息
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Action.class);
        Map<String, String> modules = new HashMap<>(elements.size());

        ClassName actionWrapperClassName = ClassName.get("com.drouter.api.extra", "ActionWrapper");
        ClassName threadModeClassName = ClassName.get("com.drouter.base", "ThreadMode");
        for (Element element : elements) {

            // 获取注解上面的 action
            Action actionAnnotation = element.getAnnotation(Action.class);
            String actionName = actionAnnotation.path();

            // 必须以配置的 gradle 包名开头
            if (!actionName.startsWith(moduleName + "/")) {
                error(element, "Path name of the action must begin with %s%s", moduleName, "/");
            }
            // 获取 Action 的 ClassName
            Element enclosingElement = element.getEnclosingElement();
            String packageName = mElementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
            String actionClassName = packageName + "." + element.getSimpleName();  // 全路径名 例如 com.login.module.LoginAction

            // 判断 Interceptor 注解类是否实现了 ActionInterceptor
            if (!((TypeElement) element).getInterfaces().contains(iRouterAction)) {
                error(element, "%s verify failed, @Action must be implements %s", element.getSimpleName().toString(), Consts.ROUTERACTION);
            }

            if (modules.containsKey(actionName)) {
                // 输出错误，Action 名称冲突重复了
                error(element, "%s module name already exists", actionName);
            }
            // 添加到集合
            modules.put(actionName, actionClassName);

            // 给构造函数添加参数和内部数据实现
            constructorMethodBuilder.addStatement("this.actions.put($S,$T.build($T.class, $S, "
                            + actionAnnotation.extraProcess() + ", $T." + actionAnnotation.threadMode() + "))",
                    actionName, actionWrapperClassName, ClassName.bestGuess(actionClassName), actionName, threadModeClassName);
        }

        // 实现方法

        MethodSpec.Builder unbindMethodBuilder = MethodSpec.methodBuilder("findAction")
                .addParameter(String.class, "actionName")
                .addAnnotation(Override.class)
                .returns(actionWrapperClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        unbindMethodBuilder.addStatement("return (ActionWrapper)actions.get(actionName)");

        classBuilder.addMethod(constructorMethodBuilder.build());
        classBuilder.addMethod(unbindMethodBuilder.build());

        // 生成类，看下效果 在包com.drouter.assist.module下面生成
        try {
            JavaFile.builder(Consts.ROUTER_MODULE_PACK_NAME, classBuilder.build())
                    .addFileComment("DRouter 自动生成")
                    .build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("翻车了！");
        }

        return false;
    }

    private void error(Element element, String message, String... args) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args);
    }

    // 虽然是编译时执行Processor,但也是可以输入日志信息用于调试的
    /**
     * Processor日志输出的位置在编译器下方的Messages窗口中。
     * 同样Processor也有自己的Log输出工具: Messager
     * Processor支持最基础的System.out方法
     *
     * 同Log类似，Messager也有日志级别的选择：
     * Diagnostic.Kind.ERROR
     * Diagnostic.Kind.WARNING
     * Diagnostic.Kind.MANDATORY_WARNING
     * Diagnostic.Kind.NOTE
     * Diagnostic.Kind.OTHER
     *
     * @param kind     日志级别
     * @param element  元素
     * @param message
     * @param args
     */
    private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        // 取得Messager对象
        Messager messager = processingEnv.getMessager();
        // 输出日志
        messager.printMessage(kind, message, element);
    }


    // 1. 指定处理的版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    // 2. 给到需要处理的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        // 需要解析的自定义注解 BindView  OnClick
        annotations.add(Action.class);
        return annotations;
    }
}

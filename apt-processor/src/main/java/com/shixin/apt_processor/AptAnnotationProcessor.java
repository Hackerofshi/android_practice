package com.shixin.apt_processor;

import com.google.auto.service.AutoService;
import com.shixin.apt_annotation.AptAnnotation;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

//注意：getSupportedAnnotationTypes()、getSupportedSourceVersion()
// 和getSupportedOptions()
// 这三个方法，我们还可以采用注解的方式进行提供：

@SupportedOptions("MODULE_NAME")
@SupportedAnnotationTypes("com.shixin.apt_annotation.AptAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class AptAnnotationProcessor extends AbstractProcessor {

    /**
     * 节点工具类（类、函数、属性都是节点）
     */
    private Elements mElementUtils;

    /**
     * 类信息工具类
     */
    private Types mTypeUtils;

    /**
     * 文件生成器
     */
    private Filer mFiler;


    /**
     * 日志信息打印器
     */
    private Messager mMessager;


    //模块名
    private String mModuleName;



    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mElementUtils = processingEnv.getElementUtils();
        mTypeUtils = processingEnv.getTypeUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();

        //通过 key 获取 build.gradle 中对应的 value
        mModuleName = processingEnv.getOptions().get("MODULE_NAME");

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        System.out.println("-----------------------------");
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }


        //获取当前注解下的节点信息
        Set<? extends Element> rootElements = roundEnv.getElementsAnnotatedWith(AptAnnotation.class);

        // 构建 test 函数
        MethodSpec.Builder builder = MethodSpec.methodBuilder("test")
                .addModifiers(Modifier.PUBLIC) // 指定方法修饰符
                .returns(void.class) // 指定返回类型
                .addParameter(String.class, "param"); // 添加参数
        builder.addStatement("$T.out.println($S)", System.class, "模块: " + mModuleName);

        if (rootElements != null && !rootElements.isEmpty()) {
            for (Element element : rootElements) {
                //当前节点名称
                String elementName = element.getSimpleName().toString();
                //当前节点下注解的属性
                String desc = element.getAnnotation(AptAnnotation.class).desc();
                // 构建方法体
                builder.addStatement("$T.out.println($S)", System.class,
                        "节点: " + elementName + "  " + "描述: " + desc);
            }
        }
        MethodSpec main =builder.build();

        // 构建 HelloWorld 类
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC) // 指定类修饰符
                .addMethod(main) // 添加方法
                .build();

        // 指定包路径，构建文件体
        JavaFile javaFile = JavaFile.builder("com.shixin.ui", helloWorld).build();
        try {
            // 创建文件
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 接收外来传入的参数，最常用的形式就是在 build.gradle 脚本文件里的 javaCompileOptions 的配置
     *
     * @return 属性的 Key 集合
     */
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    /**
     * 当前注解处理器支持的注解集合，如果支持，就会调用 process 方法
     *
     * @return 支持的注解集合
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    /**
     * 编译当前注解处理器的 JDK 版本
     *
     * @return JDK 版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }


}
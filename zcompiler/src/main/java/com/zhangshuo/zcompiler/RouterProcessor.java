package com.zhangshuo.zcompiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.zhangshuo.zanno.Route;
import com.zhangshuo.zcompiler.Bean.TargetInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        UtilManager.getMgr().init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        UtilManager.getMgr().getMessager().printMessage(Diagnostic.Kind.NOTE, "process");
        Set<? extends Element> elementSets = roundEnvironment.getElementsAnnotatedWith(Route.class);
        List<TargetInfo> targetInfoList = new ArrayList<>();
        for (Element element : elementSets) {
            //检查类型R
            if (!UtilManager.checkTypeValid(element)) continue;
            //正常的类型。R
            TypeElement typeElement = (TypeElement) element;
            Route route = typeElement.getAnnotation(Route.class);
            String[] value = route.value();
            if (value.length == 0) continue;
            for (String v : value) {
                if ("".equals(v) || v.length() == 0) continue;
                targetInfoList.add(new TargetInfo(typeElement, v));
            }
            if (!targetInfoList.isEmpty()) {
                generateCode(targetInfoList);
            }
        }
        return false;
    }

    private void generateCode(List<TargetInfo> targetInfoList) {
        TypeElement activityType=UtilManager.getMgr()
                .getElementUtils().getTypeElement("android.app.Activity");
        ParameterizedTypeName actParam=ParameterizedTypeName.get(ClassName.get(Class.class),
                WildcardTypeName.subtypeOf(ClassName.get(activityType)));
        ParameterizedTypeName mapParam=ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class),actParam);

        ParameterSpec spec=ParameterSpec.builder(mapParam,"routers").build();

        MethodSpec.Builder builder=MethodSpec.methodBuilder("initRouter")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(spec);
        for (TargetInfo info : targetInfoList) {
            builder.addStatement("routers.put($S, $T.class)", info.getRoute(), info.getTypeElement());
        }

        TypeElement interfaceType = UtilManager
                .getMgr()
                .getElementUtils()
                .getTypeElement("com.zhangshuo.zapi.IRoute");

        TypeSpec typeSpec = TypeSpec.classBuilder("AppRouter")
                .addSuperinterface(ClassName.get(interfaceType))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(builder.build())
                .addJavadoc("Generated by Router. Do not edit it!\n")
                .build();
        try {
            JavaFile.builder("com.zhangshuo.zapproute", typeSpec)
                    .build()
                    .writeTo(UtilManager.getMgr().getFiler());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Route.class.getCanonicalName());
    }
}

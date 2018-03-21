package com.zhangshuo.zcompiler;

import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class UtilManager {
  /**
   * 一个用来处理TypeMirror的工具类
   */
  private Types typeUtils;
  /**
   * 一个用来处理Element的工具类
   */
  private Elements elementUtils;
  /**
   * 正如这个名字所示，使用Filer你可以创建文件
   */
  private Filer filer;
  /**
   * 日志相关的辅助类
   */
  private Messager messager;

  private static UtilManager mgr = new UtilManager();

  public void init(ProcessingEnvironment environment) {
      setTypeUtils(environment.getTypeUtils());
      setElementUtils(environment.getElementUtils());
      setFiler(environment.getFiler());
      setMessager(environment.getMessager());
  }

  private UtilManager() {
  }

    public Types getTypeUtils() {
        return typeUtils;
    }

    public void setTypeUtils(Types typeUtils) {
        this.typeUtils = typeUtils;
    }

    public Elements getElementUtils() {
        return elementUtils;
    }

    public void setElementUtils(Elements elementUtils) {
        this.elementUtils = elementUtils;
    }

    public Filer getFiler() {
        return filer;
    }

    public void setFiler(Filer filer) {
        this.filer = filer;
    }

    public Messager getMessager() {
        return messager;
    }

    public void setMessager(Messager messager) {
        this.messager = messager;
    }

    public static UtilManager getMgr() {
        return mgr;
    }

    public static void setMgr(UtilManager mgr) {
        UtilManager.mgr = mgr;
    }


    public static boolean checkTypeValid(Element element){
      //只能注解到类
        if(element.getKind()!= ElementKind.CLASS){
            return false;
        }
        TypeElement typeElement= (TypeElement) element;
        Set<Modifier> modifiers=typeElement.getModifiers();
        if(modifiers.contains(Modifier.PRIVATE)||
                modifiers.contains(Modifier.ABSTRACT)||
                        (!isSuperClass(typeElement,"android.app.Activity")))
            return false;
        return true;
    }
    public static boolean isSuperClass(TypeElement type, String superClass) {
        return !(type == null || "java.lang.Object".equals(type.getQualifiedName().toString()))
                && (type.getQualifiedName().toString().equals(superClass)
                || isSuperClass((TypeElement) UtilManager.getMgr().getTypeUtils().asElement(type.getSuperclass()), superClass));
    }
}
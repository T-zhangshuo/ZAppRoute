package com.zhangshuo.zcompiler.Bean;

import javax.lang.model.element.TypeElement;

public class TargetInfo {

    private TypeElement mTypeElement;
    private String mRoute;

    public TargetInfo() {
    }

    public TargetInfo(TypeElement typeElement, String route) {
        mTypeElement = typeElement;
        mRoute = route;
    }

    public TypeElement getTypeElement() {
        return mTypeElement;
    }

    public void setTypeElement(TypeElement typeElement) {
        mTypeElement = typeElement;
    }

    public String getRoute() {
        return mRoute;
    }

    public void setRoute(String route) {
        mRoute = route;
    }
}
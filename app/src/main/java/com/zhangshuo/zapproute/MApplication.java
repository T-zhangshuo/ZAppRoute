package com.zhangshuo.zapproute;

import android.app.Application;

import com.zhangshuo.zapi.RouterManager;

/**
 * Created by zhangshuo on 2018/3/21.
 */

public class MApplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化路由
        RouterManager.getManager().init();
    }
}

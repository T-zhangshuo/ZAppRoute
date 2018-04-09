package com.zhangshuo.zapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RouterManager {

    private static volatile RouterManager sManager;
    private Map<String, Class<? extends Activity>> mTables;

    public static final int REQUEST_CODE = 7777;
    public static final int RESULT_CODE = 8888;
    private IRouteFilter iRouteFilter;

    private RouterManager() {
        mTables = new HashMap<>();
    }

    public static RouterManager getManager() {
        if (sManager == null) {
            synchronized (RouterManager.class) {
                if (sManager == null) {
                    sManager = new RouterManager();
                }
            }
        }
        return sManager;
    }

    public void init(Context context) {
        try {
            //扫描包名为 com.zhangshuo.zapproute下的所有
            List<String> routerList = PackageManager.getClassNameInPackage(context, "com.zhangshuo.zapproute");
            for (int i = 0; i < routerList.size(); i++) {
                String className = routerList.get(i);
                Log.i("TAG", "---" + className);
                Class<?> moduleRouteTable = Class.forName(className);
                Constructor constructor = moduleRouteTable.getConstructor();
                IRoute instance = (IRoute) constructor.newInstance();
                instance.initRouter(mTables);
            }
            //打印所有的路由地址
            for (String key : mTables.keySet()) {
                Log.i("TAG", "--key:" + key + "--value:" + mTables.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * @Author zhangshuo
     * @editTime 2018/3/21 上午9:48
     * @Dec 打开访问的地址 path：普通地址（注解部分）或含scheme协议地址(清单文件定义的部分）
     * openXXXX  为 startActivity 或者 startActivityForResult
     */
    public void open(Context context, String path) {
        open(context, path, null);
    }

    public void open(Context context, String path, Bundle bundle) {
        openForResult(context, path, -1, bundle);
    }

    public void openForResult(Context context, String path) {
        openForResult(context, path, REQUEST_CODE, null);
    }

    public void openForResult(Context context, String path, Bundle bundle) {
        openForResult(context, path, REQUEST_CODE, bundle);
    }


    public void openForResult(Context context, final String path, int requestCode, Bundle bundle) {
        if (TextUtils.isEmpty(path)) return;
        //判断拦截器是否需要拦截。
        boolean beforeHandler = (iRouteFilter != null && iRouteFilter.beforeHandler(path));
        if (beforeHandler) {
            //销毁拦截器 防止在拦截器中添加路由事件，而当前方法并未执行结束，iRouterFilter被重新引用。
            IRouteFilter tmpFilter = iRouteFilter;
            iRouteFilter = null;
            tmpFilter.interception(path);
            return;
        }
        boolean isActivity = context instanceof Activity;
        Intent intent = null;
        if (path.contains("://")) {
            //为scheme协议地址
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            //判断是否是否存在该协议地址，不存在，则不跳转
            List<ResolveInfo> activitys = context.getPackageManager().queryIntentActivities(intent, 0);
            if (activitys.isEmpty()) intent = null;
        } else {
            //普通的注解地址
            Class aClass = mTables.get(path);

            if (aClass != null) {
                intent = new Intent(context, aClass);
                //如果不是Activity 则增加newTask
                if (!isActivity)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }
        if (intent != null) {
            if (bundle != null) intent.putExtras(bundle);
            if (requestCode == -1 || !isActivity) {
                //不需要返回值的  或者不是Activity 的
                context.startActivity(intent);
            } else
                ((Activity) context).startActivityForResult(intent, requestCode);
        }
        if (iRouteFilter != null) {
            iRouteFilter.afterHandler(path);
            //销毁拦截器
            iRouteFilter = null;
        }
    }

    /*
     * @Author zhangshuo
     * @editTime 2018/3/21 上午10:16
     * @Dec 关闭当前界面，可以产生onActivityForResult
     */
    public void setFinishResult(Activity activity, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null)
            intent.putExtras(bundle);
        activity.setResult(RESULT_CODE, intent);
        activity.finish();
    }

    public Bundle getBundle(Activity activity) {
        Bundle extras = activity.getIntent().getExtras();
        if (extras == null)
            extras = new Bundle();
        return extras;
    }

    /*
     * @Author zhangshuo
     * @editTime 2018/3/21 上午10:31
     * @Dec 业务拦截器
     */
    public RouterManager setFilter(IRouteFilter iRouteFilter) {
        this.iRouteFilter = iRouteFilter;
        return sManager;
    }
}
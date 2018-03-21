package com.zhangshuo.zapi;

/**
 * Created by zhangshuo on 2018/3/21.
 *
 * @desc 拦截器，可针对性实现拦截。
 */

public interface IRouteFilter {

    /*
    * @Author zhangshuo
    * @editTime 2018/3/21 上午10:29
    * @Dec 发生该跳转事件前
    */
    boolean beforeHandler(String path);

    /*
    * @Author zhangshuo
    * @editTime 2018/3/21 下午2:41
    * @Dec beforeHandler返回为true时执行
    */
    void interception(String path);
    /*
    * @Author zhangshuo
    * @editTime 2018/3/21 上午10:30
    * @Dec 发生该跳转事件后
    */
    void afterHandler(String path);

}

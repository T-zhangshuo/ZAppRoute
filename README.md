# ZAppRoute
Android 路由。利用apt自动加入到路由表。

- 支持apt自动加入到路由表。
- 支持scheme协议跳转。
- 支持bundle传值。具体在示例APP中查看。
- 支持多路径 指向同一个Activity
- 支持跳转结果回调(onActivityForResult)
- 增加拦截器
## 引入

project中到build.gradle 中添加
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
在module中build.gradle中defaultconfig中添加
```java
javaCompileOptions {
            annotationProcessorOptions {
                includeCompileClasspath = true
                arguments = [moduleName: project.getName()]
            }
        }
```

在module中到build.gradle 中添加依赖

```java
api 'com.github.T-zhangshuo.ZAppRoute:zapi:1.1'//基础模块添加一次即可
annotationProcessor 'com.github.T-zhangshuo.ZAppRoute:zcompiler:1.1'//需要注解的模块都要添加
```

在你的主APP中的application中初始化路由表
```java
RouterManager.getManager().init(context);
```
 
在需要使用的Activity 中，添加注解
```java
@Route({"second1", "second2"})
public class SecondActivity extends Activity 
```


使用
 ```java
 //具体参数见提示
  RouterManager.getManager().open(Object... param);
  RouterManager.getmanager().openForResult(Object...param);
 ```

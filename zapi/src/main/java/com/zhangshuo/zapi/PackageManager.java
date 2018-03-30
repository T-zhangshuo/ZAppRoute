package com.zhangshuo.zapi;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

import dalvik.system.DexFile;

/**
 * Created by zhangshuo on 2018/3/30.
 */

public class PackageManager {

    public  static List<String> getClassNameInPackage(Context context, String packageName) {
        List<String> classNameList = new ArrayList<String>();
        try {
            String packageCodePath = context.getPackageCodePath();
            File dir=new File(packageCodePath).getParentFile();
            for(File file:dir.listFiles()){
                if(file.getName().contains(".apk")){
                    DexFile dexFile=new DexFile(file);
                    Enumeration<String> entries = dexFile.entries();
                    while (entries.hasMoreElements()){
                        String className=entries.nextElement();
                        //如果包含包名，且包含AppRouter.
                        if(className.contains(packageName+".AppRouter")){
                            classNameList.add(className);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return classNameList;
    }
}

package com.zhangshuo.zapproute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangshuo.zapi.IRouteFilter;
import com.zhangshuo.zapi.RouterManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void gotoPath1(View view){
        RouterManager.getManager().open(this,"second1");
    }

    public void gotoPath2(View view){
        Bundle bundle=new Bundle();
        bundle.putString("TIP",((TextView)view).getText().toString());
        RouterManager.getManager().open(this,"second2",bundle);
    }

    //见清单文件 设置 scheme协议
    public void gotoPath3(View view){
        Bundle bundle=new Bundle();
        bundle.putString("TIP",((TextView)view).getText().toString());
        RouterManager.getManager().open(this,"scheme://host:8888/testpath",bundle);
    }

    public void gotoPath4(View view){
        Bundle bundle=new Bundle();
        bundle.putString("TIP",((TextView)view).getText().toString());
        RouterManager.getManager().openForResult(this,"second2");
    }

    public void gotoPath5(View view){

        RouterManager.getManager().openForResult(this,"scheme://host:8888/testpath");
    }

    public void gotoPath6(View view){
        RouterManager.getManager().setFilter(new IRouteFilter() {
            @Override
            public boolean beforeHandler(String path) {
                if(path.contains("second")){
                    Toast.makeText(MainActivity.this,"拦截成功!",Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;//返回true表示拦截（执行interception方法)，false不拦截
            }

            @Override
            public void interception(String path) {
                //beforeHandler返回为true 的时候，执行
                RouterManager.getManager().open(MainActivity.this,"second1");
            }

            @Override
            public void afterHandler(String path) {
                //处理完成后
                Toast.makeText(MainActivity.this,"拦截处理完成!",Toast.LENGTH_SHORT).show();
            }
        }).open(this,"second2");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RouterManager.REQUEST_CODE
                &&resultCode==RouterManager.RESULT_CODE){
            Bundle extras = data.getExtras();
            if(extras!=null) {
                String result = extras.getString("TIP");
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

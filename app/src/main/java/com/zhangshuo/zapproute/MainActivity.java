package com.zhangshuo.zapproute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    public void gotoPath3(View view){
        RouterManager.getManager().open(this,"scheme://host:8888/testpath");
    }

    public void gotoPath4(View view){
        RouterManager.getManager().openForResult(this,"second2");
    }

    public void gotoPath5(View view){
        RouterManager.getManager().openForResult(this,"scheme://host:8888/testpath");
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

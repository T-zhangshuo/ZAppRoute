package com.zhangshuo.zapproute;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.zhangshuo.zanno.Route;
import com.zhangshuo.zapi.RouterManager;

/**
 * Created by zhangshuo on 2018/3/21.
 */

@Route({"second1", "second2"})
public class SecondActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Bundle extras = RouterManager.getManager().getBundle(this);
        String tip = extras.getString("TIP");
        Toast.makeText(this, "第一个页面：" + tip, Toast.LENGTH_SHORT).show();
    }

    public void finish(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("TIP", "第二个页面回传值");
        RouterManager.getManager().setFinishResult(this, bundle);
    }
}

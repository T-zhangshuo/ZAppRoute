package com.mandofin.basemodule;

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

@Route({"third1", "third2"})
public class ThirdActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Bundle extras = RouterManager.get().getBundle(this);
        String tip = extras.getString("TIP");
        Toast.makeText(this, "第一个页面：" + tip, Toast.LENGTH_SHORT).show();
    }

    public void finish(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("TIP", "第二个页面回传值");
        RouterManager.get().setFinishResult(this, bundle);
    }
}

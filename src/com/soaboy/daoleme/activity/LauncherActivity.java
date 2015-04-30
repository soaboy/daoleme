package com.soaboy.daoleme.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import com.soaboy.daoleme.R;

/**
 * Created by liuyun on 2015/4/21.
 */
public class LauncherActivity extends Activity {
    SharedPreferences sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_launcher);
        sp=getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        sp.getString("AUTO_LOGIN","");
        Log.i("aa", sp.getString("AUTO_LOGIN",""));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sp.getString("AUTO_LOGIN","").equals("true")){
                    String s=sp.getString("MAC","");
                    Log.i("AA", s);
                    startActivity(new Intent(LauncherActivity.this,MainActivity.class));
                }
                else{
                startActivity(new Intent(LauncherActivity.this,RegisterActivity.class));
                }
                LauncherActivity.this.finish();
            }
        },1500);


    }


}
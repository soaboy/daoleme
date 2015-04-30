package com.soaboy.daoleme.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.soaboy.daoleme.R;
import com.soaboy.daoleme.fragment.HomeFragment;
import com.soaboy.daoleme.fragment.MeetingFragment;
import com.soaboy.daoleme.fragment.WeatherFragment;

public class MainActivity extends Activity implements View.OnClickListener {
    private long exitTime;
    //首页
    private HomeFragment homeFragment;
    private MeetingFragment meetingFragment;
    private WeatherFragment weatherFragment;

    private FragmentManager fragmentManager;

    //Tab导航的布局文件
    private View homeLayout;
    private  View meetingLayout;
    private View weatherLayout;

    //Tab导航中的各个控件
    private ImageView homeImage;
    private TextView homeText;
    private ImageView meetingImage;
    private TextView meetingText;
    private ImageView weatherImage;
    private TextView weatherText;

    //初始化Tab导航控件，设置导航和标题栏按钮的单击事件
    private void initViews() {
        homeLayout = findViewById(R.id.main_tab_home);
        homeImage = (ImageView) findViewById(R.id.main_tab_home_image);
        homeText = (TextView) findViewById(R.id.main_tab_home_text);
        meetingLayout=findViewById(R.id.main_tab_meeting);
        meetingImage=(ImageView)findViewById(R.id.main_tab_meeting_image);
        meetingText=(TextView)findViewById(R.id.main_tab_meeting_text);
        weatherLayout=findViewById(R.id.main_tab_weather);
        weatherImage=(ImageView)findViewById(R.id.main_tab_weather_image);
        weatherText=(TextView)findViewById(R.id.main_tab_weather_text);
        homeLayout.setOnClickListener(this);
        meetingLayout.setOnClickListener(this);
        weatherLayout.setOnClickListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();
        fragmentManager = getFragmentManager();
        setTabSelection(0);
    }

    //加载菜单文件
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // 菜单选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Tab导航栏和标题栏按钮的单击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tab_home:
                setTabSelection(0);
                break;
            case R.id.main_tab_meeting:
                setTabSelection(1);
                break;
            case R.id.main_tab_weather:
                setTabSelection(2);
                break;
        }
    }

    // 切换Tab导航时设置背景和字体的样式
    public void setTabSelection(int index) {
        clearTabSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                homeImage.setImageResource(R.drawable.ic_launcher);
                homeText.setTextColor(Color.parseColor("#5286BF"));
                homeLayout.setBackgroundColor(Color.WHITE);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.main_content_frame, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                meetingImage.setImageResource(R.drawable.ic_launcher);
                meetingText.setTextColor(Color.parseColor("#5286BF"));
                meetingLayout.setBackgroundColor(Color.WHITE);
                if (meetingFragment==null){
                    meetingFragment=new MeetingFragment();
                    transaction.add(R.id.main_content_frame,meetingFragment);
                }else{
                    transaction.show(meetingFragment);
                }
                break;
            case 2:
                weatherImage.setImageResource(R.drawable.ic_launcher);
                weatherText.setTextColor(Color.parseColor("#5286BF"));
                weatherLayout.setBackgroundColor(Color.WHITE);
                if (weatherFragment==null){
                    weatherFragment=new WeatherFragment();
                    transaction.add(R.id.main_content_frame,weatherFragment);
                }else{
                    transaction.show(weatherFragment);
                }
                break;
        }
        transaction.commit();
    }

    // 隐藏所有Fragment
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if(meetingFragment!=null){
            transaction.hide(meetingFragment);
        }
        if(weatherFragment!=null){
            transaction.hide(weatherFragment);
        }
    }

    // 清空Tab导航栏的选定状态
    private void clearTabSelection() {
        homeImage.setImageResource(R.drawable.ic_launcher);
        homeText.setTextColor(Color.WHITE);
        meetingImage.setImageResource(R.drawable.ic_launcher);
        meetingText.setTextColor(Color.WHITE);
        weatherImage.setImageResource(R.drawable.ic_launcher);
        weatherText.setTextColor(Color.WHITE);
        homeLayout.setBackgroundColor(Color.parseColor("#5286BF"));
        meetingLayout.setBackgroundColor(Color.parseColor("#5286BF"));
        weatherLayout.setBackgroundColor(Color.parseColor("#5286BF"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

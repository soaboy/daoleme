package com.soaboy.daoleme.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.soaboy.daoleme.R;
import com.soaboy.daoleme.util.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.content.SharedPreferences.Editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyun on 2015/4/19.
 */
public class RegisterActivity extends Activity {
    private static final int REQUEST_TIMEOUT = 5 * 1000;//设置请求超时5秒钟
    private static final int SO_TIMEOUT = 5 * 1000;  //设置等待数据超时时间10秒钟
    private EditText userText = null;
    private EditText phoneText = null;
    private EditText emailText = null;
    private EditText noText = null;
    private EditText deptText = null;
    private EditText pswdText = null;
    private Button registerbutton = null;
    private ProgressDialog mDialog;
    private SharedPreferences sp;
    String responseMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        //获得实例对象
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        userText = (EditText) findViewById(R.id.user_edit);
        noText = (EditText) findViewById(R.id.no_edit);
        phoneText = (EditText) findViewById(R.id.phone_edit);
        pswdText = (EditText) findViewById(R.id.password_edit);
        registerbutton = (Button) findViewById(R.id.register_btn);
        CheckNetworkState();
        registerbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mDialog = new ProgressDialog(RegisterActivity.this);
                mDialog.setTitle("注册");
                mDialog.setMessage("正在请求服务器，请稍后...");
                mDialog.show();
                Thread registerThread = new Thread(new registerThread());
                registerThread.start();
                //OKRegister();
            }
        });
    }

    //保存用户的登录状态
    public void saveLoginState(String registerValidate,String no,String mac) {
        if (registerValidate.equals("success")) {
            Editor editor = sp.edit();
            editor.putString("NO", no);
            editor.putString("MAC", mac);
            editor.putString("AUTO_LOGIN","true");
            editor.commit();
        }
    }

    public void OKRegister() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    //获取手机MAC地址
    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 注册成功向LoginActivity登陆框传递注册信息
     */
    public void putData(String user) {
        Intent intent = new Intent();
        intent.putExtra("Usertext", user);// 把姓名以参数的方式传递给Activity2
        intent.setClass(RegisterActivity.this, MainActivity.class);
    }

    private String registerServer(String no, String mac) {
        String registerValidate = "";

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        //paramsList.add(new BasicNameValuePair("username", user));
        paramsList.add(new BasicNameValuePair("user.no", no));
        paramsList.add(new BasicNameValuePair("user.mac", mac));

      /* if (!mac.trim().equals("") && !user.trim().equals("")) {
            registerValidate = HttpUtil
                    .getJsonContentByPsot(
                            "http://192.168.139.1:8080/register/RegisterServlet",
                            paramsList);

        }*/
        if (!mac.trim().equals("") && !no.trim().equals("")) {
            registerValidate = HttpUtil
                    .getJsonContentByPsot(
                            "http://192.168.139.1:8080/Meeting/user/user_regist?",
                            paramsList);
        }
       saveLoginState(registerValidate,no,mac);//注册成功则保存用户的唯一标示信息，下次自动登录
        return registerValidate;
    }

    // Handler
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "用户名已存在",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    mDialog.cancel();

                    Toast.makeText(getApplicationContext(), "注册成功！",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "请将注册信息填写完整",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    // registerThread线程类
    class registerThread implements Runnable {

        @Override
        public void run() {
            String user = userText.getText().toString();
            String mac = getLocalMacAddress();
            String no = noText.getText().toString();
            String registerValidate = registerServer(no, mac);
            System.out.println("----------------------------loginValidate is :"
                    + registerValidate + "----------");
            Message msg = handler.obtainMessage();
            if (!no.trim().equals("") && !mac.trim().equals("")) {
                if (registerValidate.equals("failed")) {
                    msg.what = 0;
                    handler.sendMessage(msg);
                    System.out.println("BBB---用户名已存在");

                }
                if (registerValidate.equals("success")) {
                    msg.what = 1;
                    handler.sendMessage(msg);
                    System.out.println("BBB---注册成功");
                }
            } else {
                msg.what = 2;
                handler.sendMessage(msg);
                System.out.println("BBB---请正确填写注册信息");
            }
        }

    }

    /**
     * 检查网络状态
     */
    public void CheckNetworkState() {
        boolean flag = false;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        // 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING)
            return;
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
            return;
        showTips();
    }

    private void showTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("没有可用网络");
        builder.setMessage("当前网络不可用，是否设置网络？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 如果没有网络连接，则进入网络设置界面
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                RegisterActivity.this.finish();
            }
        });
        builder.create();
        builder.show();
    }


}

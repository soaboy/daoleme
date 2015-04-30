package com.soaboy.daoleme.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.soaboy.daoleme.R;

/**
 * Created by liuyun on 2015/4/22.
 */
public class HomeFragment extends Fragment {
    private int count = 100;

    // 倒计时TextView
    private TextView timerText;

    private Handler handler = new Handler();

    private Runnable runTimer = new Runnable() {

        @Override
        public void run() {
            if (count > 0) {
                timerText.setText("距离本次会议开始还有:" + count);
                count--;
                handler.postDelayed(runTimer, 1000);
            } else {
                timerText.setText("本次会议已经开始");
                return;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeLayout = inflater.inflate(R.layout.fragment_home, container,
                false);
        timerText = (TextView) homeLayout
                .findViewById(R.id.home_currentmeeting_timer);
        handler.postDelayed(runTimer, 1000);
        return homeLayout;
    }

}

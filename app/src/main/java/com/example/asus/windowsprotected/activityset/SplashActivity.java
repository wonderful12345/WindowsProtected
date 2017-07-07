package com.example.asus.windowsprotected.activityset;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.windowsprotected.MainActivity;
import com.example.asus.windowsprotected.R;
import com.example.asus.windowsprotected.otherset.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.animation;

public class SplashActivity extends AppCompatActivity {

    private TextView tv_time_count;
    private int count=1;
    private Animation mAnimation;
    private ImageView iv_splash;

//    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd- HH:mm:ss ");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            String hour = str.substring(12,14);
            String minute = str.substring(15,17);
            int hour1 = Integer.valueOf(hour);
            int minute1 = Integer.valueOf(minute);
            if ((hour1>18)||(hour1==18&&minute1>=30)||(hour1>=0&&hour1<=6)){
                tintManager.setStatusBarTintResource(R.color.night_mode);//通知栏所需颜色
            }else {
                tintManager.setStatusBarTintResource(R.color.top_bg_color);//通知栏所需颜色
            }
        }
        setContentView(R.layout.activity_splash);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd- HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String hour = str.substring(12,14);
        String minute = str.substring(15,17);
        int hour1 = Integer.valueOf(hour);
        int minute1 = Integer.valueOf(minute);
        if ((hour1>18)||(hour1==18&&minute1>=30)||(hour1>=0&&hour1<=6)){
            iv_splash.setBackgroundResource(R.drawable.r8_night);
        }else {
            iv_splash.setBackgroundResource(R.drawable.r8);
        }
        tv_time_count = (TextView) findViewById(R.id.tv_time_count);
        mAnimation = AnimationUtils.loadAnimation(this,R.anim.animation);
        handler.sendEmptyMessageDelayed(0,1000);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private int getCount(){
        count--;
        if (count==0){
            Intent intent = new Intent();
            intent.setClass(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return count;
    }
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                tv_time_count.setText(getCount()+"");
                handler.sendEmptyMessageDelayed(0, 1000);
                mAnimation.reset();
                tv_time_count.startAnimation(mAnimation);
            }
        }
    };
}

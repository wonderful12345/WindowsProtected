package com.example.asus.windowsprotected.activityset;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.asus.windowsprotected.bean.notification_item_bean;
import com.example.asus.windowsprotected.adaterset.notification_text_adater;

import com.example.asus.windowsprotected.R;
import com.example.asus.windowsprotected.otherset.DataBaseSQLHelp;
import com.example.asus.windowsprotected.otherset.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private List<notification_item_bean> mData;
    private notification_text_adater mNotification_text_adater;
    private RecyclerView mRecyclerView_notification;
    private DataBaseSQLHelp mDataBaseSQLHelp_notification_text;
    private Cursor cursor;
    private SQLiteDatabase db;
    private Toolbar mToolbar;
    private String user_name;
    private SharedPreferences mSharedPreferences_get_user_name;
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
        setContentView(R.layout.activity_notification);
        initview();
        setToolbar();
        initRecycler();
        cursor_Data(user_name);
    }

    /**
     * toolbar 随着模式的改变而改变
     */
    private void setToolbar() {
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd- HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String hour = str.substring(12,14);
        String minute = str.substring(15,17);
        int hour1 = Integer.valueOf(hour);
        int minute1 = Integer.valueOf(minute);
        if ((hour1>18)||(hour1==18&&minute1>=30)||(hour1>=0&&hour1<=6)){
            mToolbar.setBackgroundResource(R.color.night_mode);
        }else {
            return;
        }
    }

    /**
     * 对Recyclerview设置
     */
    private void initRecycler() {
        mRecyclerView_notification.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView_notification.setItemAnimator(new DefaultItemAnimator());
        mNotification_text_adater = new notification_text_adater(NotificationActivity.this,mData);
        mRecyclerView_notification.setAdapter(mNotification_text_adater);
    }

    /**
     * 控件初始化
     */
    private void initview() {
        mRecyclerView_notification = (RecyclerView) findViewById(R.id.recycler_notification);
        mData = new ArrayList<>();
        mToolbar = (Toolbar) findViewById(R.id.toolbar_notification);
        mDataBaseSQLHelp_notification_text = new DataBaseSQLHelp(this,"NOTIFICATION_TEXT.db",null,1);
        db = mDataBaseSQLHelp_notification_text.getWritableDatabase();
        cursor = db.query("NOTIFICATION_TEXT",null,null,null,null,null,null);
        mSharedPreferences_get_user_name = getSharedPreferences("user_name",MODE_PRIVATE);
        user_name = mSharedPreferences_get_user_name.getString("user","");
        Log.d("user_name",user_name);

    }


    /**
     * 获取标题栏的相关信息
     * @param on
     */
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
    /**
     * 遍历数据库
     * @param
     */
    public void cursor_Data(String register_name){
        cursor = db.query("NOTIFICATION_TEXT",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                String s1 = cursor.getString(cursor.getColumnIndex("register_name"));
                String s2 = cursor.getString(cursor.getColumnIndex("data_time"));
                String s3 = cursor.getString(cursor.getColumnIndex("notification_content"));
                if (s1.equals(register_name)){
                    mData.add(new notification_item_bean(s2,s3));
                    int h = mData.size();
                    if (h>0){
                        mNotification_text_adater.notifyDataSetChanged();
                    }
                }
            }while (cursor.moveToNext());
        }
    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

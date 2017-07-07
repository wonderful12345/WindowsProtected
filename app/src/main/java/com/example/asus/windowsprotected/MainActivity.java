package com.example.asus.windowsprotected;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.example.asus.windowsprotected.activityset.Room_window_add;
import com.example.asus.windowsprotected.bean.itembean;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.windowsprotected.otherset.DataBaseSQLHelp;
import com.example.asus.windowsprotected.otherset.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.asus.windowsprotected.adaterset.room_item_adater;
import com.example.asus.windowsprotected.voice.VoiceToWord;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton_add_room;
    private DataBaseSQLHelp mDataBaseSQLHelp_add_room;
    private Cursor cursor;
    private RecyclerView mRecyclerView;
    private View mView;
    private String text;
    private SQLiteDatabase db;
    private SharedPreferences mSharedPreferences_get_user;
    private String user_name;
    private CheckBox cb_bedroom;
    private CheckBox cb_cookroom;
    private CheckBox cb_keting;
    private CheckBox cb_workroom;
    private CheckBox cb_otherroom;
    //private Button btn_source;
    private Button btn_sure;
    private Button btn_cancel;
    private ContentValues values;
    private List<itembean> list_mItembeen;
    private room_item_adater mAdater_room_item;
    private int check_num = 0;
    private String window_style = null;
    private SharedPreferences.Editor mEditor;
    public static EditText et_room_name;
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
        setContentView(R.layout.activity_main);
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
        list_mItembeen = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdater_room_item = new room_item_adater(this,list_mItembeen);
        mRecyclerView.setAdapter(mAdater_room_item);
        mAdater_room_item.setOnItemClickListener(new room_item_adater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String room_title = list_mItembeen.get(position).getRoom_name();
                Intent intent = new Intent();
                intent.putExtra("title",room_title);
                intent.setClass(MainActivity.this, Room_window_add.class);
                startActivity(intent);
            }
        });
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
    /**
     * 控件初始化
     */
    private void initview() {
        mDataBaseSQLHelp_add_room = new DataBaseSQLHelp(this,"Room_Add.db",null,1);
        values = new ContentValues();
        db = mDataBaseSQLHelp_add_room.getWritableDatabase();
        cursor = db.query("Room_Add",null,null,null,null,null,null);
        mSharedPreferences_get_user = getSharedPreferences("user_name",MODE_PRIVATE);
        user_name = mSharedPreferences_get_user.getString("user","");
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        et_room_name = (EditText) findViewById(R.id.et_room_name);
        mToolbar.setTitle("");
        //mLinearLayout_add_room = (LinearLayout) findViewById(R.id.linear_add_room);
        //mLinearLayout_add_room.setOnClickListener(this);
        mFloatingActionButton_add_room = (FloatingActionButton) findViewById(R.id.fab_add_room);
        mFloatingActionButton_add_room.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        mEditor = getSharedPreferences("data",MODE_PRIVATE).edit();
        mEditor.putString("popupwindow","false");
        mEditor.commit();
    }

    /**
     * 显示新建房间对话框
     */
    private void show_add_room_dialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        mView = layoutInflater.inflate(R.layout.dialog_of_add_room,null);
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("添加房间").setIcon(R.drawable.room_icon).setView(mView).show();
        et_room_name = (EditText) mView.findViewById(R.id.et_room_name);
        cb_bedroom = (CheckBox) mView.findViewById(R.id.cb_bedroom);
        cb_cookroom = (CheckBox) mView.findViewById(R.id.cb_cookroom);
        cb_keting = (CheckBox) mView.findViewById(R.id.cb_keting);
        cb_workroom = (CheckBox) mView.findViewById(R.id.cb_workroom);
        cb_otherroom = (CheckBox) mView.findViewById(R.id.cb_otherroom);
        btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        btn_sure = (Button) mView.findViewById(R.id.btn_sure);
        //btn_source = (Button) mView.findViewById(R.id.btn_source);
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd- HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String hour = str.substring(12,14);
        String minute = str.substring(15,17);
        int hour1 = Integer.valueOf(hour);
        int minute1 = Integer.valueOf(minute);
        if ((hour1>18)||(hour1==18&&minute1>=30)||(hour1>=0&&hour1<=6)){
            btn_sure.setBackgroundResource(R.drawable.button_style_night);
            btn_cancel.setBackgroundResource(R.drawable.button_style_night);
        }else {
            btn_cancel.setBackgroundResource(R.drawable.button_style);
            btn_sure.setBackgroundResource(R.drawable.button_style);
        }
        cb_bedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_bedroom.isChecked()){
                    cb_otherroom.setClickable(false);
                    cb_workroom.setClickable(false);
                    cb_keting.setClickable(false);
                    cb_cookroom.setClickable(false);
                    check_num = 1;
                    window_style = "卧室";
                } else{
                    cb_otherroom.setClickable(true);
                    cb_workroom.setClickable(true);
                    cb_keting.setClickable(true);
                    cb_cookroom.setClickable(true);
                    check_num = 0;
                    window_style = null;
                }
            }
        });
        cb_cookroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_cookroom.isChecked()){
                    cb_otherroom.setClickable(false);
                    cb_workroom.setClickable(false);
                    cb_keting.setClickable(false);
                    cb_bedroom.setClickable(false);
                    check_num = 2;
                    window_style = "厨房";
                } else{
                    cb_otherroom.setClickable(true);
                    cb_workroom.setClickable(true);
                    cb_keting.setClickable(true);
                    cb_bedroom.setClickable(true);
                    check_num = 0;
                    window_style = null;
                }
            }
        });
        cb_keting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_keting.isChecked()){
                    cb_otherroom.setClickable(false);
                    cb_workroom.setClickable(false);
                    cb_cookroom.setClickable(false);
                    cb_bedroom.setClickable(false);
                    check_num = 3;
                    window_style = "客厅";
                } else{
                    cb_otherroom.setClickable(true);
                    cb_workroom.setClickable(true);
                    cb_cookroom.setClickable(true);
                    cb_bedroom.setClickable(true);
                    check_num = 0;
                    window_style = null;
                }
            }
        });
        cb_workroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_workroom.isChecked()){
                    cb_otherroom.setClickable(false);
                    cb_cookroom.setClickable(false);
                    cb_keting.setClickable(false);
                    cb_bedroom.setClickable(false);
                    check_num = 4;
                    window_style = "办公室";
                } else{
                    cb_otherroom.setClickable(true);
                    cb_cookroom.setClickable(true);
                    cb_keting.setClickable(true);
                    cb_bedroom.setClickable(true);
                    check_num = 0;
                    window_style = null;
                }
            }
        });
        cb_otherroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_otherroom.isChecked()){
                    cb_cookroom.setClickable(false);
                    cb_workroom.setClickable(false);
                    cb_keting.setClickable(false);
                    cb_bedroom.setClickable(false);
                    check_num = 5;
                    window_style = "其他";
                } else{
                    cb_cookroom.setClickable(true);
                    cb_workroom.setClickable(true);
                    cb_keting.setClickable(true);
                    cb_bedroom.setClickable(true);
                    check_num = 0;
                    window_style = null;
                }
            }
        });

        /*btn_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceToWord voice = new VoiceToWord(MainActivity.this,"534e3fe2");
                voice.GetWordFromVoice();
            }
        });*/
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String room_name = et_room_name.getText().toString();
                Boolean room_exist = true;
                cursor = db.query("Room_Add",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do{
                        String s1 = cursor.getString(cursor.getColumnIndex("register_name"));
                        String s2 = cursor.getString(cursor.getColumnIndex("room_name"));
                        Log.d("s1",s1);
                        Log.d("usr",user_name);
                        Log.d("room",s2);
                        if (room_name.equals(s2)&&user_name.equals(s1)){
                            room_exist = false;
                            break;
                        }
                    }while (cursor.moveToNext());
                }
                if (check_num==0|| TextUtils.isEmpty(room_name)||room_exist==false){
                    if (room_exist){
                        Toast.makeText(MainActivity.this,"请补充完信息",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this,"房间名已经存在",Toast.LENGTH_LONG).show();
                    }
                } else {
                    list_mItembeen.add(new itembean(room_name,window_style,check_num-1));
                    values.put("register_name",user_name);
                    values.put("room_name",room_name);
                    values.put("room_style",window_style);
                    values.put("room_image_num",check_num);
                    db.insert("Room_Add",null,values);
                    values.clear();
                    int h = list_mItembeen.size();
                    if (h>0){
                        mAdater_room_item.notifyDataSetChanged();
                    }
                    alertDialog.dismiss();
                    Toast.makeText(MainActivity.this,String.valueOf(check_num),Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    /**
     * 遍历数据库
     * @param
     */
    public void cursor_Data(String register_name){
        cursor = db.query("Room_Add",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                String s1 = cursor.getString(cursor.getColumnIndex("register_name"));
                String s2 = cursor.getString(cursor.getColumnIndex("room_name"));
                String s3 = cursor.getString(cursor.getColumnIndex("room_style"));
                int s4 = cursor.getInt(cursor.getColumnIndex("room_image_num"));
                if (s1.equals(register_name)){
                    list_mItembeen.add(new itembean(s2,s3,s4-1));
                    int h = list_mItembeen.size();
                    if (h>0){
                        mAdater_room_item.notifyDataSetChanged();
                    }
                }
            }while (cursor.moveToNext());
        }
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_room:
                show_add_room_dialog();
                break;
            default:
                break;
        }
    }
}

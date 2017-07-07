package com.example.asus.windowsprotected.activityset;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.asus.windowsprotected.adaterset.window_add_adater;
import com.example.asus.windowsprotected.bean.itembean;
import com.example.asus.windowsprotected.bean.window_item_bean;
import com.example.asus.windowsprotected.R;
import com.example.asus.windowsprotected.otherset.DataBaseSQLHelp;
import com.example.asus.windowsprotected.otherset.SystemBarTintManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room_window_add extends AppCompatActivity implements View.OnClickListener {

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private TextView tv_scan;
    private TextView tv_title;
    private DataBaseSQLHelp mDataBaseSQLHelp_add_window;
    private Cursor cursor;
    private Toolbar mToolbar_window_add;
    private ContentValues values;
    private SQLiteDatabase db;
    private String user_name;
    private SharedPreferences mSharedPreferences_window_add_get_user;
    private EditText et_window_id;
    private TextView tv_window_add_sure_btn;
    //private TextView tv_test;
    private String title;
    private RecyclerView mRecyclerView;
    private window_add_adater mAdater_window_add;
    private List<window_item_bean> mData_window_item;
    public static final int REQUSET = 1;
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
        setContentView(R.layout.activity_room_window_add);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        initview();
        setToolbar();
        initRecycler();
        cursor_Data(user_name,title);
    }

    /**
     * 对Recyclerview设置
     */
    private void initRecycler() {
        mData_window_item = new ArrayList<>();
        mRecyclerView.setLayoutManager(new GridLayoutManager(Room_window_add.this,2));
        mAdater_window_add = new window_add_adater(Room_window_add.this,mData_window_item);
        mRecyclerView.setAdapter(mAdater_window_add);
        mAdater_window_add.setOnItemClickListener(new window_add_adater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(Room_window_add.this,WindowControlActivity.class);
                intent.putExtra("window_name",mData_window_item.get(position).getWindow_name());
                startActivity(intent);
            }
        });
    }
    /**
     * 控件初始化
     */
    private void initview() {
        mDataBaseSQLHelp_add_window = new DataBaseSQLHelp(this,"Room_Window_Add.db",null,1);
        values = new ContentValues();
        db = mDataBaseSQLHelp_add_window.getWritableDatabase();
        cursor = db.query("Room_Window_Add",null,null,null,null,null,null);
        mSharedPreferences_window_add_get_user = getSharedPreferences("user_name",MODE_PRIVATE);
        user_name = mSharedPreferences_window_add_get_user.getString("user","");
        tv_scan = (TextView) findViewById(R.id.tv_room_window_add_icon);
        tv_scan.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_room_window_add_title);
        tv_title.setText(title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_add_window);
        mToolbar_window_add = (Toolbar) findViewById(R.id.toolbar_room_window_add);

    }

    /**
     * 遍历数据库
     * @param
     */
    public void cursor_Data(String register_name,String room_name){
        cursor = db.query("Room_Window_Add",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                String s1 = cursor.getString(cursor.getColumnIndex("register_name"));
                String s2 = cursor.getString(cursor.getColumnIndex("room_name"));
                String s3 = cursor.getString(cursor.getColumnIndex("window_name"));
                String s4 = cursor.getString(cursor.getColumnIndex("window_image_num"));
                if (s1.equals(register_name)&&s2.equals(room_name)){
                    mData_window_item.add(new window_item_bean(s3,s4));
                    mAdater_window_add.notifyDataSetChanged();
                }
            }while (cursor.moveToNext());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_room_window_add_icon:
                Intent intent1 = new Intent();
                intent1.setClass(Room_window_add.this, MipcaActivityCapture.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent1, SCANNIN_GREQUEST_CODE);
                break;
            default:
                break;
        }
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
            mToolbar_window_add.setBackgroundResource(R.color.night_mode);
        }else {
            return;
        }
    }
    /**
     * 调用二维码扫描、获取传回的信息
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    LayoutInflater layoutInflater = LayoutInflater.from(Room_window_add.this);
                    View view = layoutInflater.inflate(R.layout.dialog_of_scan, null);
                    et_window_id = (EditText) view.findViewById(R.id.et_window_id);
                    tv_window_add_sure_btn = (TextView) view.findViewById(R.id.tv_window_add_sure_btn);
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    String h = bundle.getString("result");
                    final String g[] = h.split(" ");
                    Boolean window_exist = true;
                    if (g[0].equals("窗卫士")) {
                        cursor = db.query("Room_Window_Add",null,null,null,null,null,null);
                        if (cursor.moveToFirst()){
                            do{
                                String s1 = cursor.getString(cursor.getColumnIndex("register_name"));
                                String s2 = cursor.getString(cursor.getColumnIndex("window_name"));
                                String s3 = cursor.getString(cursor.getColumnIndex("room_name"));
                                if (g[1].equals(s2)&&user_name.equals(s1)&&s3.equals(title)){
                                    window_exist = false;
                                    break;
                                }
                            }while (cursor.moveToNext());
                        }
                        if (window_exist){
                            mData_window_item.add(new window_item_bean(g[1],g[3]));
                            mAdater_window_add.notifyDataSetChanged();
                            values.put("register_name",user_name);
                            values.put("room_name",title);
                            values.put("window_name",g[1]);
                            values.put("window_image_num",g[3]);
                            db.insert("Room_Window_Add",null,values);
                            values.clear();
                            Toast.makeText(Room_window_add.this,"窗户添加成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Room_window_add.this,"窗户不能重复添加",Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Room_window_add.this, "扫描的信息无效", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}

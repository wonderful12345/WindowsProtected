package com.example.asus.windowsprotected.activityset;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.asus.windowsprotected.R;
import com.example.asus.windowsprotected.otherset.DataBaseSQLHelp;
import com.example.asus.windowsprotected.otherset.SystemBarTintManager;
import com.example.asus.windowsprotected.otherset.WifiAdmin;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.acl.LastOwnerException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class WindowControlActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_title_name;
    private WifiAdmin wifiAdmin;

    private DataBaseSQLHelp mDataBaseSQLHelp_notification_text;
    private Cursor cursor;
    private ContentValues values;
    private SQLiteDatabase db;
    private String user_name;
    private SharedPreferences mSharedPreferences_get_user_name;
    private List<WifiConfiguration> configuratedList;
    private List<ScanResult> scanResultList;
    private static final String HOST = "192.168.0.110";
    //private static final String HOST = "192.168.1.132";
    private static final int PORT = 8888;
    private static final int CONNECTED = 1;
    private static final int RECEIVE = 0;
    //当前连接的wifi
    private WifiInfo currentWifiInfo;
    //WifiConfigutate的SSID的格式为："\"" + wifi名称 + "\""
    private final static String CONFIGURATESSID = "\"" + "jqx_wifi123" + "\"";
    //ScanResult的SSID的格式为："wifi名称"
    private final static String SCANSSID = "jqx_wifi123";
    private final static String MER_SSID = "\"" + "MERCURY_574C" + "\"";
    private final static String MER_SCANSSID = "MERCURY_574C";
    // 表示是否在可连接范围内
    private boolean canConnectable = false;
    // 表示wifi是否已经配置过了
    private boolean isConfigurated = false;
    //判断是否连接上制定的wifi
    private boolean isConnected = false;
    private int networkId;
    private BroadcastReceiver wifiConnectReceiver;
    private LinearLayout mLinearLayout_background_change;
    private ImageButton btn_window_open;
    private ImageButton btn_window_close;
    private TextView tv_window_mode_style;
    private TextView tv_window_temperature;
    private TextView tv_window_humidity;
    private TextView tv_window_dew_point;
    private TextView tv_window_flour;
    private TextView tv_window_illumination_intensity;
    private ImageView iv_window_rain;
    private ImageView iv_air_alarm;
    private ImageView iv_fan_work;
    private ImageView iv_video_work;
    private ImageView iv_temperature_picture;
    private ImageView iv_light_out;
    private ImageView iv_mode_change;
    private TextView tv_dew_point_name;
    private TextView tv_flour_name;
    private Toolbar mToolbar_window_control;
    private LinearLayout linear_fan_work;
    private LinearLayout linear_video_work;
    private LinearLayout linear_light_out;
    private LinearLayout linear_mode_change;
    private TextView tv_humidity_name;
    private BufferedWriter mWriter=null;
    private BufferedReader mReader;
    private Socket socket;
    private String receive = null;
    private Boolean window_mode_style = false;
    private Boolean window_pervious_to_light = false;
    private Boolean window_out_fan = true;
    private Boolean window_control_open = false;
    private Boolean window_control_close = false;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private NotificationManager manager;
    int notification_id;
    public Handler handler = new Handler() {
        @Override
        // 当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CONNECTED:
                    Toast.makeText(WindowControlActivity.this,"connect",Toast.LENGTH_SHORT).show();
                    break;
                case RECEIVE:
                    String h = msg.obj.toString();
                    String temperature = h.substring(1,5);
                    String humidity = h.substring(6,10);
                    String dew_point = h.substring(11,15);
                    String flour = h.substring(15,18);
                    String illumination_intensity = h.substring(18,21);
                    String rain = h.substring(21,22);
                    String air = h.substring(22,23);
                    //读取温度
                    String temperature1 = temperature.substring(0,2);
                    String temperature2 = temperature.substring(2,3);
                    temperature = temperature1+"."+temperature2;
                    tv_window_temperature.setText(temperature);
                    //读取湿度
                    String humidity1 = humidity.substring(0,2);
                    String humidity2 = humidity.substring(2,4);
                    humidity = humidity1+"."+humidity2;
                    tv_window_humidity.setText(humidity);
                    //读取露点
                    String dew_point1 = dew_point.substring(0,2);
                    String dew_point2 = dew_point.substring(2,4);
                    dew_point = dew_point1+"."+dew_point2;
                    tv_window_dew_point.setText(dew_point);
                    //读取粉尘浓度012
                    String flour1 = flour.substring(0,1);
                    String flour2 = flour.substring(1,3);
                    flour = flour1+"."+flour2;
                    tv_window_flour.setText(flour);
                    //读取光照强度01
                    /*String illumination_intensity1 = illumination_intensity.substring(0,1);
                    String illumination_intensity2 = illumination_intensity.substring(1,2);
                    illumination_intensity = illumination_intensity1+"."+illumination_intensity2;*/
                    String hundreds_place = illumination_intensity.substring(0,1);
                    if (hundreds_place.equals("0")){
                        illumination_intensity = illumination_intensity.substring(1,3);
                        tv_window_illumination_intensity.setText("光照强度："+illumination_intensity);
                    }else {
                        tv_window_illumination_intensity.setText("光照强度："+illumination_intensity);
                    }
                    //读取雨滴操作
                    if (rain.equals("0")){
                        iv_window_rain.setBackgroundResource(R.drawable.rain);
                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String txt = "尊敬的用户您好，由于窗外雨量过大，窗户已为您自动关闭。待雨量减少将为您自动开启，您也可以通过app手动开启。";
                        sendNotification(txt);
                        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd- HH:mm:ss ");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String str = formatter.format(curDate);
                        String data = str.substring(0,10);
                        String k = str.substring(12,21);
                        String last_time = data+" "+k;
                        cursor = db.query("NOTIFICATION_TEXT",null,null,null,null,null,null);
                        values.put("register_name",user_name);
                        values.put("data_time",last_time);
                        values.put("notification_content",txt);
                        db.insert("NOTIFICATION_TEXT",null,values);
                        values.clear();

                    }else {
                        iv_window_rain.setBackgroundResource(R.drawable.sunny);
                    }
                    //气体操作
                    if (air.equals("1")){
                        iv_air_alarm.setImageResource(R.drawable.alarm_nore);
                    }else {
                        iv_air_alarm.setImageResource(R.drawable.alarm);
                        String txt1 = "尊敬的用户您好，检测到有毒气体的泄漏，请您及时采取措施";
                        sendNotification(txt1);
                        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd- HH:mm:ss ");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String str = formatter.format(curDate);
                        String data = str.substring(0,10);
                        String k = str.substring(12,21);
                        String last_time = data+" "+k;
                        cursor = db.query("NOTIFICATION_TEXT",null,null,null,null,null,null);
                        values.put("register_name",user_name);
                        values.put("data_time",last_time);
                        values.put("notification_content",txt1);
                        db.insert("NOTIFICATION_TEXT",null,values);
                        values.clear();
                    }
                    break;
            }
        }
    };
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
        setContentView(R.layout.activity_window_control);
        initview();
        setBackgroundmode();
        /**
         * 获取title的名称
         */
        Intent intent = getIntent();
        String window_name = intent.getStringExtra("window_name");
        tv_title_name.setText(window_name);
        /**
         * 与服务器建立连接
         */
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    if (socket==null){
                        socket = new Socket(HOST, PORT);
                        Connect();
                    }
                    ReceiveMsg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        /**
         * wifi初始化
         */
        wifiAdmin = new WifiAdmin(WindowControlActivity.this);
        wifiConnectReceiver = new WifiConnectReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        WindowControlActivity.this.registerReceiver(wifiConnectReceiver, filter);
    }

    private void setBackgroundmode() {
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd- HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String hour = str.substring(12,14);
        String minute = str.substring(15,17);
        int hour1 = Integer.valueOf(hour);
        int minute1 = Integer.valueOf(minute);
        if ((hour1>18)||(hour1==18&&minute1>=30)||(hour1>=0&&hour1<=6)){
            mLinearLayout_background_change.setBackgroundResource(R.drawable.night_test);
            mToolbar_window_control.setBackgroundResource(R.color.night_mode);
            tv_window_temperature.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_window_mode_style.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_window_humidity.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_window_dew_point.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_window_flour.setTextColor(getResources().getColor(R.color.colorWhite));
            iv_temperature_picture.setBackgroundResource(R.drawable.temperature_picture_white);
            tv_humidity_name.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_dew_point_name.setTextColor(getResources().getColor(R.color.colorWhite));
            tv_flour_name.setTextColor(getResources().getColor(R.color.colorWhite));
        }else {
            mLinearLayout_background_change.setBackgroundResource(R.color.colorgray);
        }

    }

    /**
     * 初始化布局控件
     */
    private void initview() {
        iv_temperature_picture = (ImageView) findViewById(R.id.imageView_temperature_picture);
        tv_humidity_name = (TextView) findViewById(R.id.tv_humidity_name);
        tv_dew_point_name = (TextView) findViewById(R.id.tv_dew_point_name);
        tv_flour_name = (TextView) findViewById(R.id.tv_flour_name);
        mDataBaseSQLHelp_notification_text = new DataBaseSQLHelp(this,"NOTIFICATION_TEXT.db",null,1);
        values = new ContentValues();
        db = mDataBaseSQLHelp_notification_text.getWritableDatabase();
        cursor = db.query("NOTIFICATION_TEXT",null,null,null,null,null,null);
        mSharedPreferences_get_user_name = getSharedPreferences("user_name",MODE_PRIVATE);
        user_name = mSharedPreferences_get_user_name.getString("user","");
        Log.d("lll",user_name);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mSharedPreferences = getSharedPreferences("socket_string",MODE_PRIVATE);
        mEditor = getSharedPreferences("socket_string",MODE_PRIVATE).edit();
        mEditor.putString("first","11111");
        mEditor.commit();
        mToolbar_window_control = (Toolbar) findViewById(R.id.toolbar_window_control);
        tv_title_name = (TextView) findViewById(R.id.tv_window_control_title);
        linear_video_work = (LinearLayout) findViewById(R.id.linear_video_work);
        linear_video_work.setOnClickListener(this);
        mLinearLayout_background_change = (LinearLayout) findViewById(R.id.linear_window_control_background_change);
        tv_window_mode_style = (TextView) findViewById(R.id.tv_window_mode_style);
        linear_light_out = (LinearLayout) findViewById(R.id.linear_light_out);
        linear_light_out.setOnClickListener(this);
        linear_mode_change = (LinearLayout) findViewById(R.id.linear_mode_change);
        linear_mode_change.setOnClickListener(this);
        tv_window_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_window_humidity = (TextView) findViewById(R.id.tv_window_humidity);
        tv_window_dew_point = (TextView) findViewById(R.id.tv_window_dew_point);
        tv_window_flour = (TextView) findViewById(R.id.tv_window_flour);
        tv_window_illumination_intensity = (TextView) findViewById(R.id.tv_window_illumination_intensity);
        iv_window_rain = (ImageView) findViewById(R.id.iv_window_rain);
        linear_fan_work = (LinearLayout) findViewById(R.id.linear_fan_work);
        linear_fan_work.setOnClickListener(this);
        iv_air_alarm = (ImageView) findViewById(R.id.iv_air_alarm);
        iv_fan_work = (ImageView) findViewById(R.id.iv_fan_work);
        iv_light_out = (ImageView) findViewById(R.id.iv_light_out);
        iv_video_work = (ImageView) findViewById(R.id.iv_video_work);
        iv_mode_change = (ImageView) findViewById(R.id.iv_mode_change);
        btn_window_open = (ImageButton) findViewById(R.id.btn_window_open);
        btn_window_close = (ImageButton) findViewById(R.id.btn_window_close);
        btn_window_open.setOnClickListener(this);
        btn_window_close.setOnClickListener(this);
        unclickable();
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
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_window_open:
              /* String window_open = "11";
                ReceiverListener(window_open);*/
                /*String window_open = mSharedPreferences.getString("first","");
                String n2 = window_open.substring(2,5);*/
                if (window_control_open){
                    btn_window_close.setClickable(true);
                    //window_open = "11"+n2;
                    /*if (window_mode_style){
                        String window_open = "11";
                        ReceiverListener(window_open);
                    }else{
                        String window_open = "10";
                        ReceiverListener(window_open);
                    }*/
                    String window_open = "01";
                    ReceiverListener(window_open);
                    btn_window_open.setBackgroundResource(R.drawable.turn_right_cancle);
                    window_control_open = false;

                    /*mEditor.putString("first",window_open);
                    mEditor.commit();*/
                }else {
                    btn_window_close.setClickable(false);
                    //window_open = "10"+n2;
                    String window_open = "21";
                    btn_window_open.setBackgroundResource(R.drawable.turn_right);
                    window_control_open = true;
                    ReceiverListener(window_open);
                    /*mEditor.putString("first",window_open);
                    mEditor.commit();*/
                }
                break;
            case R.id.btn_window_close:
                /*String window_close = mSharedPreferences.getString("first","");
                String p = window_close.substring(2,5);*/
                if (window_control_close){
                    //window_close = "11"+p;
                    String window_close = "01";
                    btn_window_open.setClickable(true);
                    btn_window_close.setBackgroundResource(R.drawable.turn_left_cancle);
                    window_control_close = false;
                    ReceiverListener(window_close);
                    /*mEditor.putString("first",window_close);
                    mEditor.commit();*/
                }else {
                    //window_close = "01"+p;
                    String window_close = "11";
                    btn_window_open.setClickable(false);
                    btn_window_close.setBackgroundResource(R.drawable.turn_left);
                    window_control_close = true;
                    ReceiverListener(window_close);
                    mEditor.putString("first",window_close);
                    mEditor.commit();
                }
                /*String window_close = "21";
                ReceiverListener(window_close);*/

                break;
            case R.id.linear_fan_work:
                /*if (window_mode_style){
                    String fan_work = "31";
                    ReceiverListener(fan_work);
                }else {
                    String fan_work = "30";
                    ReceiverListener(fan_work);
                }*/
                /*String out_fan = mSharedPreferences.getString("first","");
                String q1 = out_fan.substring(0,2);
                String q2 = out_fan.substring(3,5);*/
                if (window_out_fan){
                    iv_fan_work.setBackgroundResource(R.drawable.fan_stop);
                    //String last = q1+"1"+q2;
                    if (window_mode_style){
                        String last = "41";
                        ReceiverListener(last);
                    }else {
                        String last = "40";
                        ReceiverListener(last);
                    }

                   /* mEditor.putString("first",last);
                    mEditor.commit();*/
                    window_out_fan = false;
                }else {
                    iv_fan_work.setBackgroundResource(R.drawable.fan_work);
                    //String last2 = q1+"0"+q2;
                    //ReceiverListener(last2);

                    if (window_mode_style){
                        String last = "31";
                        ReceiverListener(last);
                    }else {
                        String last = "30";
                        ReceiverListener(last);
                    }
                    window_out_fan = true;
                    //mEditor.putString("first",last2);
                    //mEditor.commit();
                }
                break;
            case R.id.linear_light_out:
                /*String previous_to_light = mSharedPreferences.getString("first","");
                String r1 = previous_to_light.substring(0,3);
                String r2 = previous_to_light.substring(4,5);*/
                if (window_pervious_to_light){
                    iv_light_out.setBackgroundResource(R.drawable.light_in);
                    /*String last3 = r1+"1"+r2;*/
                    if (window_mode_style){
                        String last4 = "61";
                        ReceiverListener(last4);
                    }else {
                        String last4 = "60";
                        ReceiverListener(last4);
                    }

                    //ReceiverListener(last3);
                    /*mEditor.putString("first",last3);
                    mEditor.commit();*/
                    window_pervious_to_light = false;
                }else {
                    iv_light_out.setBackgroundResource(R.drawable.light_out);

                    //String last4 = r1+"0"+r2;

                    if (window_mode_style){
                        String last3 = "51";
                        ReceiverListener(last3);
                    }else {
                        String last3 = "50";
                        ReceiverListener(last3);
                    }
                    //ReceiverListener(last4);
                    window_pervious_to_light = true;
                    //mEditor.putString("first",last4);
                    //mEditor.commit();
                }
                break;
            case R.id.linear_mode_change:
                //01234
                /*String window_mode = mSharedPreferences.getString("first","");
                String t = window_mode.substring(0,4);*/
                if (window_mode_style){
                    tv_window_mode_style.setText("自动");
                    iv_mode_change.setBackgroundResource(R.drawable.mode_auto);
                    //String t2 = t+"0";
                    String t2 = "01";
                    ReceiverListener(t2);
                    /*mEditor.putString("first",t2);
                    mEditor.commit();*/
                    window_mode_style = false;
                    unclickable();
                }else {
                    tv_window_mode_style.setText("手动");
                    iv_mode_change.setBackgroundResource(R.drawable.mode_merchan);
                    //String t3 = t+"1";
                    String t3 = "00";
                    ReceiverListener(t3);
                    /*mEditor.putString("first",t3);
                    mEditor.commit();*/
                    window_mode_style = true;
                    clickable();
                }
                break;
            case R.id.linear_video_work:
                iv_video_work.setBackgroundResource(R.drawable.video_work);
                // TODO Auto-generated method stub
                // configuratedList = wifiAdmin.getConfiguratedList();
                configuratedList = wifiAdmin.getConfiguratedList();
                scanResultList = wifiAdmin.getScanResultList();
                if (scanResultList != null) {
                    for (int i = 0; i < scanResultList.size(); i++) {
                        if (scanResultList.get(i).SSID.equals(SCANSSID)) {
                            canConnectable = true;
                            break;
                        }
                    }
                    if (canConnectable) {
                        if (configuratedList != null) {
                            for (int j = 0; j < configuratedList.size(); j++) {
                                if (configuratedList.get(j).SSID
                                        .equals(CONFIGURATESSID)) {
                                    isConfigurated = true;
                                    networkId = configuratedList.get(j).networkId;
                                    break;
                                }
                            }
                            if (isConfigurated) {
                                wifiAdmin.connectWifi(networkId);
                                while (!isConnected) {
                                    break;
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /**
                 * 实现调用视频的APP
                 */
                doStartApplicationWithPackageName("com.toydemo.main");
                break;
            default:
                break;
        }
    }


    /**
     * 用于启动其他的APP软件
     * @param packagename
     */
    private void doStartApplicationWithPackageName(String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager().queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            startActivity(intent);
        }
    }

    /**
     * WifiConnectReceiver
     */
    class WifiConnectReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo notewokInfo = manager.getActiveNetworkInfo();
                if (notewokInfo != null) {
                    currentWifiInfo = wifiAdmin.getCurrentWifiInfo();
                    if (currentWifiInfo.getSSID().equals(CONFIGURATESSID)) {
                        isConnected = true;
                    }
                    if (currentWifiInfo.getSSID().equals(MER_SSID)){
                        isConnected = true;
                    }
                }
            }
        }
    }

    /**
     * 重新回到该界面时调用
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        iv_video_work.setBackgroundResource(R.drawable.video_stop);
        configuratedList = wifiAdmin.getConfiguratedList();
        scanResultList = wifiAdmin.getScanResultList();
        if (scanResultList != null) {
            for (int i = 0; i < scanResultList.size(); i++) {
                if (scanResultList.get(i).SSID.equals(MER_SCANSSID)) {
                    canConnectable = true;
                    break;
                }
            }
            if (canConnectable) {
                if (configuratedList != null) {
                    for (int j = 0; j < configuratedList.size(); j++) {
                        if (configuratedList.get(j).SSID
                                .equals(MER_SSID)) {
                            isConfigurated = true;
                            networkId = configuratedList.get(j).networkId;
                            break;
                        }
                    }
                    if (isConfigurated) {
                        wifiAdmin.connectWifi(networkId);
                        while (!isConnected) {
                            break;
                        }
                    }
                }
            }
        }
        try {
            socket.close();
            mReader.close();
            mWriter.close();
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    if (socket==null){
                        socket = new Socket(HOST, PORT);
                        Connect();
                    }
                    ReceiveMsg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        WindowControlActivity.this.unregisterReceiver(wifiConnectReceiver);
    }

    /**
     * socket 通信部分代码
     * @param sendtext
     * @throws IOException
     */
    private void SendMsg(String sendtext) throws IOException {
        if (socket!=null){
            mWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            mWriter.write(sendtext.replace("\n", "") +"\n");
            mWriter.flush();
        }else {
            Toast.makeText(WindowControlActivity.this,"无法连接上服务器",Toast.LENGTH_LONG).show();
        }

    }
    private void ReceiveMsg() throws IOException {
        mReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true){
            if (mReader.ready()){
                while ((receive = mReader.readLine())!=null){
                    //txt += receive+"\n";
                    Message msg = handler.obtainMessage();
                    msg.obj = receive;
                    msg.what = RECEIVE;
                    handler.sendMessage(msg);
                }
            }
        }
    }
    private void Connect(){
        if(socket.isConnected()){
            Message message = new Message();
            message.what = CONNECTED;
            handler.sendMessage(message);
        }
    }
    private void ReceiverListener(final String string){
        new Thread() {
            @Override
            public void run() {
                String s=string;
                // 执行完毕后给handler发送一个空消息
                try {
                    Looper.prepare();
                    SendMsg(s);
                    Log.d("tag","tag");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 发送APP通知
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String txt) {
        Intent intent = new Intent(WindowControlActivity.this,NotificationActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(WindowControlActivity.this)
                .setSmallIcon(R.drawable.red_icon)
                .setTicker("【窗卫士】")//手机状态栏的提示
                .setWhen(System.currentTimeMillis())
                .setContentTitle("【窗卫士】")
                .setContentText(txt)
                .setContentIntent(pendingIntent)//设置点击后的意图
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.beep))
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_VIBRATE);
        Notification notification = builder.build();//4.1以上
        manager.notify(notification_id,notification);
    }
    /**
     * 按钮设置成不能点击
     */
    private void unclickable(){
        btn_window_close.setClickable(false);
        btn_window_open.setClickable(false);
        linear_fan_work.setClickable(false);
        linear_light_out.setClickable(false);
    }
    /**
     * 按钮设置成可以点击
     */
    private void clickable(){
        btn_window_close.setClickable(true);
        btn_window_open.setClickable(true);
        linear_fan_work.setClickable(true);
        linear_light_out.setClickable(true);
    }
}

package com.example.asus.windowsprotected.otherset;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ASUS on 2017/2/21.
 */

public class DataBaseSQLHelp extends SQLiteOpenHelper {

    public static final String CREATE_Register = "create table register("
    +"id integer primary key autoincrement,"
    +"register_name text,"
    +"register_password text)";

    public static final String CREATE_Room_Add = "create table room_add("
            +"id integer primary key autoincrement,"
            +"register_name text,"
            +"room_name text,"
            +"room_style text,"
            +"room_image_num integer)";

    public static final String CREATE_Room_Window_Add = "create table room_window_add("
            +"id integer primary key autoincrement,"
            +"register_name text,"
            +"room_name text,"
            +"window_name text,"
            +"window_image_num text)";

    public static final String CREATE_NOTIFICATION_TEXT = "create table notification_text("
            +"id integer primary key autoincrement,"
            +"register_name text,"
            +"data_time text,"
            +"notification_content text)";

    private Context mContext;
    public DataBaseSQLHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Register);
        db.execSQL(CREATE_Room_Add);
        db.execSQL(CREATE_Room_Window_Add);
        db.execSQL(CREATE_NOTIFICATION_TEXT);
        Log.d("MainActivity","Creat Success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

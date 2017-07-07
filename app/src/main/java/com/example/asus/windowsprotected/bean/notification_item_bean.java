package com.example.asus.windowsprotected.bean;

/**
 * Created by ASUS on 2017/5/21.
 */

public class notification_item_bean {
    private String data_time;
    private String notification_content;
    public notification_item_bean(String data_time,String notification_content){
        this.data_time = data_time;
        this.notification_content = notification_content;
    }

    public String getData_time() {
        return data_time;
    }

    public void setData_time(String data_time) {
        this.data_time = data_time;
    }

    public String getNotification_content() {
        return notification_content;
    }

    public void setNotification_content(String notification_content) {
        this.notification_content = notification_content;
    }
}

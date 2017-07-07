package com.example.asus.windowsprotected.bean;

/**
 * Created by ASUS on 2017/5/6.
 */

public class window_item_bean {
    private String window_num;
    private String window_name;

    public window_item_bean(String window_name,String window_num){
        this.window_name = window_name;
        this.window_num = window_num;
    }

    public String getWindow_num() {
        return window_num;
    }

    public void setWindow_name(String window_name) {
        this.window_name = window_name;
    }

    public String getWindow_name() {
        return window_name;
    }

    public void setWindow_num(String window_num) {
        this.window_num = window_num;
    }
}

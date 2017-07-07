package com.example.asus.windowsprotected.bean;

/**
 * Created by ASUS on 2017/5/2.
 */

public class itembean {
    private String room_name;
    private String room_num;
    private int room_style;

    public itembean(String name,String num,int style_num){
        this.room_name = name;
        this.room_num = num;
        this.room_style = style_num;
    }

    public int getRoom_style() {
        return room_style;
    }

    public void setRoom_style(int room_style) {
        this.room_style = room_style;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getRoom_num() {
        return room_num;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }
}

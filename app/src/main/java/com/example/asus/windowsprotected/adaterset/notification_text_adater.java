package com.example.asus.windowsprotected.adaterset;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.windowsprotected.R;
import com.example.asus.windowsprotected.bean.notification_item_bean;

import java.util.List;

/**
 * Created by ASUS on 2017/5/21.
 */

public class notification_text_adater extends RecyclerView.Adapter<notification_text_adater.Notification_viewHolder>{

    private List<notification_item_bean> mData_notification;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public notification_text_adater(Context context,List<notification_item_bean> list){
        this.mContext = context;
        this.mData_notification = list;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public Notification_viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_notification_item,parent,false);
        Notification_viewHolder viewHolder = new Notification_viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Notification_viewHolder holder, int position) {
        holder.tv_data.setText(mData_notification.get(position).getData_time());
        holder.notification_text.setText(mData_notification.get(position).getNotification_content());
    }

    @Override
    public int getItemCount() {
        return mData_notification.size();
    }

    class Notification_viewHolder extends RecyclerView.ViewHolder{
        public TextView tv_data;
        public TextView notification_text;
        public Notification_viewHolder(View itemView) {
            super(itemView);
            tv_data = (TextView) itemView.findViewById(R.id.tv_notification_data);
            notification_text = (TextView) itemView.findViewById(R.id.tv_notification_content);
        }
    }
}

package com.example.asus.windowsprotected.adaterset;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.windowsprotected.R;
import com.example.asus.windowsprotected.bean.window_item_bean;

import java.util.List;

/**
 * Created by ASUS on 2017/5/6.
 */

public class window_add_adater extends RecyclerView.Adapter<window_add_adater.Window_add_holder> {

    private LayoutInflater mLayoutInflater;
    private List<window_item_bean> mData_window_list;
    private Context mContext;
    public OnItemClickListener itemClickListener;


    public window_add_adater(Context context,List<window_item_bean> list){
        this.mContext = context;
        this.mData_window_list = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public Window_add_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_window_item,parent,false);
        Window_add_holder holder = new Window_add_holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Window_add_holder holder, int position) {
        holder.mTextView.setText(mData_window_list.get(position).getWindow_name());
        String h = mData_window_list.get(position).getWindow_num();
        int i = Integer.parseInt(h);
        switch (i){
            case 0:
                holder.mImageView.setImageResource(R.drawable.window_one);
                break;
            case 1:
                holder.mImageView.setImageResource(R.drawable.window_two);
                break;
            case 2:
                holder.mImageView.setImageResource(R.drawable.window_three);
                break;
            case 3:
                holder.mImageView.setImageResource(R.drawable.window_four);
                break;
            case 4:
                holder.mImageView.setImageResource(R.drawable.window_five);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData_window_list.size();
    }

    class Window_add_holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView mImageView;
        public TextView mTextView;
        public CardView mCardView_add_window;
        //public LinearLayout mLinearLayout;
        public Window_add_holder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_recycler_picture);
            mTextView = (TextView) itemView.findViewById(R.id.tv_recycler_window_name);
            mCardView_add_window = (CardView) itemView.findViewById(R.id.cv_window_recycler);
            mCardView_add_window.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v,getPosition());
            }
        }
    }
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}

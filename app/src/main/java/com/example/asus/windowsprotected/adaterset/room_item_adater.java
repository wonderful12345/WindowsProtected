package com.example.asus.windowsprotected.adaterset;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.asus.windowsprotected.R;
import com.example.asus.windowsprotected.bean.itembean;
import java.util.List;
/**
 * Created by ASUS on 2017/5/2.
 */
public class room_item_adater extends RecyclerView.Adapter<room_item_adater.Item_holder> {
    private List<itembean> mItembeen;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public OnItemClickListener itemClickListener;
    public room_item_adater(Context context,List<itembean> list){
        this.mContext = context;
        this.mItembeen = list;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public Item_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_main_item,parent,false);
        Item_holder item_holder = new Item_holder(view);
        return item_holder;
    }
    @Override
    public void onBindViewHolder(Item_holder holder, int position) {
        holder.tv_room_name.setText(mItembeen.get(position).getRoom_name());
        holder.tv_room_num.setText(mItembeen.get(position).getRoom_num());
        int style_num = mItembeen.get(position).getRoom_style();
        switch (style_num){
            case 0:
                holder.iv_room_picture.setImageResource(R.drawable.bedroom);
                break;
            case 1:
                holder.iv_room_picture.setImageResource(R.drawable.cook_room);
                break;
            case 2:
                holder.iv_room_picture.setImageResource(R.drawable.keting);
                break;
            case 3:
                holder.iv_room_picture.setImageResource(R.drawable.workroom);
                break;
            case 4:
                holder.iv_room_picture.setImageResource(R.drawable.otherroom);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItembeen.size();
    }
    class Item_holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_room_name;
        public TextView tv_room_num;
        public ImageView iv_room_picture;
        public CardView mCardView;
        //public RelativeLayout mRelativeLayout;
        public Item_holder(View itemView) {
            super(itemView);
            tv_room_name = (TextView) itemView.findViewById(R.id.tv_room_style);
            tv_room_num = (TextView) itemView.findViewById(R.id.tv_room_num);
            iv_room_picture = (ImageView) itemView.findViewById(R.id.iv_recycler_item);
            //mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_item_recycler);
            //mRelativeLayout.setOnClickListener(this);
            mCardView = (CardView) itemView.findViewById(R.id.cv_item_recycler);
            mCardView.setOnClickListener(this);
        }
        // 通过接口回调来实现RecyclerView的点击事件
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

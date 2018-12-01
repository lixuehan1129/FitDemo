package com.example.fitdemo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fitdemo.R;

import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/11/27.
 */

public class ClassSelectAdapter extends RecyclerView.Adapter<ClassSelectAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Context mContext;
    private List<Class_Select> mDataSet;


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView itr, coach, time;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.class_select_item_iv);
            itr = (TextView) itemView.findViewById(R.id.class_select_item_itr);
            coach = (TextView) itemView.findViewById(R.id.class_select_item_coach);
            time = (TextView) itemView.findViewById(R.id.class_select_item_time);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.class_select_item_relative);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.class_select_item_l);
        }
    }

    @Override
    public ClassSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_select_item, parent, false);
        mContext = parent.getContext();
        final RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    public ClassSelectAdapter(List<Class_Select> data) {
        mDataSet = data;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ClassSelectAdapter.ViewHolder holder, int position) {
        Class_Select class_select = mDataSet.get(position);
        System.out.println(class_select.getCheck());
        if(class_select.getCheck() == 1){
            holder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlue_W));
        }else {
            holder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        if(class_select.getImage() != null){
            holder.imageView.setImageResource(class_select.getImage());
        }else {
            holder.imageView.setImageResource(R.mipmap.ic_run1);
        }
        holder.itr.setText(class_select.getItr());
        holder.coach.setText(class_select.getCoach());
        holder.time.setText(class_select.getTime());

        // String url = class_video.getItem_image();
//        if(url!=null){
////          holder.item_text.setImageBitmap(DealBitmap.centerSquareScaleBitmap(url));
//            Glide.with(mcontext)
//                    .load(url)
//                    .asBitmap()  //不可加载动图
//                    .dontAnimate()//取消淡入淡出动画
//                    .thumbnail(0.1f) //先加载十分之一作为缩略图
//                    .into(holder.class_video_iv);
//        }
        //判断是否设置了监听器
        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView, position); // 2
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public class Class_Select {
        private String itr,coach,time;
        private Integer image;
        private int check;

        public int getCheck() {
            return check;
        }

        public void setCheck(int check) {
            this.check = check;
        }

        public String getItr() {
            return itr;
        }

        public void setItr(String itr) {
            this.itr = itr;
        }

        public String getCoach() {
            return coach;
        }

        public void setCoach(String coach) {
            this.coach = coach;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Integer getImage() {
            return image;
        }

        public void setImage(Integer image) {
            this.image = image;
        }

        public Class_Select(String itr, String coach, String time, Integer image, int check) {
            this.itr = itr;
            this.coach = coach;
            this.time = time;
            this.image = image;
            this.check = check;
        }
    }
}
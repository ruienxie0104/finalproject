package com.example.bottomnavigationviewwithfragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    private List<User> mUserList;
    private Context mContext;

    public interface OnItemClickListener {
        void onClick(int position);
    }
    public interface onItemLongClickListener {
        void onItemLongClick(View view,int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private onItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public UserListAdapter(List<User> list, Context context) {
        this.mUserList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_useritem_room.setText("教室 : "+mUserList.get(position).getRoom());
        holder.txt_useritem_device.setText("設備 : "+mUserList.get(position).getDevice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(holder.getLayoutPosition());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(view,holder.getLayoutPosition());
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_useritem_room;
        TextView txt_useritem_device;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_useritem_room = itemView.findViewById(R.id.txt_useritem_room);
            txt_useritem_device = itemView.findViewById(R.id.txt_useritem_device);
        }
    }
}

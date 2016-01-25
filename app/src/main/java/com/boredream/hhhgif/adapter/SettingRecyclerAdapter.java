package com.boredream.hhhgif.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.entity.MoreItem;

import java.util.List;

public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.ViewHolder> {

    private List<MoreItem> datas;
    private OnItemClickListener mOnItemClickListener;

    public SettingRecyclerAdapter(List<MoreItem> datas, OnItemClickListener listener) {
        this.datas = datas;
        mOnItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_left;
        public TextView tv_mid;
        public TextView tv_right;

        public ViewHolder(final View itemView) {
            super(itemView);

            iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
            tv_mid = (TextView) itemView.findViewById(R.id.tv_mid);
            tv_right = (TextView) itemView.findViewById(R.id.tv_right);
        }
    }

    @Override
    public SettingRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SettingRecyclerAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("RecyclerAdapter", "position = " + position);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(null, view, position, -1);
                }
            }
        });
        MoreItem data = datas.get(position);

        holder.iv_left.setImageResource(data.leftImgRes);
        if(!TextUtils.isEmpty(data.rightText)) {
            holder.tv_right.setVisibility(View.VISIBLE);
            holder.tv_right.setText(data.rightText);
        } else {
            holder.tv_right.setVisibility(View.GONE);
        }
        holder.tv_mid.setText(data.midText);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}

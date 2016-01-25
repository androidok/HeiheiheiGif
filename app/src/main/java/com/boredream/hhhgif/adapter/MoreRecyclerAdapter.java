package com.boredream.hhhgif.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.activity.LoginActivity;
import com.boredream.hhhgif.entity.MoreItem;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.SimpleGlideDecorator;
import com.bumptech.glide.Glide;

import java.util.List;

public class MoreRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_LIST = 1;

    private User user;
    private List<MoreItem> datas;
    private OnItemClickListener mOnItemClickListener;

    public MoreRecyclerAdapter(List<MoreItem> datas, OnItemClickListener listener) {
        this.datas = datas;
        mOnItemClickListener = listener;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class ViewHolderUserHeader extends RecyclerView.ViewHolder {

        public View include_more_header;
        public ImageView iv_avatar;
        public TextView tv_name;
        // not login
        public View include_more_no_header;
        public Button btn_login;

        public ViewHolderUserHeader(final View itemView) {
            super(itemView);

            include_more_header = itemView.findViewById(R.id.include_more_header);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            include_more_no_header = itemView.findViewById(R.id.include_more_no_header);
            btn_login = (Button) itemView.findViewById(R.id.btn_login);
        }
    }

    public static class ViewHolderList extends RecyclerView.ViewHolder {

        public ImageView iv_left;
        public TextView tv_mid;

        public ViewHolderList(final View itemView) {
            super(itemView);

            iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
            tv_mid = (TextView) itemView.findViewById(R.id.tv_mid);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_header, parent, false);
            return new ViewHolderUserHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false);
            return new ViewHolderList(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);

        if (itemViewType == ITEM_VIEW_TYPE_HEADER) {
            ViewHolderUserHeader viewHolderHeader = (ViewHolderUserHeader) holder;
            if (user != null) {
                viewHolderHeader.include_more_header.setVisibility(View.VISIBLE);
                viewHolderHeader.include_more_no_header.setVisibility(View.GONE);

                SimpleGlideDecorator
                        .decorator(Glide.with(viewHolderHeader.itemView.getContext()).load(user.getAvatar()))
                        .placeholder(R.mipmap.ic_account_circle_grey600_24dp)
                        .into(viewHolderHeader.iv_avatar);
                viewHolderHeader.tv_name.setText(user.getUsername());

                viewHolderHeader.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("RecyclerAdapter", "position = " + position);
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(null, view, position, -1);
                        }
                    }
                });
            } else {
                viewHolderHeader.include_more_header.setVisibility(View.GONE);
                viewHolderHeader.include_more_no_header.setVisibility(View.VISIBLE);

                viewHolderHeader.btn_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        view.getContext().startActivity(intent);
                    }
                });
            }
        } else {
            ViewHolderList viewHolderList = (ViewHolderList) holder;
            viewHolderList.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("RecyclerAdapter", "position = " + position);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(null, view, position, -1);
                    }
                }
            });
            MoreItem data = datas.get(position - 1);

            viewHolderList.iv_left.setImageResource(data.leftImgRes);
            viewHolderList.tv_mid.setText(data.midText);
        }

    }

    @Override
    public int getItemCount() {
        // header + 1
        return datas.size() + 1;
    }
}

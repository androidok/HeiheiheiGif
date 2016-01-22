package com.boredream.hhhgif.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.entity.MoreItem;
import com.boredream.hhhgif.entity.User;

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

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mOnItemClickListener = mListener;
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {

        public ImageView iv_avatar;
        public TextView tv_name;
        public TextView tv_phone;
        public TextView tv_community;

        private OnItemClickListener mListener;

        public ViewHolderHeader(final View itemView, OnItemClickListener listener) {
            super(itemView);

            mListener = listener;

            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            tv_community = (TextView) itemView.findViewById(R.id.tv_community);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    Log.i("RecyclerAdapter", "position = " + position);
                    if (mListener != null) {
                        mListener.onItemClick(null, view, position, -1);
                    }

                }
            });
        }
    }

    public static class ViewHolderList extends RecyclerView.ViewHolder {

        public ImageView iv_left;
        public TextView tv_mid;

        private OnItemClickListener mListener;

        public ViewHolderList(final View itemView, OnItemClickListener listener) {
            super(itemView);

            mListener = listener;

            iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
            tv_mid = (TextView) itemView.findViewById(R.id.tv_mid);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    Log.i("RecyclerAdapter", "position = " + position);
                    if (mListener != null) {
                        mListener.onItemClick(null, view, position, -1);
                    }

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.include_more_header, parent, false);
            return new ViewHolderHeader(v, mOnItemClickListener);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_more, parent, false);
            return new ViewHolderList(v, mOnItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);

        if (itemViewType == ITEM_VIEW_TYPE_HEADER) {
            ViewHolderHeader viewHolderHeader = (ViewHolderHeader) holder;
            if (user != null) {
//                ImageLoader.getInstance().displayImage(user.getPortraitPicUrl(),
//                        viewHolderHeader.iv_avatar,
//                        ImageOptHelper.getAvatarOptions());
//                viewHolderHeader.tv_name.setText(user.getName());
//                viewHolderHeader.tv_phone.setText(user.getMobile());
//                viewHolderHeader.tv_community.setText(block == null ? "" : block.getName());
            } else {

            }
        } else {
            ViewHolderList viewHolderList = (ViewHolderList) holder;
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

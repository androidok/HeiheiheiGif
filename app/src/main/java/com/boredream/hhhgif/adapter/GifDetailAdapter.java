package com.boredream.hhhgif.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.hhhgif.R;
import com.boredream.hhhgif.entity.Comment;
import com.boredream.hhhgif.entity.GifInfo;
import com.boredream.hhhgif.entity.User;
import com.boredream.hhhgif.net.SimpleGlideDecorator;
import com.bumptech.glide.Glide;

import java.util.List;

public class GifDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_LIST = 1;

    private GifInfo gifInfo;
    private List<Comment> datas;

    public GifDetailAdapter(List<Comment> datas) {
        this.datas = datas;
    }

    public void setGifInfo(GifInfo gifInfo) {
        this.gifInfo = gifInfo;
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {

        public ImageView iv_gif;
        public TextView tv_title;
        public TextView tv_comment_count;
        public TextView tv_fav_count;

        public ViewHolderHeader(final View itemView) {
            super(itemView);

            iv_gif = (ImageView) itemView.findViewById(R.id.iv_gif);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_comment_count = (TextView) itemView.findViewById(R.id.tv_comment_count);
            tv_fav_count = (TextView) itemView.findViewById(R.id.tv_fav_count);
        }
    }

    public static class ViewHolderList extends RecyclerView.ViewHolder {

        public ImageView iv_avatar;
        public TextView tv_username;
        public TextView tv_content;

        public ViewHolderList(final View itemView) {
            super(itemView);

            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_gif_detail, parent, false);
            return new ViewHolderHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new ViewHolderList(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);

        if (itemViewType == ITEM_VIEW_TYPE_HEADER) {
            ViewHolderHeader viewHolderHeader = (ViewHolderHeader) holder;

            viewHolderHeader.tv_title.setText(gifInfo.getTitle());
            viewHolderHeader.tv_comment_count.setText(gifInfo.getCommentCount() + "");
            viewHolderHeader.tv_fav_count.setText(gifInfo.getFavCount() + "");
            SimpleGlideDecorator
                    .decorator(Glide.with(viewHolderHeader.itemView.getContext()).load(gifInfo.getImgUrl()))
                    .into(viewHolderHeader.iv_gif);
        } else {
            ViewHolderList viewHolderList = (ViewHolderList) holder;
            Comment data = datas.get(position - 1);

            User user = data.getUser();
            SimpleGlideDecorator
                    .decorator(Glide.with(viewHolderList.itemView.getContext()).load(user.getAvatar()))
                    .placeholder(R.mipmap.ic_account_circle_grey600_24dp)
                    .into(viewHolderList.iv_avatar);
            viewHolderList.tv_username.setText(user.getUsername());
            viewHolderList.tv_content.setText(data.getContent());

        }

    }

    @Override
    public int getItemCount() {
        // header + 1
        return datas.size() + 1;
    }
}

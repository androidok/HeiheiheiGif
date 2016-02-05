package com.boredream.hhhgif.adapter;

import android.app.Activity;
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
import com.boredream.hhhgif.net.GlideUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class GifDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_LIST = 1;

    private Activity context;
    private GifInfo gifInfo;
    private List<Comment> datas;

    public GifDetailAdapter(Activity context, List<Comment> datas) {
        this.context = context;
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
            View v = LayoutInflater.from(context).inflate(R.layout.include_gif_detail, parent, false);
            return new ViewHolderHeader(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
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

            final ImageView iv_gif = viewHolderHeader.iv_gif;

            Glide.with(context)
                    .load(gifInfo.getImgUrl())
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .fitCenter()
                    .listener(new RequestListener<String, GifDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            float ivHeight = (float) resource.getIntrinsicHeight() / resource.getIntrinsicWidth() * iv_gif.getWidth();
                            iv_gif.getLayoutParams().height = (int) ivHeight;
                            return false;
                        }
                    })
                    .into(iv_gif);
        } else if(itemViewType == ITEM_VIEW_TYPE_LIST) {
            ViewHolderList viewHolderList = (ViewHolderList) holder;
            Comment data = datas.get(position);

            User user = data.getUser().parseToObject(User.class);
            GlideUtils
                    .decorator(Glide.with(context).load(user.getAvatar()))
                    .placeholder(R.mipmap.ic_account_circle_grey600_24dp)
                    .into(viewHolderList.iv_avatar);
            viewHolderList.tv_username.setText(user.getUsername());
            viewHolderList.tv_content.setText(data.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}

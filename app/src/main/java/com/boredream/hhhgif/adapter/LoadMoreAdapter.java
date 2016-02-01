package com.boredream.hhhgif.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boredream.hhhgif.R;

/**
 * 加载更多
 */
public class LoadMoreAdapter extends RecyclerView.Adapter {
    public static final int STATUS_NONE = 0;
    public static final int STATUS_HAVE_MORE = 1;
    public static final int STATUS_LOADED_ALL = 2;
    private int status = STATUS_NONE;

    private boolean isLoading = false;

    private OnLoadMoreListener mOnLoadMoreListener;
    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private int TYPE_FOOTER = 110119120;

    public LoadMoreAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter, OnLoadMoreListener onLoadMoreListener) {
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mOnLoadMoreListener = onLoadMoreListener;
        setScrollListener();
    }

    public RecyclerView.Adapter getSrcAdapter() {
        return mAdapter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        isLoading = false;
        this.status = status;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View loadMore = inflater.inflate(R.layout.footer_progress, parent, false);
            LoadMoreViewHolder holder = new LoadMoreViewHolder(loadMore);
            return holder;
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            handleFooter((LoadMoreViewHolder) holder);
        } else {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    private void handleFooter(final LoadMoreViewHolder holder) {
        // set footer full span
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize = 1;
                    if (mAdapter.getItemViewType(position) == TYPE_FOOTER) {
                        spanSize = manager.getSpanCount();
                    }
                    return spanSize;
                }
            });
        }

        // check status
        switch (status) {
            case 1:
                holder.itemView.setVisibility(View.VISIBLE);

                holder.tv_footer_progress.setVisibility(View.GONE);
                holder.pb_footer_progress.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.itemView.setVisibility(View.VISIBLE);

                holder.tv_footer_progress.setVisibility(View.VISIBLE);
                holder.pb_footer_progress.setVisibility(View.GONE);
                break;
            case 0:
            default:
                holder.itemView.setVisibility(View.GONE);
                break;
        }
    }

    private void setScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int pastVisibleItems = -1;
                    int visibleItemCount = staggeredGridLayoutManager.getChildCount();
                    int totalItemCount = staggeredGridLayoutManager.getItemCount();
                    int[] firstVisibleItems = null;
                    firstVisibleItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisibleItems = firstVisibleItems[0];
                    }

                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        triggerLoadMore();
                    }
                } else if (layoutManager instanceof LinearLayoutManager) {
                    // LinearLayoutManager and GridLayoutManager
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                        triggerLoadMore();
                    }
                }
            }
        });
    }

    private synchronized void triggerLoadMore() {
        // block duplicate
        if(isLoading) {
            return;
        }

        Log.i("DDD", "load more");

        isLoading = true;
        mOnLoadMoreListener.onLoadMore();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mAdapter.getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return mAdapter.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar pb_footer_progress;
        public TextView tv_footer_progress;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);

            pb_footer_progress = (ProgressBar) itemView.findViewById(R.id.pb_footer_progress);
            tv_footer_progress = (TextView) itemView.findViewById(R.id.tv_footer_progress);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}

package com.loonggg.refreshloadmorelib.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.loonggg.refreshloadmorelib.adapter.BaseRecyclerAdapter;

/**
 * Created by loonggg on 2017/6/27.
 */

public class SwipeRefreshLoadMoreLayout extends LinearLayout {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private BaseRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;

    public SwipeRefreshLoadMoreLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshLoadMoreLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshLoadMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        swipeRefreshLayout = new SwipeRefreshLayout(context);
        recyclerView = new RecyclerView(context);
        swipeRefreshLayout.addView(recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshListener != null)
                    onRefreshListener.onRefresh();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter
                        .getItemCount()) {
                    if (onLoadMoreListener != null)
                        onLoadMoreListener.onLoad();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });


        addView(swipeRefreshLayout);
    }

    public void setLoadMore(boolean flag) {
        adapter.setIsShowLoadMore(flag);
    }

    /**
     * 下拉刷新完毕
     */
    public void refreshFinish() {
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    /**
     * 开始加载更多
     */
    public void startLoadMore() {
        adapter.setMoreStatus(adapter.LOADING_MORE);
    }

    /**
     * 加载更多完毕
     */
    public void loadMoreFinish() {
        adapter.setMoreStatus(adapter.PULLUP_LOAD_MORE);
        adapter.notifyDataSetChanged();
    }

    /**
     * @param layoutManager
     */
    public void setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(BaseRecyclerAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseRecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                onClickLoadMoreListener.onClickLoad();
            }
        });
    }

    public void setEmptyView(int resId) {
        adapter.setIsShowLoadMore(false);
        adapter.setIsShowEmptyView(true);
        adapter.setEmptyView(resId);
    }

    public void setIsShowEmptyView(boolean flag) {
        adapter.setIsShowEmptyView(flag);
    }


    private OnRefreshListener onRefreshListener;

    /**
     * 下拉刷新监听
     */
    public interface OnRefreshListener {
        void onRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public interface OnLoadMoreListener {
        void onLoad();
    }

    private OnLoadMoreListener onLoadMoreListener;

    /**
     * 滑动到底部自动加载更多的监听
     *
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnClickLoadMoreListener {
        void onClickLoad();
    }

    /**
     * 点击加载更多的监听
     */
    private OnClickLoadMoreListener onClickLoadMoreListener;

    public void setOnClickLoadMoreListener(OnClickLoadMoreListener onClickLoadMoreListener) {
        this.onClickLoadMoreListener = onClickLoadMoreListener;
    }


}

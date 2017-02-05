package com.qingmu.footerrecyclerview.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qingmu.footerrecyclerview.R;
import com.qingmu.footerrecyclerview.callback.LoadMoreDataListener;
import com.qingmu.footerrecyclerview.module.TestModule;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/5.
 */

public class FooterRecyclerAdapter extends RecyclerView.Adapter {
    private final RecyclerView mRecyclerView;
    public ArrayList<TestModule> mItemDatas;
    private LoadMoreDataListener mLoadMoreDataListener;
    private boolean isLoading;
    private int lastVisibleItemPosition;
    private int totalItemCount;
    //预留的阈值
    private int visibleThreshold = 5;
    //itemViewTypePosition
    private int VIEW_ITEM = 101;
    private int VIEW_LOADMORE = 102;

    public FooterRecyclerAdapter(ArrayList<TestModule> itemDatas, RecyclerView recyclerView) {
        if (itemDatas == null || recyclerView == null) {
            throw new RuntimeException("构造参数不能为null");
        }
        mItemDatas = itemDatas;
        mRecyclerView = recyclerView;
        handlerLoadMore(recyclerView);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View testView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testview, parent, false);
            return new ViewHolder(testView);
        } else {
            View loadmoreView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_loadermore, parent, false);
            return new LoadMoreViewHolder(loadmoreView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            ViewHolder realHolder = (ViewHolder) holder;
            realHolder.idTvTestitem.setText(mItemDatas.get(position).msg);
        }
    }

    @Override
    public int getItemCount() {
        return mItemDatas == null ? 0 : mItemDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemDatas.get(position) != null ? VIEW_ITEM : VIEW_LOADMORE;
    }

    //在原有数据的基础上添加新的数据
    public void addDatas(ArrayList<TestModule> newDatas) {
        int size = mItemDatas.size();
        //因为之前的数据最后都为null
        mItemDatas.remove(size - 1);
        mItemDatas.addAll(size-1, newDatas);
        mItemDatas.add(null);
        notifyDataSetChanged();
    }

    //清空原有数据，重新刷新数据
    public void setNewDatas(ArrayList<TestModule> newDatas) {
        mItemDatas = newDatas;
        notifyDataSetChanged();
    }

    //为recyclerView添加加载更多的监听
    public void setLoadMoreDataListener(LoadMoreDataListener loadMoreDataListener) {
        mLoadMoreDataListener = loadMoreDataListener;
    }

    //处理加载更多
    private void handlerLoadMore(RecyclerView recyclerView) {
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            //mRecyclerView添加滑动事件监听
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    Log.d("test", "totalItemCount =" + totalItemCount + "-----" + "lastVisibleItemPosition =" + lastVisibleItemPosition);
                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                        //此时是刷新状态
                        if (mLoadMoreDataListener != null)
                            mLoadMoreDataListener.loadMoreData();
                        isLoading = true;
                    }
                }
            });
        }
    }
    public void setLoaded() {
        isLoading = false;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_tv_testitem)
        TextView idTvTestitem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_prob_loadmore)
        ProgressBar idProbLoadmore;
        @BindView(R.id.id_tv_loadmore)
        TextView idTvLoadmore;
        @BindView(R.id.id_ll_container_loadmore)
        LinearLayout idLlContainerLoadmore;

        LoadMoreViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

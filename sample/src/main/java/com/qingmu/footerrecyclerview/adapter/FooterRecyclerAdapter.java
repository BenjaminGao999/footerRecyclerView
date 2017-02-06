package com.qingmu.footerrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingmu.footerrecyclerview.R;
import com.qingmu.footerrecyclerview.callback.LoadMoreDataListener;
import com.qingmu.footerrecyclerview.module.ParentOfRecord;
import com.qingmu.footerrecyclerview.module.RecordData;
import com.qingmu.footerrecyclerview.utils.Util;
import com.qingmu.footerrecyclerview.viewHolder.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/5.
 */

public class FooterRecyclerAdapter extends RecyclerView.Adapter {
    private final RecyclerView mRecyclerView;
    private final Context mContext;
    private LoadMoreDataListener mLoadMoreDataListener;
    private boolean isLoading;
    private int lastVisibleItemPosition;
    private int totalItemCount;
    //预留的阈值
    private int visibleThreshold = 5;
    //itemViewType
    private int VIEW_FOOTER = 101;
    private int VIEW_TITLE = 102;
    private int VIEW_MISSION = 103;

    private View mLoadingView; //分页加载中view
    private View mLoadFailedView; //分页加载失败view
    private View mLoadEndView; //分页加载结束view
    private View mEmptyView; //首次预加载view
    private View mReloadView; //首次预加载失败、或无数据的view
    private RelativeLayout mFooterLayout;//footer view
    ArrayList<ParentOfRecord> mItemDatas = new ArrayList<>();//真实的数据

    public FooterRecyclerAdapter(Context context, ArrayList<ParentOfRecord> itemDatas, RecyclerView recyclerView) {
        mContext = context;
        if (itemDatas == null || recyclerView == null) {
            throw new RuntimeException("构造参数不能为null");
        }
        mItemDatas = itemDatas;
        mRecyclerView = recyclerView;
        handlerLoadMore(recyclerView);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_FOOTER) {

            if (mFooterLayout == null) {
                mFooterLayout = new RelativeLayout(mContext);
            }
            if (mFooterLayout == null) {
                mFooterLayout = new RelativeLayout(mContext);
            }
            return ViewHolder.create(mFooterLayout);
        } else if (viewType == VIEW_TITLE) {
            View titleView = LayoutInflater.from(mContext).inflate(R.layout.item_record_title, parent, false);
            return new TitleViewHolder(titleView);
        } else {
            View missionView = LayoutInflater.from(mContext).inflate(R.layout.item_record_mission, parent, false);
            return new MissionViewHolder(missionView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            String title = mItemDatas.get(position).title;
            titleViewHolder.idTvRecordDate.setText(title);
        }

        if (holder instanceof MissionViewHolder) {
            MissionViewHolder missionViewHolder = (MissionViewHolder) holder;
            RecordData.DataBeanX.DataBean bean = mItemDatas.get(position).bean;
            missionViewHolder.idTvRecordName.setText(bean.getMissionName());
            missionViewHolder.idTvRecordState.setText(bean.getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return mItemDatas == null ? 0 : mItemDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemDatas.get(position) == null) {
            return VIEW_FOOTER;
        } else if (mItemDatas.get(position).isTitle) {
            return VIEW_TITLE;
        } else {
            return VIEW_MISSION;
        }
    }


    //real data
    public void setNewDatas(ArrayList<ParentOfRecord> records) {
        records.add(null);
        mItemDatas = records;
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

    /**
     * 清空footer view
     */
    private void removeFooterView() {
        mFooterLayout.removeAllViews();
    }

    /**
     * 添加新的footer view
     *
     * @param footerView
     */
    private void addFooterView(View footerView) {
        if (footerView == null) {
            return;
        }

        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(mContext);
        }
        removeFooterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (mContext.getResources().getDisplayMetrics().density * 60));
        mFooterLayout.addView(footerView, params);
    }

    /**
     * 初始化加载中布局
     *
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        addFooterView(mLoadingView);
    }

    public void setLoadingView(int loadingId) {
        setLoadingView(Util.inflate(mContext, loadingId));
    }

    /**
     * 初始化全部加载完成布局
     *
     * @param loadEndView
     */
    public void setLoadEndView(View loadEndView) {
        mLoadEndView = loadEndView;
        addFooterView(mLoadEndView);
    }

    public void setLoadEndView(int loadEndId) {
        setLoadEndView(Util.inflate(mContext, loadEndId));
    }


    static class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_tv_record_date)
        TextView idTvRecordDate;

        TitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class MissionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_tv_record_name)
        TextView idTvRecordName;
        @BindView(R.id.id_tv_record_state)
        TextView idTvRecordState;

        MissionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

package com.qingmu.footerrecyclerview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.qingmu.footerrecyclerview.R;
import com.qingmu.footerrecyclerview.adapter.FooterRecyclerAdapter;
import com.qingmu.footerrecyclerview.callback.LoadMoreDataListener;
import com.qingmu.footerrecyclerview.callback.TestCallBack;
import com.qingmu.footerrecyclerview.module.TestModule;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FooterRecyclerActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_recyclerview_footer)
    RecyclerView idRecyclerviewFooter;
    @BindView(R.id.id_swiperefresh_footer)
    SwipeRefreshLayout idSwiperefreshFooter;
    @BindView(R.id.id_fl_container)
    FrameLayout idFlContainer;
    private int currentPage = 0;
    private int PAGE_COUNT = 18;
    private int TOTAL_COUNT = 54;
    private int mCurrentCount = 0;
    private FooterRecyclerAdapter mFooterRecyclerAdapter;
    Handler mHandler = new Handler();
    private ArrayList<TestModule> testModules;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footerrecycler);
        ButterKnife.bind(this);
        initListener();
        initAdapter();
        initData();
    }

    private void initData() {
        idSwiperefreshFooter.setRefreshing(true);
        getTestDatas(currentPage, new TestCallBack() {
            @Override
            public void onSuccess(ArrayList<TestModule> testModules) {
                idSwiperefreshFooter.setRefreshing(false);

                //根据第一次请求数据判断是否要携带加载更多的逻辑
                if (testModules.size() < PAGE_COUNT) {
                    mFooterRecyclerAdapter.setNewDatas(testModules);
                } else {
                    mFooterRecyclerAdapter.setLoadMoreDataListener(loadmoreListener);
                    testModules.add(null);//为了加上footerView
                    mFooterRecyclerAdapter.setNewDatas(testModules);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    //高度解耦。先搭建空台子，然后再演戏（加载数据）。
    private void initAdapter() {
        ArrayList<TestModule> testModules = new ArrayList<>();//为了方便后续操作，要求构造方法的参数不能为null
        idRecyclerviewFooter.setLayoutManager(new LinearLayoutManager(this));
        mFooterRecyclerAdapter = new FooterRecyclerAdapter(this, testModules, idRecyclerviewFooter);
        idRecyclerviewFooter.setAdapter(mFooterRecyclerAdapter);
        //设置加载更多的监听
//        mFooterRecyclerAdapter.setLoadMoreDataListener(loadmoreListener);
        //footerView添加loadMoreView
        mFooterRecyclerAdapter.setLoadingView(R.layout.view_loadermore);
    }

    private LoadMoreDataListener loadmoreListener = new LoadMoreDataListener() {
        @Override
        public void loadMoreData() {
            getTestDatas(++currentPage, new TestCallBack() {
                @Override
                public void onSuccess(ArrayList<TestModule> testModules) {
                    if (testModules.size() < PAGE_COUNT) {
                        mFooterRecyclerAdapter.setLoadEndView(R.layout.view_nomoredata);
                        //没有更多数据，标记下
                        mFooterRecyclerAdapter.setNoMoreData(true);
                    }
                    mFooterRecyclerAdapter.addDatas(testModules);
                    mFooterRecyclerAdapter.setLoaded();
                }

                @Override
                public void onFail(String msg) {

                }
            });
        }
    };

    private void initListener() {
        idSwiperefreshFooter.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        idSwiperefreshFooter.setRefreshing(false);
    }

    private void getTestDatas(int page, final TestCallBack testCallBack) {
        testModules = new ArrayList<>();
        int requestCount = PAGE_COUNT;
        if (page >= 3) {
            requestCount--;
        }
        if (!mFooterRecyclerAdapter.isNoMoreData) {
            for (int i = 0; i < requestCount; i++) {
                TestModule testModule = new TestModule("item" + page + i);
                testModules.add(testModule);
            }
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testCallBack.onSuccess(testModules);
            }
        }, 1000);
    }
}

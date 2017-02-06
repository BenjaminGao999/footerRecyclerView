package com.qingmu.footerrecyclerview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qingmu.footerrecyclerview.R;
import com.qingmu.footerrecyclerview.adapter.FooterRecyclerTestAdapter;
import com.qingmu.footerrecyclerview.callback.LoadMoreDataListener;
import com.qingmu.footerrecyclerview.callback.RecordCallBack;
import com.qingmu.footerrecyclerview.callback.TestCallBack;
import com.qingmu.footerrecyclerview.module.ParentOfRecord;
import com.qingmu.footerrecyclerview.module.PostJson;
import com.qingmu.footerrecyclerview.module.RecordData;
import com.qingmu.footerrecyclerview.module.TestModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FooterRecyclerTestActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_recyclerview_footer)
    RecyclerView idRecyclerviewFooter;
    @BindView(R.id.id_swiperefresh_footer)
    SwipeRefreshLayout idSwiperefreshFooter;
    @BindView(R.id.id_fl_container)
    FrameLayout idFlContainer;
    private int currentPage = 1;
    private int PAGE_COUNT = 18;
    private int TOTAL_COUNT = 54;
    private int mCurrentCount = 0;
    private FooterRecyclerTestAdapter mFooterRecyclerTestAdapter;
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
                    mFooterRecyclerTestAdapter.setTestNewDatas(testModules);
                } else {
                    mFooterRecyclerTestAdapter.setLoadMoreDataListener(loadmoreListener);
                    testModules.add(null);//为了加上footerView
                    mFooterRecyclerTestAdapter.setTestNewDatas(testModules);
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
        mFooterRecyclerTestAdapter = new FooterRecyclerTestAdapter(this, testModules, idRecyclerviewFooter);
        idRecyclerviewFooter.setAdapter(mFooterRecyclerTestAdapter);
        //设置加载更多的监听
//        mFooterRecyclerTestAdapter.setLoadMoreDataListener(loadmoreListener);
        //footerView添加loadMoreView
        mFooterRecyclerTestAdapter.setLoadingView(R.layout.view_loadermore);
    }

    private LoadMoreDataListener loadmoreListener = new LoadMoreDataListener() {
        @Override
        public void loadMoreData() {
            getTestDatas(++currentPage, new TestCallBack() {
                @Override
                public void onSuccess(ArrayList<TestModule> testModules) {
                    if (testModules.size() < PAGE_COUNT) {
                        mFooterRecyclerTestAdapter.setLoadEndView(R.layout.view_nomoredata);
                        //没有更多数据，标记下
                        mFooterRecyclerTestAdapter.setNoMoreData(true);
                    }
                    mFooterRecyclerTestAdapter.addDatas(testModules);
                    mFooterRecyclerTestAdapter.setLoaded();
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
        if (!mFooterRecyclerTestAdapter.isNoMoreData) {
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

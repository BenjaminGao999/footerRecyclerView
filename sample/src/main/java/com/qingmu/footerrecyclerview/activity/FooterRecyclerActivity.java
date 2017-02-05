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
    private int PAGE_COUNT = 8;
    private int TOTAL_COUNT = 24;
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
                mFooterRecyclerAdapter.setNewDatas(testModules);
                idSwiperefreshFooter.setRefreshing(false);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    //高度解耦。先搭建空台子，然后再演戏（加载数据）。
    private void initAdapter() {
        ArrayList<TestModule> testModules = new ArrayList<>();//为了方便后续操作，要求构造方法的参数不能为null
        mFooterRecyclerAdapter = new FooterRecyclerAdapter(testModules);
        idRecyclerviewFooter.setLayoutManager(new LinearLayoutManager(this));
        idRecyclerviewFooter.setAdapter(mFooterRecyclerAdapter);
    }


    private void initListener() {
        idSwiperefreshFooter.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        idSwiperefreshFooter.setRefreshing(false);
    }

    private void getTestDatas(int page, final TestCallBack testCallBack) {
        testModules = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++) {
            TestModule testModule = new TestModule("item" + page + i);
            testModules.add(testModule);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testCallBack.onSuccess(testModules);
            }
        }, 1000);
    }
}

package com.qingmu.footerrecyclerview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qingmu.footerrecyclerview.R;
import com.qingmu.footerrecyclerview.adapter.FooterRecyclerAdapter;
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

public class FooterRecyclerActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_recyclerview_footer)
    RecyclerView idRecyclerviewFooter;
    @BindView(R.id.id_swiperefresh_footer)
    SwipeRefreshLayout idSwiperefreshFooter;
    @BindView(R.id.id_fl_container)
    FrameLayout idFlContainer;
    private static int currentPage = 1;
    private static int PAGE_COUNT = 8;
    private FooterRecyclerAdapter mFooterRecyclerAdapter;
    Handler mHandler = new Handler();
    private String recordUrl = "http://192.168.3.35:8888/services/mission/searchMissionInstanceGroupByDay";
    private static int daysCount = 0;//任务总天数
    private ArrayList<ParentOfRecord> parentOfRecords = new ArrayList<>();
    private static int totalCount;

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
        //把footerView和currentPage设置为初始状态。
        currentPage = 1;
        mFooterRecyclerAdapter.setLoadingView(R.layout.view_loadermore);

        getDataFromNet(currentPage * PAGE_COUNT, new RecordCallBack() {
            @Override
            public void onSuccess(ArrayList<ParentOfRecord> records) {
                idSwiperefreshFooter.setRefreshing(false);

                //根据第一次请求数据判断是否要携带加载更多的逻辑
                if (records.size() < PAGE_COUNT) {
                    mFooterRecyclerAdapter.setNewDatas(records);
                } else {
                    mFooterRecyclerAdapter.setLoadMoreDataListener(loadmoreListener);
                    parentOfRecords.add(null);//为了加上footerView
                    mFooterRecyclerAdapter.setNewDatas(records);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    //高度解耦。先搭建空台子，然后再演戏（加载数据）。
    private void initAdapter() {
        ArrayList<ParentOfRecord> modules = new ArrayList<>();//为了方便后续操作，要求构造方法的参数不能为null
        idRecyclerviewFooter.setLayoutManager(new LinearLayoutManager(this));
        mFooterRecyclerAdapter = new FooterRecyclerAdapter(this, modules, idRecyclerviewFooter);
        idRecyclerviewFooter.setAdapter(mFooterRecyclerAdapter);
        //设置加载更多的监听
//        mFooterRecyclerAdapter.setLoadMoreDataListener(loadmoreListener);
        //footerView添加loadMoreView
        mFooterRecyclerAdapter.setLoadingView(R.layout.view_loadermore);
    }

    private LoadMoreDataListener loadmoreListener = new LoadMoreDataListener() {
        @Override
        public void loadMoreData() {

            getDataFromNet((++currentPage) * PAGE_COUNT, new RecordCallBack() {
                @Override
                public void onSuccess(final ArrayList<ParentOfRecord> records) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (records.size() - daysCount == totalCount) {
                                mFooterRecyclerAdapter.setLoadEndView(R.layout.view_nomoredata);
                                //没有更多数据，标记下
                                mFooterRecyclerAdapter.setNoMoreData(true);
                            }
                            mFooterRecyclerAdapter.setNewDatas(records);
                            mFooterRecyclerAdapter.setLoaded();

                        }


                    }, 1000);
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
//        idSwiperefreshFooter.setRefreshing(false);
        initData();
    }


    private void getDataFromNet(int requestCount, final RecordCallBack recordCallBack) {
        OkHttpClient client = new OkHttpClient.Builder().build();

        MediaType MEDIA_TYPE_JSON
                = MediaType.parse("application/json;charset=utf-8");
        final PostJson postJson = new PostJson(requestCount, 0, 111, "testName");
        String json = JSON.toJSONString(postJson);

        System.out.println("json = " + json);
        Request request = new Request.Builder()
                .url(recordUrl)
                .post(RequestBody.create(MEDIA_TYPE_JSON, json))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(FooterRecyclerActivity.this, "网络状态不佳", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String resString = response.body().string();
                    System.out.println("Record_Data = " + resString);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 1.1: 成功获取数据
                            handleResponse(resString, recordCallBack);
                        }
                    });
                } catch (IllegalStateException e) {
                    Toast.makeText(FooterRecyclerActivity.this, "异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //1.1.1加工数据
    private void handleResponse(String resString, RecordCallBack recordCallBack) {
        //1.1.1.1 将请求的json转化为module
        RecordData recordData = JSON.parseObject(resString, RecordData.class);
        totalCount = recordData.data.totalCount;
        //原始数据item_mission
        List<RecordData.DataBeanX.DataBean> data = recordData.data.data;
        //将原始数据进行转化
        ArrayList<ParentOfRecord> parentOfRecords = new ArrayList<>();
        String tempGtmCreated = "";
        daysCount = 0;
        for (int i = 0; i < data.size(); i++) {
            String gtmCreated = data.get(i).getGtmCreated();
            if (!TextUtils.equals(tempGtmCreated, gtmCreated)) {
                parentOfRecords.add(new ParentOfRecord(true, gtmCreated, null));
                tempGtmCreated = gtmCreated;
                daysCount++;
            }
            parentOfRecords.add(new ParentOfRecord(false, null, data.get(i)));
        }
        recordCallBack.onSuccess(parentOfRecords);
    }
}

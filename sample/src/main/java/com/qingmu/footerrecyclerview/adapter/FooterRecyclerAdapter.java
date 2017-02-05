package com.qingmu.footerrecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingmu.footerrecyclerview.R;
import com.qingmu.footerrecyclerview.module.TestModule;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/5.
 */

public class FooterRecyclerAdapter extends RecyclerView.Adapter {
    public ArrayList<TestModule> itemDatas;

    public FooterRecyclerAdapter(ArrayList<TestModule> itemDatas) {
        if (itemDatas == null) {
            throw new RuntimeException("构造参数不能为null");
        }
        this.itemDatas = itemDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testview, parent, false);
        return new ViewHolder(testView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder realHolder = (ViewHolder) holder;
        realHolder.idTvTestitem.setText(itemDatas.get(position).msg);
    }

    @Override
    public int getItemCount() {
        return itemDatas == null ? 0 : itemDatas.size();
    }

    //在原有数据的基础上添加新的数据
    public void addDatas(ArrayList<TestModule> newDatas) {
        int size = itemDatas.size();
        itemDatas.addAll(size, newDatas);
        notifyDataSetChanged();
    }

    //清空原有数据，重新刷新数据
    public void setNewDatas(ArrayList<TestModule> newDatas) {
        itemDatas = newDatas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_tv_testitem)
        TextView idTvTestitem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}

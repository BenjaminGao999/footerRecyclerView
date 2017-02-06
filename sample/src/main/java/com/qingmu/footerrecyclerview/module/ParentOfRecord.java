package com.qingmu.footerrecyclerview.module;

/**
 * Author:　Created by benjamin
 * DATE :  2017/1/24 17:37
 */

public class ParentOfRecord {
    public boolean isTitle;
    public String title;
    public RecordData.DataBeanX.DataBean bean;

    public ParentOfRecord(boolean isTitle, String title, RecordData.DataBeanX.DataBean bean) {
        this.isTitle = isTitle;
        this.title = title;
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "ParentOfRecord{" +
                "isTitle=" + isTitle +
                ", title='" + title + '\'' +
                ", bean=" + bean +
                '}';
    }
}

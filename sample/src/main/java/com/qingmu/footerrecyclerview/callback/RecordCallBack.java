package com.qingmu.footerrecyclerview.callback;

import com.qingmu.footerrecyclerview.module.ParentOfRecord;
import com.qingmu.footerrecyclerview.module.TestModule;

import java.util.ArrayList;

/**
 * Author:　Created by benjamin
 * DATE :  2017/2/6 14:26
 */
public interface RecordCallBack {
    void onSuccess(ArrayList<ParentOfRecord> records);

    void onFail(String msg);

}

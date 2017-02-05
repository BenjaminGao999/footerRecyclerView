package com.qingmu.footerrecyclerview.callback;

import com.qingmu.footerrecyclerview.module.TestModule;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/5.
 */

public interface TestCallBack {
    void onSuccess(ArrayList<TestModule> testModules);

    void onFail(String msg);

}

package com.shixin.dagger;

import javax.inject.Inject;

/**
 * Created by shixin on 2017/8/29 0029.
 */

public class Poetry {

    private String mPemo;

    // 用Inject标记构造函数,表示用它来注入到目标对象中去
    @Inject
    public Poetry() {
        mPemo = "生活就像海洋";
    }

    public String getPemo() {
        return mPemo;
    }

    public Poetry(String s) {
        mPemo = s;
    }
}

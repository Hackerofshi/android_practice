package com.shixin.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shixin on 2017/3/6 0006.
 * 类：compose操作符
 */

public class RxSchedulers {
    public static <T> Observable.Transformer<T, T> io_main() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

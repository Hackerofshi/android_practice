package com.shixin.task;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shixin on 2017/4/6 0006.
 */

public abstract class MultiAsyncTask<Param, Update, Result> {

    private static final int UPDATE_WHAT = 0xa1;
    private static final int RESULT_WHAT = 0xa2;

    /**
     * 默认并发大小
     */
    private static final int DEFAULT_POOL_SIZE = 5;

    public void setDefaultExecutorService(ExecutorService mExecutorService) {
        synchronized (MultiAsyncTask.class) {
            this.mExecutorService = mExecutorService;

        }
    }

    private ExecutorService mExecutorService;
    private static ExecutorService sExecutorService;


    public MultiAsyncTask() {
        this(getDefaultExecutor());

    }

    private static ExecutorService getDefaultExecutor() {
        if (sExecutorService == null) {
            synchronized (MultiAsyncTask.class) {
                sExecutorService = Executors.newFixedThreadPool(5);
            }
        }
        return sExecutorService;
    }


    public MultiAsyncTask(ExecutorService executorService) {
        mExecutorService = executorService;
    }

    /**
     * Handler的锁
     */
    private static Object HANDLER_LOCK = new Object();
    private static Handler sHandler;

    private Handler getDeafultPoster() {
        if (sHandler == null)
            synchronized (HANDLER_LOCK){
                sHandler = new Poster();
            }
        return sHandler;
    }


    /**
     * 执行方法
     *
     * @param params 请求的url参数
     */
    public void execute(Param... params) {
        mExecutorService.execute(new Tasker(params));
    }


    public abstract Result executorTask(Param... params);

    public void onPostUpdate(Update update) {
        Message message = getDeafultPoster().obtainMessage();
        message.what = UPDATE_WHAT;
        message.obj = new Messager<Param,Update,Result>(this,update,null);
        message.sendToTarget();

    }
    /**
     * 更新过程
     * @param update
     */
    protected void onUpdate(Update update) {

    }


    public void onPostResult(Result result) {
        Message message = getDeafultPoster().obtainMessage();
        message.what = RESULT_WHAT;
        message.obj = new Messager<Param,Update,Result>(this,null,result);
        message.sendToTarget();

    }


    /**
     * 返回结果
     * @param result
     */
    protected void onResult(Result result) {}


    private static class Messager<Param, Update, Result> {
        private final MultiAsyncTask<Param, Update, Result> asyncTask;
        private Update update;
        private Result result;

        public Messager(MultiAsyncTask<Param, Update, Result> asyncTask, Update update, Result result) {
            this.asyncTask = asyncTask;
            this.update = update;
            this.result = result;
        }

        public void onUpdate() {
            asyncTask.onUpdate(update);
        }


        public void onResult() {
            asyncTask.onResult(result);
        }
    }
    /**
     * <p>线程间通信使者</p>
     *
     */
    private static class Poster extends Handler {
        public Poster() {
            super(Looper.getMainLooper());
        }
        @Override
        public void handleMessage(android.os.Message msg) {
            Messager<?, ?, ?> messager = (Messager<?, ?, ?>)  msg.obj;
            if (msg.what == UPDATE_WHAT){
                messager.onUpdate();
            }

            if (msg.what == RESULT_WHAT){
                messager.onResult();
            }
        }
    }


    /**
     * <p>任务执行器</p>
     *
     */
    private class Tasker implements Runnable {

        private Param[] param;
        Tasker(Param... param) {
            this.param = param;
        }

        @Override
        public void run() {
            Result result = executorTask(param);
            onPostResult(result);
        }
    }
}

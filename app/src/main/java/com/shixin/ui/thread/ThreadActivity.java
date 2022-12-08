package com.shixin.ui.thread;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shixin.R;

import java.util.concurrent.CyclicBarrier;

public class ThreadActivity extends AppCompatActivity {
    //CyclicBarrier和CountDownLatch的区别
    //CountDownLatch的计数器只能使用一次，而CyclicBarrier的计数器可以使用reset()方法重置，
    // 可以使用多次，所以CyclicBarrier能够处理更为复杂的场景；
    //
    //CyclicBarrier还提供了一些其他有用的方法，比如getNumberWaiting()
    // 方法可以获得CyclicBarrier阻塞的线程数量，isBroken()方法用来了解阻塞的线程是否被中断；
    //
    //CountDownLatch允许一个或多个线程等待一组事件的产生，
    // 而CyclicBarrier用于等待其他线程运行到栅栏位置。



    // 自定义工作线程
    private static class Worker extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Worker(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            super.run();

            try {
                System.out.println(Thread.currentThread().getName() + "开始等待其他线程");

                //等待其他线程全部执行到此处
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "开始执行");
                // 工作线程开始处理，这里用Thread.sleep()来模拟业务处理
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "执行完毕");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Worker worker = new Worker(cyclicBarrier);
                worker.start();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Worker worker1 = new Worker(cyclicBarrier);
                worker1.start();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Worker worker2 = new Worker(cyclicBarrier);
                worker2.start();
            }
        }).start();
    }
}
package com.shixin.rxjava;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity {

    private Subscriber<String> subscriber;
    private Observable observable;
    private String tag = "------";
    private String path;
    private ImageView img1;
    private Button button;
    private Button button1;
    private CheckBox check;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //不能创建 多层文件夹  /naishi/mine  这样
        path = Environment.getExternalStorageDirectory().getPath() + "/img/logo.jpg";
        File file = new File(path);
        //System.out.println("-----" + path + "----" + file.length());
        init();
        initView();


    }

    private void initView() {
        img1 = (ImageView) findViewById(R.id.img1);
        button = (Button) findViewById(R.id.bu1);
        button1 = (Button) findViewById(R.id.bu2);
        tv1 = (TextView) findViewById(R.id.tv1);
        RxView.clicks(button).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();
                        init4();
                    }
                });

        check = (CheckBox) findViewById(R.id.checkbox);


        RxCompoundButton.checkedChanges(check).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                button.setEnabled( aBoolean );
                button.setBackgroundResource( aBoolean ? R.color.colorAccent : R.color.colorPrimary );
            }
        });

    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                observable.subscribe(subscriber);
                break;
            case R.id.bu1:
                init1();
                break;
            case R.id.bu2:
                init3();
                break;
            case R.id.bu3:
                init5();
                break;
        }

    }


    private void init() {
        subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d("-----", "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d("-----", "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("-----", "Error!");
            }
        };
        observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        //第二种实现的打印的方式
        String[] words = {"hello", "hi", "haha"};
        Observable observable1 = Observable.from(words);


        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.d(tag, s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.d(tag, "completed");
            }
        };

        /*// 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);*/


    }

    /**
     * 主线程中显示图片
     */
    private void init1() {

        final int drableRes = R.mipmap.ic_launcher;


        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drable = getTheme().getDrawable(drableRes);
                subscriber.onNext(drable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Drawable drawable) {
                img1.setImageDrawable(drawable);

            }
        });


    }



    private void init2() {
        Observable.just(path) // 输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) { // 参数类型 String
                        System.out.println("---sd-----------" + filePath);
                        return createBitmap(filePath); // 返回类型 Bitmap
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) { // 参数类型 Bitmap
                        System.out.println("Hello--------------" + bitmap.getWidth() + Thread.currentThread().getId());
                        img1.setImageBitmap(bitmap);
                    }
                });
    }

    private Bitmap getBitmapFromPath(String filePath) {

        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }

    /**
     * 创建图片 将图片文件转换成bitmap并且可以压缩
     *
     * @param path
     * @return
     */
    public static Bitmap createBitmap(String path) {
        if (path == null) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;  //
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            System.out.println("-bitmap-------------" + bitmap.getWidth() + Thread.currentThread().getId());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 这个是floatmap的例子
     */
    private void init3() {

        Stu stu = new Stu();
        stu.name = "小强";
        Stu stu1 = new Stu();
        stu1.name = "小明";
        Stu[] stus = {stu, stu1};
        Course course = new Course();
        course.name = "化学";

        Course course1 = new Course();
        course1.name = "物理";
        stu.setCourses(new Course[]{course, course1});
        stu1.setCourses(new Course[]{course, course1});


        Subscriber<Course> subscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.d(tag, course.name);
            }

        };
        Observable.from(stus)
                .flatMap(new Func1<Stu, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Stu student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(subscriber);


    }

    /**
     * lift操作符
     */
    private void init4() {

        Integer []integer= {1,2};
        Observable.from(integer).lift(new Observable.Operator<String, Integer>() {
            @Override
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                // 将事件序列中的 Integer 对象转换为 String 对象
                return new Subscriber<Integer>() {
                            @Override
                            public void onNext(Integer integer) {
                                subscriber.onNext("" + integer);
                            }

                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }
                        };
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("-----"+s);
            }
        });
    }

    /**
     * timer操作符
     */
    private void init5() {
        Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                Toast.makeText(MainActivity.this,"延时两秒",Toast.LENGTH_LONG).show();
                return null;
            }
        }).subscribe();
    }


}

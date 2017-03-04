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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import basex.RxManager;
import bean.Course;
import bean.Stu;
import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;

import static android.R.attr.x;
import static android.R.attr.y;

public class MainActivity extends BaseActivity {

    private Subscriber<String> subscriber;
    private Observable observable;
    private String tag = "------";
    private String path;
    private ImageView img1;
    private Button button;
    private Button button1;
    private CheckBox check;
    private CheckBox check1;
    private TextView tv1;
    private int i;

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
        initRxBus();

    }

    private void initRxBus() {
        mRxManager.on("---", new Action1<Course>() {
            @Override
            public void call(Course course) {
                System.out.println("-------------"+course.name);

            }
        });
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
                        //initLift();
                        initSchedulerPeriodically();
                    }
                });

        check = (CheckBox) findViewById(R.id.checkbox);
        check1 = (CheckBox) findViewById(R.id.checkbox);


        RxCompoundButton.checkedChanges(check).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                button.setEnabled(aBoolean);
                button.setBackgroundResource(aBoolean ? R.color.colorAccent : R.color.colorPrimary);
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
                initFlatmap();
                break;
            case R.id.bu3:
                initTimer();
                break;
            case R.id.bu4:
                startActivity(new Intent(this,SecondActivity.class));
                break;
        }

    }

    /**
     * 最基本的用法包括action
     */
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

        //create() 方法是 RxJava 最基本的创造事件序列的方法。基于这个方法， RxJava 还提供了一些方法用来快捷创建事件队列，例如：
        //just(T...): 将传入的参数依次发送出来。
        //from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();

                //subscriber.onError();
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
        //自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        //自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);*/


    }


    /**
     * defer操作符是直到有订阅者订阅时，才通过Observable的工厂方法创建Observable并执行，
     * defer操作符能够保证Observable的状态是最新的，其流程实例如下：
     */
    private void initDefer() {
        i = 10;
        Observable justObservable = Observable.just(i);
        i = 12;
        Observable deferObservable = Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(i);
            }
        });
        i = 15;

        justObservable.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                System.out.println("just result:" + o.toString());
            }
        });

        deferObservable.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                System.out.println("defer result:" + o.toString());
            }
        });
        /**
         * just result:10
         defer result:15
         */
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
     * 这个是flatmap操作符
     */
    private void initFlatmap() {

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
    private void initLift() {

        Integer[] integer = {1, 2};
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
                System.out.println("-----" + s);
            }
        });
    }

    /**
     * timer操作符
     */
    private void initTimer() {
        Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                Toast.makeText(MainActivity.this, "延时两秒", Toast.LENGTH_LONG).show();
                return null;
            }
        }).subscribe();
    }


    /**
     * Range操作符根据初始值n和数目m发射一系列大于等于n的m个值
     */

    private void initRang() {
        Observable.range(5, 5).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer" + integer);//5,6,7,8,9
            }
        });
    }

    /**
     * Repeat会将一个Observable对象重复发射，我们可以指定其发射的次数。
     */
    private void initRepeat() {
        Observable.just(1, 2, 3).repeat().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer=" + integer);// 1,2,3,1,2,3...重复5次
            }
        });
    }

    /**
     * 间隔时间为一秒；循环执行
     */
    private void initInterval() {
        Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d(tag, "Along" + aLong);
                    }
                });

    }

    /**
     * Buffer操作符定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值。
     * <p>
     * Buffer操作符将一个Observable变换为另一个，原来的Observable正常发射数据，变换产生的Observable发射这些数据的缓存集合。
     */

    private void initBuffer() {
        /*    RxView.clicks(button)
            .buffer(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<ViewClickEvent>>() {
                @Override
                public void onCompleted() {}
                @Override
                public void onError(Throwable e) {}
                @Override
                public void onNext(List<ViewClickEvent> viewClickEvents) {
                    if (viewClickEvents.size() > 0) {
                        Toast.makeText(MainActivity.this, "2秒内点击了" + viewClickEvents.size() + "次", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
            });*/
    }

    /**
     * GroupBy操作符将原始Observable发射的数据按照key来拆分成一些小的Observable，
     * 然后这些小的Observable分别发射其所包含的的数据。
     */
    private void initgroupBy() {
        Observable.just(1, 2, 3, 4, 5).groupBy(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer % 2 == 0;
            }
        }).subscribe(new Action1<GroupedObservable<Boolean, Integer>>() {
            @Override
            public void call(final GroupedObservable<Boolean, Integer> booleanIntegerGroupedObservable) {

                booleanIntegerGroupedObservable.toList().subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        Log.d(tag, "key=" + booleanIntegerGroupedObservable.getKey() + ",values=" + integers);
                        //key=false,values=[1, 3, 5]
                        //key=true,values=[2, 4, 6]
                    }
                });
            }
        });
    }


    private void initScan() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return x + y;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer" + integer);// 1,3,6,10,15,21,28,36,45,55
            }
        });
    }

    /**
     * Distinct操作符用来除去重复数据。
     */

    private void initDistinct() {
        Observable.from(new Integer[]{1, 2, 2, 3, 3, 3, 2, 2, 1})
                .distinct().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer" + integer);//1,2,3
            }
        });

    }

    /**
     * DistinctUntilChanged操作符用来过滤掉连续的重复数据。
     */
    private void initDistinctUntilChaged() {
        Observable.from(new Integer[]{1, 2, 3, 3, 3, 2, 2, 1}).distinctUntilChanged()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(tag, "integer" + integer);//1.2,3,2,1
                    }
                });
    }


    //ElementAt只会返回指定位置的数据。
    private void initELement() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
                .elementAt(4).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer" + integer);//5
            }
        });

    }

    /**
     * filter返回满足过滤条件的数据
     */
    private void initFilter() {
        Observable
                .from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 5;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(tag, "integer" + integer);//1,2,3,4
                    }
                });
    }

    /**
     * First操作符返回第一条数据或者返回满足条件的第一条数据。
     * <p>
     * <p>
     * 还有Last  返回最后一条数据
     */
    private void initFirst() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9})
                .first()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(tag, "integer=" + integer); //1 返回第一条数据

                    }
                });


        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9})
                .first(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 3;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer" + integer);//4 返回满足条件的第一个数据
            }
        });
    }


    /**
     * 过滤掉前n项
     */
    private void initSkip() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9})
                .skip(6)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(tag, "integer" + integer);//7,8,9
                    }
                });
    }

    /**
     * Take操作符只取前n项。
     */
    private void initTake() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9})
                .take(2)
                .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer+" + integer);
            }
        });
    }


    /**
     * Sample操作符会定时地发射源Observable最近发射的数据，其他的都会被过滤掉。
     */


    /**
     * 结合操作
     */

    /**
     * 当两个Observables中的任何一个发射了数据时，
     * 使用一个函数结合每个Observable发射的最近数据项，并且基于这个函数的结果发射数据。
     */
    private void initCombineLatest() {
        Observable<Long> observable = Observable.interval(200, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                });
        Observable<Long> observable1 = Observable.interval(300, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                });

        Observable.combineLatest(observable1, observable, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                return aLong + aLong2;
            }
        }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d(tag, "aLong=" + aLong);
                //0(300) ,5+0(400) ,10+0(600),10+5(600),15+5(800),15+10(900)
                //20+10(1000),20+15(1200),20+20(1500)
            }
        });


        //在实际的开发中我们可以利用该操作符结合RxBinding来实现表单提交的校验。
        //设置按钮是否可以点击
     /*   final Observable<TextViewTextChangeEvent> usernameChangeObservable = RxTextView.textChangeEvents(mUsernameEditText);
        final Observable<TextViewTextChangeEvent> passwordChangeObservable = RxTextView.textChangeEvents(mPasswordEditText);
        button1.setEnabled(false);
        Observable.combineLatest(usernameChangeObservable, passwordChangeObservable,
                new Func2<TextViewTextChangeEvent, TextViewTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewTextChangeEvent event1, TextViewTextChangeEvent event2) {
                        boolean emailCheck = event1.text().length() >= 3;
                        boolean passwordCheck = event2.text().length() >= 3;
                        return emailCheck && passwordCheck;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        button1.setEnabled(aBoolean);
                    }
                });*/
    }

    /**
     *
     */
    private void initMerge() {
        Observable<Long> observable = Observable.interval(200, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }

                }).take(5);

        Observable<Long> observable1 = Observable.interval(300, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                }).take(5);


        Observable.merge(observable1.subscribeOn(Schedulers.io()), observable)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d(tag, "along=" + aLong);//0,0,5,10,5,15,10,20,15,20
                    }
                });

    }

    /**
     * StartWith操作符会在源Observable发射的数据前面插上一些数据。
     */
    private void initStartWith() {
        Observable.just(1, 2, 3, 4).startWith(-1, 0)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(tag, "integer=" + integer);
                    }
                });

        Observable.just(1, 2, 4, 5)
                .startWith(Observable.just(-1, 0))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(tag, "ingteger=" + integer);

                    }
                });

    }

    /**
     * Zip操作符将多个Observable发射的数据按顺序组合起来，每个数据只能组合一次，
     * 而且都是有序的。最终组合的数据的数量由发射数据最少的Observable来决定。
     */
    private void initZip() {


        //  (200)(400)(600) (800) (1000)
        // ---0--- 5---10----15----20
        //    (300)   (600)  (900) (1200)(1500)
        //------0------5------10---  15----20


        Observable<Long> observable = Observable.interval(200, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                }).take(5);
        Observable<Long>
                observable1 = Observable.interval(300, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                }).take(5);
        Observable.zip(observable, observable1, new Func2<Long, Long, Long>() {
            @Override
            public Long call(Long aLong, Long aLong2) {
                return aLong + aLong2;
            }
        }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d(tag, "aLong=" + aLong); //0,10,20,30,40
            }
        });


    }

    /**
     * Delay操作符
     Delay操作符让发射数据的时机延后一段时间，这样所有的数据都会依次延后一段时间发射。
     在Rxjava中将其实现为Delay和DelaySubscription。不同之处在于Delay是延时数据的发射，
     而DelaySubscription是延时注册Subscriber。
     */


    /**
     * Do操作符
     * Do操作符就是给Observable的生命周期的各个阶段加上一系列的回调监听，
     * 当Observable执行到这个阶段的时候，这些回调就会被触发。在Rxjava实现了很多的doXxx操作符。
     * doOnEach可以给Observable加上这样的样一个回调：Observable每发射一个数据的时候就会触发这个回调，
     * 不仅包括onNext还包括onError和onCompleted。
     * DoOnNext则只有onNext的时候才会被触发。
     * <p>
     * doOnNext则只有onNext的时候才会被触发。
     * doOnError会在OnError发生的时候触发回调，并将Throwable对象作为参数传进回调函数里
     * doOnComplete会在OnCompleted发生的时候触发回调。
     * doOnTerminate会在Observable结束前触发回调，无论是正常还是异常终止；
     * finallyDo会在Observable结束后触发回调，无论是正常还是异常终止。
     * <p>
     * doOnSubscribe和doOnUnSubscribe则会在Subscriber进行订阅和反订阅的时候触发回调。
     * 当一个Observable通过OnError或者OnCompleted结束的时候，会反订阅所有的Subscriber。
     */

    private void initDo() {
        Observable.just(1, 2, 3, 4)
                .doOnEach(new Action1<Notification<? super Integer>>() {
                    @Override
                    public void call(Notification<? super Integer> notification) {
                        Log.d(tag, "doOnEach" + notification.getKind().name());
                        // onNext,onNext,onNext,onNext,onCompleted
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d(tag, "dosinscribe");
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d(tag, "doOnUnsubscribe");
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(tag, "integer = " + integer);
                    }
                });
    }

    /**
     * Meterialize操作符将OnNext/OnError/OnComplete
     * 都转化为一个Notification对象并按照原来的顺序发射出来，而DeMeterialize则是执行相反的过程。
     */
    private void initMeterialize() {
        Observable.just(1, 2, 3).materialize()
                .subscribe(new Action1<Notification<Integer>>() {
                    @Override
                    public void call(Notification<Integer> integerNotification) {
                        Log.d(tag, "kind=" + integerNotification.getKind().name()
                                + "value=" + integerNotification.getValue());
                    }
                });
    }


    /**
     * SubscribOn/ObserverOn

     SubscribOn用来指定Observable在哪个线程上运行。
     ObserverOn用来指定观察者所运行的线程。
     */


    /**
     * TimeInterval/TimeStamp
     * <p>
     * TimeInterval会拦截发射出来的数据，然后发射两个发射数据的间隔时间。
     * 对于第一个发射的数据，其时间间隔为订阅后到首次发射的间隔。
     * <p>
     * <p>
     * <p>
     * TimeStamp会将每个数据项给重新包装一下，加上了一个时间戳来标明每次发射的时间。
     */
    private void initTimeInterval() {
        Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .timeInterval().subscribe(new Action1<TimeInterval<Long>>() {
            @Override
            public void call(TimeInterval<Long> longTimeInterval) {
                Log.d(tag, "value=" + longTimeInterval.getIntervalInMilliseconds());
                //
            }
        });
    }

    /**
     *Timeout

     Timeout操作符给Observable加上超时时间，每发射一个数据后就重置计时器，
     当超过预定的时间还没有发射下一个数据，就抛出一个超时的异常。
     */


    /**
     * All操作符
     * <p>
     * All操作符根据一个函数对源Observable发射的所有数据进行判断，最终返回的结果就是这个判断结果。
     * 这个函数使用发射的数据作为参数，
     * 内部判断所有的数据是否满足我们定义好的判断条件，如果全部都满足则返回true，否则就返回false。
     */

    private void initAll() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
                .all(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 10;

                    }
                }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.d(tag, "result" + aBoolean);
            }
        });


    }

    /**
     * Amb操作符
     * <p>
     * Amb操作符可以将至多9个Observable结合起来，让他们竞争。
     * 哪个Observable首先发射了数据
     * （包括onError和onComplete)就会继续发射这个Observable的数据
     * ，其他的Observable所发射的数据都会别丢弃。
     */

    private void initAmb() {
        Observable<Integer> delay3 = Observable.just(1, 2, 3).delay(3000, TimeUnit.MILLISECONDS);
        Observable<Integer> delay2 = Observable.just(4, 5, 6).delay(2000, TimeUnit.MILLISECONDS);
        Observable<Integer> delay1 = Observable.just(7, 8, 9).delay(1000, TimeUnit.MILLISECONDS);
        Observable.amb(delay1, delay2, delay3).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer=" + integer); // 7,8,9
            }
        });
    }

    /**
     * Contains操作符用来判断源Observable所发射的数据是否包含某一个数据，
     * 如果包含会返回true，如果源Observable已经结束了却还没有发射这个数据则返回false。
     */

    private void initContains() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
                .contains(11)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.d(tag, "result is" + aBoolean);
                    }
                });
    }

    /**
     * IsEmpty操作符
     * IsEmpty操作符用来判断源Observable是否发射过数据，如果发射过就会返回false，
     * 如果源Observable已经结束了却还没有发射这个数据则返回true。
     */

    private void initIsEmpty() {
        Observable
                .create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onCompleted();
                    }
                })
                .isEmpty()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.d(tag, "result is " + aBoolean);//result is true
                    }
                });
    }

    /**
     * DefaultIfEmpty操作符会判断源Observable是否发射数据，如果源Observable发射了
     * 数据则正常发射这些数据，如果没有则发射一个默认的数据。
     */

    private void initDefaultEmpty() {

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onCompleted();
            }
        }).defaultIfEmpty(100).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer= " + integer); // 100
            }
        });
    }


    /**
     * SequenceEqual操作符用来判断两个Observable发射的数据序列是否相同
     * （发射的数据相同，数据的序列相同，结束的状态相同），如果相同返回true，否则返回false。
     */
    private void initSequenceEqual() {
        Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 2, 3)).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.d(tag, "result is " + aBoolean);//result is true
            }
        });
        Observable.sequenceEqual(Observable.just(1, 2), Observable.just(1, 2, 3)).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.d(tag, "result is " + aBoolean);//result is false
            }
        });
    }

    /**
     * SkipUnitl根据一个标志Observable来跳过一些数据，当这个标志Observable没有发射数据的时候，
     * 所有源Observable发射的数据都会被跳过；当标志Observable发射了一个数据，则开始正常地发射数据。
     */
    private void initSkipUntil() {
        Observable.interval(1, TimeUnit.SECONDS)
                .skipUntil(Observable.timer(3, TimeUnit.SECONDS)) //延迟3s
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d(tag, "aLong = " + aLong); //2,3,4...
                    }
                });
    }


    /**
     * SkipWhile根据一个函数来判断是否跳过数据，当函数返回值为true的时候则一
     * 直跳过源Observable发射的数据；当函数返回false的时候则开始正常发射数据。
     */

    private void initSkipWhile() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1})
                .skipWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        Log.d(tag, "integer -> " + integer); //如果首次为true后面的将不进行判断
                        return integer < 5; //
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(tag, "integer = " + integer); //5, 6, 5, 4,3,2,1
                    }
                });
    }


    /**
     * TakeUntil操作符
     * TakeUntil使用一个标志Observable是否发射数据来判断，当标志Observable没有发射数据时，
     * 正常发射数据，而一旦标志Observable发射过了数据则后面的数据都会被丢弃。
     */

    private void initTakeUntil() {
        Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(Observable.timer(3, TimeUnit.SECONDS))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                    }
                });
    }

    /**
     * TakeWhile操作符
     * <p>
     * TakeWhile则是根据一个函数来判断是否发射数据，当函数返回值为true的时候正常发射数据；
     * 当函数返回false的时候丢弃所有后面的数据
     */

    private void initTakeWhile() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1})
                .takeWhile(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) { //1,2,3,4,5
                        Log.d(tag, "integer -> " + integer); //如果首次为false后面的将不进行判断
                        return integer < 5; //
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "integer = " + integer); //1,2,3,4,5
            }
        });
    }

    /**
     * Concat操作符
     * Concat操作符将多个Observable结合成一个Observable并发射数据，
     * 并且严格按照先后顺序发射数据，
     * 前一个Observable的数据没有发射完，是不能发射后面Observable的数据的。
     */
    private void initContat() {
        Observable<Integer> observable1 = Observable.just(1, 2, 3);
        Observable<Integer> observable2 = Observable.just(4, 5, 6);
        Observable<Integer> observable3 = Observable.just(7, 8, 9);
        Observable.concat(observable1, observable2, observable3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(tag, "integer=" + integer);// 1,2,3,4,5,6,7,8,9
            }
        });


        //当一个Observable发生错误的时候，发射会终止。

        Observable<Integer> observable4 = Observable.just(1, 2, 3);
        Observable<Integer> observable5 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onError(new Throwable("error"));
            }

        });
        Observable<Integer> observable6 = Observable.just(7, 8, 9);
        Observable.concat(observable4, observable5, observable6)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(tag, e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(tag, "integer=" + integer);// 1,2,3,error
                    }
                });
    }

    /**
     * Count操作符
     * Count操作符用来统计源Observable发射了多少个数据，最后将数目给发射出来；
     * 如果源Observable发射错误，则会将错误直接报出来；在源Observable没有终止前，count是不会发射统计数据的。
     */

    private void initCount() {
        Observable.just(1, 2, 3).count().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(tag, "integer=" + integer); // integer=3
            }
        });
    }

    /**
     * Reduce
     * Reduce操作符接收Observable发射的数据并利用提供的函数的计算结果作为下次计算的参数，
     * 输出最后的结果。首次没有计算结果传入前两个参数。
     */

    private void initReduce() {
        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
                .reduce(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer x, Integer y) {
                        return x + y; // 1+2+3+4+5+6+7+8+9+10
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(tag, "result=" + integer); // result = 55
            }
        });


    }


    private void initSchedulerPeriodically() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> observer) {

                Schedulers.newThread().createWorker()
                        .schedulePeriodically(new Action0() {
                            @Override
                            public void call() {
                                observer.onNext(doNetworkCallAndGetStringResult());
                            }
                        }, 1, 5, TimeUnit.SECONDS);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("polling….", s);
            }
        });


        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {


            }
        });

    }

    private String doNetworkCallAndGetStringResult() {
        return "12";
    }
}

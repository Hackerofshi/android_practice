package com.shixin.ui.dagger.component;

/**
 * Created by shixin on 2017/8/29 0029.
 */

//这里表示Component会从MainModule类中拿那些用@Provides注解的方法来生成
// 需要注入的实例
/*@Component(modules = {MainModule.class,PoetryModule.class})
public interface MainComponent {
    */

import com.shixin.ui.rxjava.Dagger1Activity;
import com.shixin.ui.rxjava.DaggerActivity;

/**
     * 需要用到这个连接器的对象，就是这个对象里面有需要注入的属性
     * （被标记为@Inject的属性）
     * 这里inject表示注入的意思，这个方法名可以随意更改，但建议就
     * 用inject即可。
     *
     * 这个是普通形式
     *
     *//*
    //void inject(DaggerActivity activity);

}*/
//@Component(modules = {MainModule.class, PoetryModule.class})
public abstract class MainComponent {

    /**
     * 需要用到这个连接器的对象，就是这个对象里面有需要注入的属性
     * （被标记为@Inject的属性）
     * 这里inject表示注入的意思，这个方法名可以随意更改，但建议就
     * 用inject即可。
     */
    public abstract void inject(DaggerActivity activity);

    public  abstract void inject(Dagger1Activity activity);

    private static MainComponent sComponent;
    public static MainComponent getInstance(){
        if (sComponent == null){
           // sComponent = DaggerMainComponent.builder().build();
        }
        return sComponent;
    }
}

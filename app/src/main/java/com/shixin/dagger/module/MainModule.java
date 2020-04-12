package com.shixin.dagger.module;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

/**
 * Created by shixin on 2017/8/29 0029.
 *
 *有时候我们并不能直接在构造函数里面添加@Inject注解，或者类中存在多个构造函数时，
 * @Inject也只能注解其中一个构造函数，不能注解多个构造函数，这里是会产生歧义性，
 * 因为Dagger2无法确认调用哪一个构造函数来生成例的实例对象。
 * 另外一种情况是我们在项目中引用第三方类库时，也是无法直接在类构造函数中添加
 * @Inject注解的，所以我们需要用到@Module注解了。
 *@Module是用来生产实例来注入对象的，它类似一个工厂，集中创建要注入的类的对象实例。
 * 下面我们引用一下Gson库来看看@Module是怎么使用的，创建MainModule类：
 *
 *
 *
 *
 *
 */



@Module
public class MainModule {

    /**
     * @Provides 注解表示这个方法是用来创建某个实例对象的，这里我们创建返回Gson对象
     * 方法名随便，一般用provideXXX结构
     * @return 返回注入对象
     */
    @Provides
    public Gson provideGson(){
        return new Gson();
    }
}

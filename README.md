Learning RxJava for Android by example
==============

##1、RxJava 各种操作符

> compose

> map

> flatmap


##2、网络请求，RxJava和Retrofit结合，同时加上了缓存。

> @Query

> @path /{XX}/  代替中间的元素

> QueryMap

> 等等

> 版本要一致

> 结合ok

##3、自定义View的圆角

> BitmapShader

>Matrix

##4、进程之间的通讯aidl

>进程间通讯，还有messenger

>可以自定义类集成IBinder实现其中的方法

##5、实现了RxBus

>注意需要解除绑定

##6、自定义ViewGroup CircleMenuLayout模仿建行圆形菜单

>重写dispatchTouchEvent

##7、自定义ViewGroup FlowLayout

>viewGroup的测量方式

##8、自定义Drawable实现圆角 和背景点击变色

>点击条目状态的变换

##9、自定义ListView

>点击条目状态的变换


>持续更新

# 《Koin》

## example 

### 1.reposity

```
interface HelloRepository {
    fun giveHello(): String
}

class HelloRepositoryImpl() : HelloRepository {
    override fun giveHello() = "Hello Koin"
}

```

### 2.presenter

```  
class MySimplePresenter(val repo: HelloRepository) {

    fun sayHello() = "${repo.giveHello()} from $this"
}
```

### 3.module

``` 
     val appModule = module {
 
 
         //datasource实例化，单例
         single { RetrofitClient.instance.create(RemoteDatasource::class.java) }
 
         // single instance of HelloRepository
         single<HelloRepository> { HelloRepositoryImpl() }
 
         // Simple Presenter Factory
         factory { MySimplePresenter(get()) }
     }
```

### 4.application

```

androidLogger：用来打印日志
androidContext：向Koin中注入context
androidFileProperties：从assets中读取保存的数据
modules：注册声明的Module
    startKoin {
            // declare used Android context
            androidContext(this@App)
            // declare modules
            modules(AppModule.appModule)

            androidFileProperties()
            androidLogger()
        }

```

### 5.注入

```
    // Lazy injected MySimplePresenter
    val firstPresenter: MySimplePresenter by inject()
    val h: HelloRepositoryImpl by inject()
    val api:RemoteDatasource by inject()
```




## single 单例

## factory
可以在module中使用factory
```
val myModule = module {    single {DataRepository()}    factory<Presenter>{MyPresenter(get())}    single {HttpClient(getProperty("server_url"))}}
```

- factory就是获取的时候每次都生成一个新的实例

## qualifiers
限定符，用来区别同一个类的不同实例
 ```
single(named("dev")) { DataRepository()} 
single(named("test")) { DataRepository() }
```

## bind
将指定的实例绑定到对应的class
```
single {TestRepository(get())} bind TestViewModel::class
```

注入时传递参数
声明：

```
single{(view : View)->Presenter(view)}
```

使用：
```
val presenter : Presenter by inject {parametersOf(view)}
```

## flags
###  createAtStart
在start的时候就创建实例。一般都是需要注入的时候才创建的
```
val testModule =module(createAtStart =true){....}
```
### override


覆盖另一个module中的实例
```
val myModuleA =module{    single<Service>{ServiceImp()}}
val myModuleB =module{    single<Service>(override=true){TestServiceImp()}}
```

## scope
Scope就是一个实例的作用范围，一旦该作用范围结束，该实例就是从容器中移除。

Koin有3种scope
single： 单实例scope，该scope下的实例不会被移除
factory： 每次都是创建新实例
scoped： 自定义scope
### 自定义scope
```
module {scope(named("A Scope Name")){        scoped {Presenter()}}}
//创建scopeval scope = koin.createScope("myScope","A_SCOPE_NAME")
//获取实例val presenter = scope.get<Presenter>()
```
### scope中的依赖
```
class ComponentAclassComponentB(val a : ComponentA)module {scope(named("A_SCOPE_NAME")){        scoped {ComponentA()}        scoped {ComponentB(get())}}}
```
使用
```
val myScope = koin.createScope("myScope",named("A_SCOPE_NAME"))val componentA = myScope.get<ComponentA>()val componentB = myScope.get<ComponentB>()
```
关闭scope
```
val myScope = koin.createScope("myScope",named("A_SCOPE_NAME"))myScope.close()
```
在android中的scope
Activity和Fragment默认添加了扩展, 可以直接使用
```
class DetailActivity :AppCompatActivity(){
    val presenter : Presenter by currentScop.inject()
}
```
FileProperties
FileProperties用来存储文本数据，就是一个普通的Properties文件，在Android中是放在assets下面。路径为/assets/koin.properties。 startKoin时候会把文件中的内容都load进去。

使用
```
val testModule =module{   factory{TestModel(getProperty("test"))}}
```

## hilt


### Qualifier

要提供同一个接口的不同实现, 可以用不同的注解来标记. (dagger之前用的是@Named).

A qualifier is an annotation used to identify a binding.

举例: LoggerDataSource接口提供了内存和数据库两种实现.

定义两个注解:
```
@Qualifier
annotation class InMemoryLogger

@Qualifier
annotation class DatabaseLogger
module中提供的时候用来标记相应的依赖:

@InstallIn(ApplicationComponent::class)
@Module
abstract class LoggingDatabaseModule {

    @DatabaseLogger
    @Singleton
    @Binds
    abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource): LoggerDataSource
}
```
```
@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {

    @InMemoryLogger
    @ActivityScoped
    @Binds
    abstract fun bindInMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}
```
这里用了两个module因为它们对应两个不同的component, 一个是application一个是activity, 依赖也是相应的scope.

注入的时候也对应加上: 根据注解就可以自动注入某一种实现
```
@InMemoryLogger
@Inject
lateinit var logger: LoggerDataSource
```


## 状态栏仿写


## ViewDragHelper

https://blog.csdn.net/briblue/article/details/73730386



## hilt

https://juejin.cn/post/6902009428633698312


## DSL

https://www.jianshu.com/p/f5f0d38e3e44




---
## 编译错误的查找
> android databinding kotlin A failure occurred while executing org.jetbrains.kotlin.gradle.internal.KaptExecution 编译错误的查找

一般都和插件有关，查看项目的build.gradle的插件是否符合需求

---
FFmpeg 视频处理
https://github.com/yangjie10930/EpMedia


-------
APT
https://juejin.cn/post/6978500975770206239#heading-18



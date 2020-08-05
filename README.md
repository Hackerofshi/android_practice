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

# Koin

## single 单例

## factory
可以在module中使用factory
```
val myModule = module {    single {DataRepository()}    factory<Presenter>{MyPresenter(get())}    single {HttpClient(getProperty("server_url"))}}
```

- factory就是获取的时候每次都生成一个新的实例

## qualifiers
限定符，用来区别同一个类的不同实例

single(named("dev")) { DataRepository() } 
single(named("test")) { DataRepository() }

## bind
将指定的实例绑定到对应的class

  single {TestRepository(get())} bind TestViewModel::class

注入时传递参数
声明：

single{(view : View)->Presenter(view)}

使用：

val presenter : Presenter by inject {parametersOf(view)}

## flags
createAtStart
在start的时候就创建实例。一般都是需要注入的时候才创建的

val testModule =module(createAtStart =true){....}
override
覆盖另一个module中的实例
val myModuleA =module{    single<Service>{ServiceImp()}}
val myModuleB =module{    single<Service>(override=true){TestServiceImp()}}

## scope
Scope就是一个实例的作用范围，一旦该作用范围结束，该实例就是从容器中移除。

Koin有3种scope
single： 单实例scope，该scope下的实例不会被移除
factory： 每次都是创建新实例
scoped： 自定义scope
自定义scope
module {scope(named("A Scope Name")){        scoped {Presenter()}}}
//创建scopeval scope = koin.createScope("myScope","A_SCOPE_NAME")
//获取实例val presenter = scope.get<Presenter>()

scope中的依赖
class ComponentAclassComponentB(val a : ComponentA)module {scope(named("A_SCOPE_NAME")){        scoped {ComponentA()}        scoped {ComponentB(get())}}}

使用

val myScope = koin.createScope("myScope",named("A_SCOPE_NAME"))val componentA = myScope.get<ComponentA>()val componentB = myScope.get<ComponentB>()

关闭scope
val myScope = koin.createScope("myScope",named("A_SCOPE_NAME"))myScope.close()

在android中的scope
Activity和Fragment默认添加了扩展, 可以直接使用

class DetailActivity :AppCompatActivity(){val presenter : Presenter by currentScop.inject()}

FileProperties
FileProperties用来存储文本数据，就是一个普通的Properties文件，在Android中是放在assets下面。路径为/assets/koin.properties。 startKoin时候会把文件中的内容都load进去。

使用

val testModule =module{   factory{TestModel(getProperty("test"))}}


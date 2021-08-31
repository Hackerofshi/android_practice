package com.shixin.ui.jetpack.flow;

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.shixin.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import retrofit2.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

const val name = "jjjj"

public class FlowDemoActivity : AppCompatActivity() {


    //文章 https://zhuanlan.zhihu.com/p/114295411
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_demo)


        findViewById<Button>(R.id.btn1).setOnClickListener {
            //  Demo1()
            test1();
        }

    }

    private fun demo1() {
        CoroutineScope(Dispatchers.Main).launch {
            flowOf(1, 2, 3, 4, 5)
                    .onEach {
                        delay(100)
                    }.collect {
                        println(it)
                    }
        }
    }

    //创建 Flow
    //新元素通过 emit 函数提供，Flow 的执行体内部也可以调用其他挂起函数，这样我们就可以在每次提供一个新元素后再延时 100ms 了。
    private fun test1() {
        val intFlow = flow {
            (1..3).forEach {
                emit(it)
                delay(100)
            }
        }
        //Flow 也可以设定它运行时所使用的调度器：
        //通过 flowOn 设置的调度器只对它之前的操作有影响，因此这里意味着 intFlow 的构造逻辑会在 IO 调度器上执行。
        //
        //最终消费 intFlow 需要调用 collect 函数，这个函数也是一个挂起函数，我们启动一个协程来消费 intFlow：
        intFlow.flowOn(Dispatchers.IO)

        //代码清单4： 消费 Flow
        CoroutineScope(Dispatchers.Main).launch() {
            intFlow.collect { println(it) }
        }
    }

    //Flow 的异常处理也比较直接，直接调用 catch 函数即可：
    //我们在 Flow 的参数中抛了一个异常，在 catch 函数中就可以直接捕获到这个异常。如果没有调用 catch 函数，
    // 未捕获异常会在消费时抛出。请注意，catch 函数只能捕获它的上游的异常。
    private fun test2() {
        flow {
            emit(1)
            throw ArithmeticException("Div 0")
        }.catch { t: Throwable ->
            println("caught error: $t")
        }

        //如果我们想要在流完成时执行逻辑，可以使用 onCompletion：
        //代码清单8：订阅流的完成
        CoroutineScope(Dispatchers.Main).launch() {
            flow {
                emit(1)
                throw ArithmeticException("Div 0")
            }.catch { t: Throwable ->
                println("caught error: $t")
            }.onCompletion { t: Throwable? ->
                println("finally.")
            }.collect { println(it) }
        }
        //onCompletion 用起来比较类似于 try ... catch ... finally
        // 中的 finally，无论前面是否存在异常，它都会被调用，参数 t 则是前面未捕获的异常。

    }

    //代码清单11：Flow 从异常中恢复
    //这里我们可以使用 emit 重新生产新元素出来。细心的读者一定会发现，emit 定义在 FlowCollector 当中，
    // 因此只要遇到 Receiver 为 FlowCollector 的函数，我们就可以生产新元素。
    private fun test3() {
        flow {
            emit(1)
            throw ArithmeticException("Div 0")
        }.catch { t: Throwable ->
            println("caught error: $t")
            emit(10)
        }
    }

    //末端操作符
    //前面的例子当中，我们用 collect 消费 Flow 的数据。collect 是最基本的末端操作符，
    // 功能与 RxJava 的 subscribe 类似。除了 collect 之外，还有其他常见的末端操作符，大体分为两类：
    //
    //集合类型转换操作，包括 toList、toSet 等。
    //聚合操作，包括将 Flow 规约到单值的 reduce、fold 等操作，以及获得单个元素的操作包括 single、singleOrNull、first 等。

    //实际上，识别是否为末端操作符，还有一个简单方法，由于 Flow 的消费端一定需要运行在协程当中，因此末端操作符都是挂起函数。


    //分离 flow 的消费和触发
    //我们除了可以在 collect 处消费 Flow 的元素以外，还可以通过 onEach 来做到这一点。这样消费的具体操作就不
    // 需要与末端操作符放到一起，collect 函数可以放到其他任意位置调用，例如：
    fun createFlow() = flow<Int> {
        (1..3).forEach {
            emit(it)
            delay(100)
        }
    }.onEach {
        println(it)
    }

    //代码清单12：分离 Flow 的消费和触发
    fun test4() {
        CoroutineScope(Dispatchers.Default).launch {
            createFlow().collect()
        }

        //代码清单13：使用协程作用域直接触发 Flow
        //其中 launchIn 函数只接收一个 CoroutineScope 类型的参数。
        createFlow().launchIn(GlobalScope)
    }

    //Flow 的取消 ###
    //Flow 没有提供取消操作，原因很简单：不需要。
    //
    //我们前面已经介绍了 Flow 的消费依赖于 collect 这样的末端操作符，而它们又必须在协程当中调用，因此 Flow 的取消主要依赖于末端操作符所在的协程的状态。
    val job = GlobalScope.launch {
        val intFlow = flow {
            (1..3).forEach {
                delay(1000)
                emit(it)
            }
        }

        intFlow.collect { println(it) }
    }

    //如此看来，想要取消 Flow 只需要取消它所在的协程即可。
    private fun test5() {
        CoroutineScope(Dispatchers.Default).launch {
            delay(2500)
            job.cancelAndJoin()
        }
    }

    //其他 Flow 的创建方式
    //想要在生成元素时切换调度器，就必须使用 channelFlow 函数来创建 Flow：
    @ExperimentalCoroutinesApi
    private fun test6() {

        channelFlow {
            send(1)
            withContext(Dispatchers.IO) {
                send(2)
            }
        }

        //此外，我们也可以通过集合框架来创建 Flow：
        listOf(1, 2, 3, 4).asFlow()
        setOf(1, 2, 3, 4).asFlow()
        flowOf(1, 2, 3, 4)
    }


    //Flow 的背压
    //只要是响应式编程，就一定会有背压问题，我们先来看看背压究竟是什么。
    //
    //背压问题在生产者的生产速率高于消费者的处理速率的情况下出现。为了保证数据不丢失，
    // 我们也会考虑添加缓存来缓解问题：
    //代码清单16：为 Flow 添加缓冲

    var job1 = flow {
        List(100) {
            emit(it)
        }
    }.buffer()


    private fun test7() {
        CoroutineScope(Dispatchers.Default).launch {
            job1.collect {

            }
        }
    }

    //我们也可以为 buffer 指定一个容量。不过，如果我们只是单纯地添加缓存，而不是从根本上解决问题就始终会造成数据积压。
    //问题产生的根本原因是生产和消费速率的不匹配，除直接优化消费者的性能以外，我们也可以采取一些取舍的手段。
    //
    //第一种是 conflate。与 Channel 的 Conflate 模式一致，新数据会覆盖老数据，例如：
    //代码清单17：使用 conflate 解决背压问题
    private fun test8() {
        CoroutineScope(Dispatchers.Default).launch {
            flow {
                List(100) {
                    emit(it)
                }
            }.conflate()
                    .collect { value ->
                        println("Collecting $value")
                        delay(100)
                        println("$value collected")
                    }
        }
        //我们快速地发送了 100 个元素，最后接收到的只有两个，当然这个结果每次都不一定一样：
        //
        //Collecting 1
        //1 collected
        //Collecting 99
        //99 collected
    }

    //第二种是 collectLatest。顾名思义，只处理最新的数据，这看上去似乎与 conflate 没有区别，
// 其实区别大了：它并不会直接用新数据覆盖老数据，而是每一个都会被处理，只不过如果前一个还没被处理完后一个就来了的话，处理前一个数据的逻辑就会被取消。
    //代码清单18：使用 collectLatest 解决背压问题
    private fun test9() {
        CoroutineScope(Dispatchers.Default).launch {
            flow {
                List(100) {
                    emit(it)
                }
            }.collectLatest { value ->
                println("Collecting $value")
                delay(100)
                println("$value collected")
            }
        }
        //前面的 Collecting 输出了 0 ~ 99 的所有结果，而 collected 却只有 99，因为后面的数据到达时，处理上一个数据的操作正好被挂起了（请注意delay(100)）。
        //
        //除 collectLatest 之外还有 mapLatest、flatMapLatest 等等，都是这个作用。
    }

    //Flow 的变换
    //我们已经对集合框架的变换非常熟悉了，Flow 看上去极其类似于这样的数据结构，这一点与 RxJava 的 Observable 的表现也基本一致。
    //
    //例如我们可以使用 map 来变换 Flow 的数据：

    // 代码清单19：Flow 的元素变换
    private fun test10() {
        flow {
            List(5) { emit(it) }
        }.map {
            it * 2
        }

        //代码清单20：Flow 的嵌套
        flow {
            List(5) { emit(it) }
        }.map {
            flow { List(it) { emit(it) } }
        }
        //这实际上得到的是一个数据类型为 Flow 的 Flow，
        // 如果希望将它们拼接起来，可以使用 flattenConcat：

        //拼接的操作中 flattenConcat 是按顺序拼接的，结果的顺序仍然是生产时的顺序；还有一个是 flattenMerge，
        // 它会并发拼接，因此结果不会保证顺序。
        CoroutineScope(Dispatchers.Default).launch {
            flow {
                List(5) { emit(it) }
            }.map {
                flow { List(it) { emit(it) } }
            }.flattenConcat()
                    .collect { println(it) }
        }
    }

    //代码清单22：使用 Flow 实现对 await 的多路复用
    private fun test11() {

        //这其中，① 处用创建了两个函数引用组成的 List；② 处调用它们得到 deferred；③ 处比较关键，对于每一个
        // deferred 我们创建一个单独的 Flow，并在 Flow 内部发送 deferred.await() 返回的结果，
        // 即返回的 User 对象；现在我们有了两个 Flow 实例，我们需要将它们整合成一个 Flow 进行处理，
        // 调用 merge 函数即可。

        /*coroutineScope {
                val login = "..."
                listOf(::getUserFromApi, ::getUserFromLocal) ... ①
                .map { function ->
                function.call(login) ... ②
            }
                    .map { deferred ->
                        flow { emit(deferred.await()) } ... ③
                    }
                    .merge() ... ④
                .onEach { user ->
                println("Result: $user")
            }.launchIn(this)
            }*/
    }

    val channels = List(10) { Channel<Int>() }

    //这比 select 的版本看上去要更简洁明了，每个 Channel 都通过 consumeAsFlow 函数被映射成 Flow，
    // 再 merge 成一个 Flow，取第一个元素。
    @ExperimentalCoroutinesApi
    private fun test12() {

        CoroutineScope(Dispatchers.Default).launch {
            val result = channels.map {
                it.consumeAsFlow()
            }.merge().first()
        }

        sum(1,2)
    }
}



val sum = { x: Int, y: Int -> x + y }
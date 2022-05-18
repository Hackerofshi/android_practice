package com.shixin.kotlin.dsl


class Demo {

    //使用函数式编程
    private var listener: (Int) -> Int = {
        100
    }

    fun setMethodListener1(listener: ((Int) -> Int)) {
        this.listener = listener
    }


    //
    //函数式接口 VS 函数类型
    //这里很有意思，对于SAM接口，Kotlin可以把lambda转换成对应的SAM（single abstract method）接口实例，从而简化代码，这其实就是一个约定，
    // 但是当Kotlin代码定义SAM接口，将无法转换，因为Kotlin自己就有函数类型。
    //KT文件单方法接口
    interface OneMethodListener1 {
        fun onMethod(a: Int)
    }

    //接口实例
    private var listener1: OneMethodListener1? = null

    //设置监听
    fun setOnMethodListener1(listener: OneMethodListener1) {
        this.listener1 = listener
    }

    //高阶函数
    //高阶函数其实很简单，就是参数是其他函数或者返回值是其他函数的函数
    private var onMethodListener: ((Int) -> Unit)? = null

    fun setMethodListener(listener: ((Int) -> Unit)?) {
        this.onMethodListener = listener
    }


    fun setMethodListener2(tag: Boolean = true, listener: ((Int) -> Unit)?) {
        this.onMethodListener = listener
    }


}

fun main() {
    test1()

    val demo = Demo()


    var tempString = "zyh"
    demo.setMethodListener1 { i ->
        tempString = "wy"
        100
    }


    //但是当Kotlin代码定义SAM接口，将无法转换，因为Kotlin自己就有函数类型。
    // IDE并没有提醒我们可以改成lambda，这就是不支持约定，因为在Kotlin中，
    // 这种SAM接口可以使用高阶函数来实现，比如下面代码：
    demo.setOnMethodListener1(object : Demo.OneMethodListener1 {
        override fun onMethod(a: Int) {
        }
    })

    //高阶函数接口实现
    demo.setMethodListener {

    }


    demo.setMethodListener2(tag = false) {

    }


    //SAM构造方法
    val testJava = TestJava()
    //简化写法
    testJava.setSingleFunctionListener {
        Runnable {

        }
    }
}

fun test() {
    val demo = Demo()

    demo.printSum { i, i2 -> i + i2 }

    demo.printSum(sum)


    // 调用高阶函数
    kotlinDSL {
        // 这个 lambda 的接收者类型为StringBuilder
        append(" DSL")
        println(this)
    }


    "key" to "value"
    val test = "kotlin".should(start).with("kot")
}


// printSum 为高阶函数，定义了 lambda 形参
fun Demo.printSum(sum: (Int, Int) -> Int) {
    val result = sum(1, 2)
    println(result)
}

val sum = { x: Int, y: Int -> x + y }

//lambda 作为形参函数声明时，可以携带接收者，如下图：
// 声明接收者,结束StringBuilder的函数
fun kotlinDSL(block: StringBuilder.() -> Unit) {
    block(StringBuilder("Kotlin"))
}


//中缀调用
//infix 修饰符代表该函数支持中缀调用，然后为任意对象提供扩展函数 to，接受任意对象作为参数，最终返回键值对。
object start

infix fun String.should(start: start): String = ""
infix fun String.with(str: String): String = ""

fun test0() {
    "kotlin" should start with "kot"

    // 等价于
    "kotlin".should(start).with("kot")


    "key" to "value"

    // 等价于
    val s = "key".to("value")
}

//invoke 约定
class Person(val name: String) {
    operator fun invoke() {
        println("my name is $name")
    }
}

fun test1() {
    val person = Person("geniusmart")
    person()
}


fun test11() {
    val items = listOf(1, 2, 3, 4, 5)

    // Lambdas 表达式是花括号括起来的代码块。
    items.fold(0) {
        // 如果一个 lambda 表达式有参数，前面是参数，后跟“->”
            acc: Int, i: Int ->
        print("acc = $acc, i = $i, ")
        val result = acc + i
        println("result = $result")
        // lambda 表达式中的最后一个表达式是返回值：
        result
    }

    //acc = 0, i = 1, result = 1
    //acc = 1, i = 2, result = 3
    //acc = 3, i = 3, result = 6
    //acc = 6, i = 4, result = 10
    //acc = 10, i = 5, result = 15


    // lambda 表达式的参数类型是可选的，如果能够推断出来的话：
    //joinedToString = Elements: 1 2 3 4 5
    val joinedToString = items.fold("Elements:") { acc, i -> "$acc $i" }

    // 函数引用也可以用于高阶函数调用：
    val product = items.fold(1, Int::times)
}




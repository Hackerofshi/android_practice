package com.shixin.ui.practice

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.shixin.R
import kotlinx.android.synthetic.main.activity_lambda.*
import java.util.Collections.max

//Lambda表达式的本质其实是匿名函数，因为在其底层实现中还是通过匿名函数来实现的。
// 但是我们在用的时候不必关心起底层实现。不过Lambda的出现确实是减少了代码量的编写，同时也是代码变得更加简洁明了。
class LambdaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lambda)

        btn_sum.setOnClickListener {

            val sum = sum(1, 3)
            Log.i("num", "onCreate: $sum")

            val items = listOf(1, 2, 3, 4, 5)

            // Lambdas 表达式是花括号括起来的代码块。
            items.fold(0, {
                // 如果一个 lambda 表达式有参数，前面是参数，后跟“->”
                    acc: Int, i: Int ->
                print("acc = $acc, i = $i, ")
                val result = acc + i
                println("result = $result")
                // lambda 表达式中的最后一个表达式是返回值：
                result
            })

        }
    }

}

val tests = fun(a: Int, b: Int): Int {
    return a + b
}

val sum1: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
val sum = { x: Int, y: Int -> (x + y) }


// lambda
//高阶函数，当Lambda表达式作为其一个参数时，只为其表达式提供了参数类型与返回类型，
// 所以，我们在调用此高阶函数的时候我们要为该Lambda表达式写出它的具体实现。
fun test(a: Int, b: (num1: Int, num2: Int) -> Int): Int {
    //invoke()函数：表示为通过函数变量调用自身，因为上面例子中的变量b是一个匿名函数
    return a + b.invoke(3, 5)
}

//、it
//it并不是Kotlin中的一个关键字(保留字)。
//it是在当一个高阶函数中Lambda表达式的参数只有一个的时候可以使用it来使用此参数。
// it可表示为单个参数的隐式名称，是Kotlin语言约定的。
//例1：

val it: Int = 0  // 即it不是`Kotlin`中的关键字。可用于变量名称

fun test2() {
    // 这里举例一个语言自带的一个高阶函数filter,此函数的作用是过滤掉不满足条件的值。
    val arr = arrayOf(1, 3, 5, 7, 9)
// 过滤掉数组中元素小于2的元素，取其第一个打印。这里的it就表示每一个元素。
    println(arr.filter { it < 5 }.component1())
}

fun test3(num1: Int, bool: (Int) -> Boolean): Int {
    return if (bool(num1)) {
        num1
    } else {
        0
    }
}

//代码讲解：上面的代码意思是，在高阶函数test中，其返回值为Int类型，Lambda表达式以num1位条件。其中如果Lambda表达式的值
// 为false的时候返回0，反之返回num1。故而当条件为num1 > 5这个条件时，
// 当调用test函数，num1 = 10返回值就是10，num1 = 4返回值就是0。
fun demo01() {
    println(test3(10, { it > 5 }))
    println(test3(4, { a: Int -> a > 6 }))
}

val test4: (Int) -> Boolean = { a: Int ->
    a > 6
}

//3.2、下划线（_）
//在使用Lambda表达式的时候，可以用下划线(_)表示未使用的参数，表示不处理这个参数。
//
//同时在遍历一个Map集合的时候，这当非常有用。
@RequiresApi(Build.VERSION_CODES.N)
fun demo02() {
    val map = mapOf("key1" to "value1", "key2" to "value2", "key3" to "value3")

    map.forEach { key, value ->
        println("$key \t $value")
    }

// 不需要key的时候
    map.forEach { _, value ->
        println("$value")
    }
}


val test1 = fun(x: Int, y: Int) = x + y  // 当返回值可以自动推断出来的时候，可以省略，和函数一样
val test2 = fun(x: Int, y: Int): Int = x + y
val test3 = fun(x: Int, y: Int): Int {
    return x + y
}

//在kotlin中，提供了指定的接受者对象调用Lambda表达式的功能。在函数字面值的函数体中，
// 可以调用该接收者对象上的方法而无需任何额外的限定符。它类似于扩展函数，它允你在函数体内访问接收者对象的成员。
val iop = fun Int.(other: Int): Int = this + other
fun demo03() {
    println(2.iop(3))
}

// 声明接收者
//lambda 作为形参函数声明时，可以携带接收者，如下图：
//带接收者的 lambda 丰富了函数声明的信息，当传递该 lambda值时，将携带该接收者，比如：
fun kotlinDSL(block:StringBuilder.()->Unit){
    block(StringBuilder("Kotlin"))
}




//要用Lambda表达式作为接收者类型的前提是接收着类型可以从上下文中推断出来。
class HTML {
    fun body() {}
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()  // 创建接收者对象
    html.init()        // 将该接收者对象传给该 lambda
    return html
}

fun demo04() {

    html {       // 带接收者的 lambda 由此开始
        body()   // 调用该接收者对象的一个方法
    }

    // 调用高阶函数
    kotlinDSL {
        // 这个 lambda 的接收者类型为StringBuilder
        append(" DSL")
        println(this)
    }
}


fun <T, R> Collection<T>.fold(
    initial: R,
    combine: (acc: R, nextElement: T) -> R
): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}

fun test(b : Int): () -> Int{
    var a = 3
    return fun() : Int{
        a++
        return a + b
    }
}

//这段源码理解起来不难，infix 修饰符代表该函数支持中缀调用，
// 然后为任意对象提供扩展函数 to，接受任意对象作为参数，最终返回键值对。
fun demo05(){
    "key" to "value"
    // 等价于
    "key".to("value")

}

infix fun Any.to(that:Any) = Pair(this,that)

package com.shixin.kotlin.dsl



class Demo {

}

fun main() {
    test1()
}

fun test() {
    val demo = Demo()

    demo.printSum { i, i2 -> i + i2 }


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
    items.fold(0, {
        // 如果一个 lambda 表达式有参数，前面是参数，后跟“->”
            acc: Int, i: Int ->
        print("acc = $acc, i = $i, ")
        val result = acc + i
        println("result = $result")
        // lambda 表达式中的最后一个表达式是返回值：
        result
    })
    //acc = 0, i = 1, result = 1
    //acc = 1, i = 2, result = 3
    //acc = 3, i = 3, result = 6
    //acc = 6, i = 4, result = 10
    //acc = 10, i = 5, result = 15



    // lambda 表达式的参数类型是可选的，如果能够推断出来的话：
    //joinedToString = Elements: 1 2 3 4 5
    val joinedToString = items.fold("Elements:", { acc, i -> acc + " " + i })

    // 函数引用也可以用于高阶函数调用：
    val product = items.fold(1, Int::times)
}
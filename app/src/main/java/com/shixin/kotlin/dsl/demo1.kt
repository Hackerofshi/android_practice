package com.shixin.kotlin.dsl



class Demo {

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
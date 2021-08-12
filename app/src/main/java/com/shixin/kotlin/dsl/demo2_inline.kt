package com.shixin.kotlin.dsl

import android.util.Log
import retrofit2.http.Body

val TAG = "tag"

fun main() {

}

//内联函数定义  ：在编译时期，把调用这个函数的的地方用这个函数的方法体进行替换

//不应该使用inline的情况
//没有任何阐述的函数
//private inline fun makeTest(){
//    Log.i(TAG, "makeTest: ")
//}

//或者带有基本变量参数的函数，编译器也会报错
//private inline fun makeTest2(test:String){
//    Log.i(TAG, "makeTest2: ")
//}

//inline在一般的方法上标注是不会起很大作用的，inline能带来的性能提升，往往是参数是lambda的函数上


//body 本身是一个函数

fun foo(body: () -> Unit) {
    println("foo()   ")
}

//ordinaryFunction 上标注inline可以使得方法的调用减少一层
inline fun ordinaryFunction(block: () -> Unit) {
    println("jjjjj")
    block.invoke()
    println("结束")
}

//支持return 退出函数

fun foo1(body: () -> Unit){
    ordinaryFunction {
        println(" 表达式退出")
        return
    }
}
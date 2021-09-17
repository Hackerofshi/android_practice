package com.shixin.kotlin

open class Base(var b: Int) {}


class Test : Base {
    constructor(i: Int) : super(i) {
        println(i)
    }
}



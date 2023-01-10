package com.shixin.kotlin.dsl

class Book() {
    var name = "《数据结构》"
    var price = 60
    fun displayInfo() = print("Book name : $name and price : $price")
}

//用于初始化对象或更改对象属性，可使用apply
//如果将数据指派给接收对象的属性之前验证对象，可使用also
//如果将对象进行空检查并访问或修改其属性，可使用let
//如果是非null的对象并且当函数块中不需要返回值时，可使用with
//如果想要计算某个值，或者限制多个本地变量的范围，则使用run
//

var name: String? = null

fun main() {
    //按照我们的编程思想，打印一个对象，输出必定是对象，但是使用let函数后，
    // 输出为最后一句字符串。这是由于let函数的特性导致。因为在Kotlin中，如果let块中的最后一条语句是非赋值语句，则默认情况下它是返回语句。
    val let: String = Book().let {
        it.price = 50
        "This book is ${it.name}"
    }
    print(let)
    //let块中的最后一条语句如果是非赋值语句，则默认情况下它是返回语句，反之，则返回的是一个 Unit类型
    val book: Unit = Book().let {
        print("----")
        it.name = "《计算机网络》"
    }
    print(book)

    //let可用于空安全检查。 如需对非空对象执行操作，可对其使用安全调用操作符 ?.
    // 并调用 let 在 lambda 表达式中执行操作。如下案例：
    val nameLength = name?.let {
        it.length
    } ?: "test"
    print(nameLength)


    //let可对调用链的结果进行操作。
    //使用let后可以直接对数组列表中长度大于3的值进行打印，去掉了变量赋值这一步。
    val numbers = mutableListOf("One", "Two", "Three", "Four", "Five")
    numbers.map { it.length }.filter { it > 3 }.let {
        print(it)
    }

    //let可以将“It”重命名为一个可读的lambda参数。
    val book1 = Book().let { book ->
        book.price = 300
    }
    print(book1)

}
//let将上下文对象引用为it ，而run引用为this；
//run无法将“this”重命名为一个可读的lambda参数，而let可以将“it”重命名为一个可读的lambda参数。
// 在let多重嵌套时，就可以看到这个特点的优势所在。


fun testRun() {
    //当 lambda 表达式同时包含对象初始化和返回值的计算时，run更适合。
    Book().run {
        price = 30
        displayInfo()
    }
    //与let一样，run是作为T的扩展函数；
    //inline fun <T, R> T.run(block: T.() -> R): R

    //第二个run的声明方式则不同，它不是扩展函数，并且块中也没有输入值，
    // 因此，它不是用于传递对象并更改属性的类型，而是可以使你在需要表达式的地方就可以执行一个语句。
    run {
        val book = Book()
        book.name = "《计算机网络》"
        book.price = 30
        book.displayInfo()
    }
}
//with和run其实做的是同一种事情，对上下文对象都称之为“this”，但是他们又存在着不同，我们来看看案例。
//相比较with来说，run函数更加简便，空安全检查也没有with那么频繁。
//inline fun <T, R> with(receiver: T, block: T.() -> R): R
fun testWith() {
    val book = Book()

    //with使用的是非null的对象，当函数块中不需要返回值时，可以使用with。
    with(book) {
        name = "《计算机网络》"
        price = 40
    }
    print(book)
}
//apply inline fun <T> T.apply(block: T.() -> Unit): T
//apply函数主要用于初始化或更改对象，因为它用于在不使用对象的函数的情况下返回自身。
fun testApply() {

    //apply是 T 的扩展函数,与run函数有些相似，它将对象的上下文引用为“this”而不是“it”，
    // 并且提供空安全检查，不同的是，apply不接受函数块中的返回值，返回的是自己的T类型对象。
    val book = Book().apply {
        name = "《计算机网络》"
        price = 40
    }
    print(book)
}

//inline fun <T> T.also(block: (T) -> Unit): T
fun testAlso() {
    //also是 T 的扩展函数，返回值与apply一致，直接返回T。
    // also函数的用法类似于let函数，将对象的上下文引用为“it”而不是“this”以及提供空安全检查方面。
    val book  = Book().also {
        it.name = "《计算机网络》"
        it.price = 40
    }
    print(book)
}
//Contract
//https://zhuanlan.zhihu.com/p/273545744
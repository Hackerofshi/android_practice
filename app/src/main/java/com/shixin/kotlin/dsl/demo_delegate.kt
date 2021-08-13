package com.shixin.kotlin.dsl

import android.graphics.MaskFilter
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


//委托模式已经证明是实现继承的一个很好的替代方式， 而 Kotlin 可以零样板代码地原生支持它。
// Derived 类可以通过将其所有公有成员都委托给指定对象来实现一个接口 Base：


interface Base {
    fun printMessage()
    fun printMessageLine()

    val message: String

}

class BaseImpl(val x: Int) : Base {
    override fun printMessage() {
        print(x)
    }

    override fun printMessageLine() {
        println(x)
    }

    override val message = "BaseImpl: x = $x"


}

//Derived 的超类型列表中的 by-子句表示 b 将会在 Derived 中内部存储，
// 并且编译器将生成转发给 b 的所有 Base 的方法。
class Derived(b: Base) : Base by b {

    //覆盖由委托实现的接口成员
    //覆盖符合预期：编译器会使用 override 覆盖的实现而不是委托对象中的。如果将 override fun printMessage() { print("abc") }
    // 添加到 Derived，那么当调用 printMessage 时程序会输出“abc”而不是“10”：
    override fun printMessage() {
        print("abc")
    }
    // 在 b 的 `print` 实现中不会访问到这个属性
    override val message = "Message of Derived"
}

//有一些常见的属性类型，虽然我们可以在每次需要的时候手动实现它们， 但是如果能够为大家把他们只实现一次并放入一个库会更好。例如包括：
//
//延迟属性（lazy properties）: 其值只在首次访问时计算；
//可观察属性（observable properties）: 监听器会收到有关此属性变更的通知；
//把多个属性储存在一个映射（map）中，而不是每个存在单独的字段中。


//语法是： val/var <属性名>: <类型> by <表达式>。在 by 后面的表达式是该 委托， 因为属性对应的 get()（与 set()）
// 会被委托给它的 getValue() 与 setValue() 方法。 属性的委托不必实现任何的接口，但是需要提供一个 getValue()
// 函数（与 setValue()——对于 var 属性）。 例如:
class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}


class Example() {
    var p: String by Delegate()
}

//标准委托

// Kotlin 标准库为几种有用的委托提供了工厂方法。
//延迟属性 Lazy
//lazy() 是接受一个 lambda 并返回一个 Lazy <T> 实例的函数，返回的实例可以作为实现延迟属性的委托：
// 第一次调用 get() 会执行已传递给 lazy() 的 lambda 表达式并记录结果， 后续调用 get() 只是返回记录的结果。

//默认情况下，对于 lazy 属性的求值是同步锁的（synchronized）：该值只在一个线程中计算，并且所有线程会看到相同的值。
// 如果初始化委托的同步锁不是必需的，这样多个线程可以同时执行，那么将 LazyThreadSafetyMode.PUBLICATION
// 作为参数传递给 lazy() 函数。 而如果你确定初始化将总是发生在与属性使用位于相同的线程， 那么可以使用
// LazyThreadSafetyMode.NONE 模式：它不会有任何线程安全的保证以及相关的开销。
val lazyValue: String by lazy() {
    println("computed")
    "hello"
}


//可观察属性


//Delegates.observable() 接受两个参数：初始值与修改时处理程序（handler）。 每当我们给属性赋值时会调用该处理程序
// （在赋值后执行）。它有三个参数：被赋值的属性、旧值与新值：
class User {
    var name: String by Delegates.observable("<no name>") { property, oldValue, newValue ->
        println("$oldValue -> $newValue")
    }
}

// 委托给另一个属性

//从 Kotlin 1.4 开始，一个属性可以把它的 getter 与 setter 委托给另一个属性。这种委托对于顶层和类的属性（成员和扩展）都可用。该委托属性可以为：
//
//顶层属性
//同一个类的成员或扩展属性
//另一个类的成员或扩展属性
//为将一个属性委托给另一个属性，应在委托名称中使用恰当的 :: 限定符，例如，this::delegate 或 MyClass::delegate。
//

//class MyClass(var memberInt: Int, val anotherClassInstance: ClassWithDelegate) {
//    var delegatedToMember: Int by this::memberInt
//    var delegatedToTopLevel: Int by ::topLevelInt
//
//    val delegatedToAnotherClass: Int by anotherClassInstance::anotherClassInt
//}


//这是很有用的，例如，当想要以一种向后兼容的方式重命名一个属性的时候：引入一个新的属性、 使用 @Deprecated 注解来注解旧的属性、并委托其实现。

class MyClass {
    var newName: Int = 0

    @Deprecated("Use 'newName' instead", ReplaceWith("newName"))
    var oldName: Int by this::newName
}

//将属性存储在映射中

class User1(val map: Map<String, Any>) {
    val name: String by map
    val age: Int by map
}

//这也适用于 var 属性，如果把只读的 Map 换成 MutableMap 的话：

class MutableUser(val map: MutableMap<String, Any?>) {
    var name: String by map
    var age: Int     by map
}
//局部委托属性
// 你可以将局部变量声明为委托属性。 例如，你可以使一个局部变量惰性初始化：
// memoizedFoo 变量只会在第一次访问时计算。 如果 someCondition 失败，那么该变量根本不会计算。

//fun example(computeFoo: () -> Foo) {
//    val memoizedFoo by lazy(computeFoo)
//    if (someCondition && memoizedFoo.isValid()) {
//        memoizedFoo.doSomething()
//    }
//}


//属性委托要求
//这里我们总结了委托对象的要求。
//
//对于一个只读属性（即 val 声明的），委托必须提供一个操作符函数 getValue()，该函数具有以下参数：
//
//thisRef —— 必须与 属性所有者 类型（对于扩展属性——指被扩展的类型）相同或者是其超类型。
//property —— 必须是类型 KProperty<*> 或其超类型。
//getValue() 必须返回与属性相同的类型（或其子类型）。
//
//class Resource
//class Owner {
//    val valResource: Resource by ResourceDelegate()
//}
//class ResourceDelegate {
//    operator fun getValue(thisRef: Owner, property: KProperty<*>): Resource {
//        return Resource()
//    }
//}
//对于一个可变属性（即 var 声明的），委托必须额外提供一个操作符函数 setValue()， 该函数具有以下参数：
//
//thisRef —— 必须与 属性所有者 类型（对于扩展属性——指被扩展的类型）相同或者是其超类型。
//property —— 必须是类型 KProperty<*> 或其超类型。
//value — 必须与属性类型相同（或者是其超类型）。
//class Resource
//class Owner {
//    var varResource: Resource by ResourceDelegate()
//}
//class ResourceDelegate(private var resource: Resource = Resource()) {
//    operator fun getValue(thisRef: Owner, property: KProperty<*>): Resource {
//        return resource
//    }
//    operator fun setValue(thisRef: Owner, property: KProperty<*>, value: Any?) {
//        if (value is Resource) {
//            resource = value
//        }
//    }
//}


fun main() {
    val b = BaseImpl(10)
    Derived(b).printMessage()
    Derived(b).printMessageLine()


    //当我们从委托到一个 Delegate 实例的 p 读取时，将调用 Delegate 中的 getValue() 函数，
    // 所以它第一个参数是读出 p 的对象、第二个参数保存了对 p 自身的描述 （例如你可以取它的名字)。 例如:
    val e = Example()
    println(e.p)

    //类似地，当我们给 p 赋值时，将调用 setValue() 函数。前两个参数相同，第三个参数保存将要被赋予的值：
    e.p = "NEW"

    //输出
    //NEW has been assigned to ‘p’ in Example@33a17727.


    //-----------------------------------------------------------------------------------
    print(lazyValue)
    val user = User()
    user.name = "first"
    user.name = "sss"
    //-----------------------------------------------------------------------------------
    val myClass = MyClass()
    // 通知：'oldName: Int' is deprecated.
    // Use 'newName' instead
    myClass.oldName = 42
    println(myClass.newName) // 42

    //-----------------------------------------------------------------------------------
    val user1 = User1(
        mapOf(
            "name" to "John Doe",
            "age" to 24
        )
    )
    println(user1.name) // Prints "John Doe"
    println(user1.age)  // Prints 25

}
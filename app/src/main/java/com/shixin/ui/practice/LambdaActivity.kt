package com.shixin.ui.practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.shixin.R
import kotlinx.android.synthetic.main.activity_lambda.*
import java.util.Collections.max


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

val tests = fun(a: Int, b: Int):Int {
    return a + b
}

val sum1: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
val sum = { x: Int, y: Int -> (x + y) }



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
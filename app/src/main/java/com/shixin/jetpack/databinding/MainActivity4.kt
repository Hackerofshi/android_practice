package com.shixin.jetpack.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.shixin.rxjava.R
import com.shixin.rxjava.databinding.ActivityMain4Binding
import kotlin.random.Random

/**
 * 作者：leavesC
 * 时间：2020/6/29 22:47
 * 描述：
 * GitHub：https://github.com/leavesC
 */
class MainActivity4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMain4Binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main4)
        val observableGoodsBean = ObservableGoodsBean("code", "hello", 25F)
        binding.observableGoods = observableGoodsBean
        binding.observableGoodsHandler = ObservableGoodsHandler(observableGoodsBean)
    }

    class ObservableGoodsHandler(private val observableGoodsBean: ObservableGoodsBean) {

        fun changeGoodsName() {
            observableGoodsBean.name.set("name " + Random.nextInt(100))
        }

        fun changeGoodsDetails() {
            observableGoodsBean.details.set("detail" + Random.nextInt(100))
            observableGoodsBean.price.set(Random.nextFloat())
        }
    }

}
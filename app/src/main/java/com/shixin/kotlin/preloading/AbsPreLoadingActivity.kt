package com.shixin.kotlin.preloading

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import kotlin.reflect.KClass

abstract class AbsPreLoadingActivity : Activity() {
    abstract fun preLoadingViewClass() : KClass<out AbsPreLoadingView>

    private lateinit var  preLoadingView: AbsPreLoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preLoadingView =preLoadingViewClass().getOrCreate(intent, this)
        setContentView(preLoadingView)
        preLoadingView.callCreate(savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        preLoadingView.callStart()
    }

    override fun onResume() {
        super.onResume()
        preLoadingView.callResume()
    }

    override fun onPause() {
        super.onPause()
        preLoadingView.callPause()
    }

    override fun onStop() {
        super.onStop()
        preLoadingView.callStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        preLoadingView.callDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        preLoadingView.onActivityResult(requestCode, resultCode, data)
    }
}

object PreLoading {

    private  val handler: Handler

    init {
        val ht = HandlerThread("ui_async")
        ht.start()
        handler = Handler(ht.looper)
    }

    fun <T : AbsPreLoadingView> preLoading(
        context: Context, intent: Intent, clazz: Class<T>, autoDestroy:Boolean = true) {

        val cacheView = AbsPreLoadingView.getCache(intent)

        if (cacheView != null) {
            return
        }

        handler.post {
            val instance = clazz.getConstructor(Context::class.java).newInstance(context)
            instance.attach(intent,autoDestroy)
            instance.callCreate(null)
            instance.callStart()
            instance.callResume()
            instance.callPause()
            instance.isPreLoaded = true
            AbsPreLoadingView.putCache(intent, instance)
        }

    }

    fun removePreLoading( intent: Intent){
        AbsPreLoadingView.remove(intent)
    }

}

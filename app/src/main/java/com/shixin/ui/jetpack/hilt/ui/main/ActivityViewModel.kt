package com.shixin.ui.jetpack.hilt.ui.main

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shixin.ui.jetpack.hilt.repo.Repository
import io.reactivex.Observable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import io.reactivex.ObservableEmitter

import io.reactivex.ObservableOnSubscribe




class ActivityViewModel @ViewModelInject constructor(
        val repository: Repository,
        @Assisted private val savedState: SavedStateHandle
) : ViewModel(){

    val sharedFlow= MutableSharedFlow<String>()

    fun test(){
        Log.i("TAG", "test: 测试")
        viewModelScope.launch{
            sharedFlow.emit("Hello")
            sharedFlow.emit("SharedFlow")
        }
    }

    fun test1(){
        //创建被观察者
        val observable: Observable<Int?> = Observable.create(
            ObservableOnSubscribe<Int?> { e ->
                e.onNext(1)
                e.onNext(2)
                e.onComplete()
                e.onNext(3)
                //e.onError(new Throwable());
            })
    }

}
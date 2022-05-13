package com.shixin.ui.jetpack.hilt.ui.main

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shixin.ui.jetpack.hilt.repo.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ActivityViewModel @Inject
constructor(
    val repository: Repository,
) : ViewModel() {

    val sharedFlow = MutableSharedFlow<String>()

    fun test() {
        Log.i("TAG", "test: 测试")
        viewModelScope.launch {
            sharedFlow.emit("Hello")
            sharedFlow.emit("SharedFlow")
        }
    }

    fun test1() {
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
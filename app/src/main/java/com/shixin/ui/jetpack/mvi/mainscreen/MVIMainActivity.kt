package com.shixin.ui.jetpack.mvi.mainscreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.shixin.R
import com.shixin.ui.jetpack.mvi.mockapi.MockApi
import com.shixin.ui.jetpack.mvi.utils.FetchStatus
import com.shixin.ui.jetpack.mvi.utils.observeState
import com.shixin.ui.jetpack.mvi.utils.toast
import kotlinx.android.synthetic.main.activity_mvi_main.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


//https://juejin.cn/post/7027815347281477645#heading-9
class MVIMainActivity  : AppCompatActivity()  {
    private val viewModel: MainViewModel by viewModels()
    private val mViewModel by viewModels<ApiViewModel>()
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvi_main)
        initView()
        initViewModel()
    }
    private fun initView() {
        //rvNewsHome.adapter = newsRvAdapter

        srlNewsHome.setOnRefreshListener {
            viewModel.dispatch(MainViewAction.OnSwipeRefresh)
        }

        fabStar.setOnClickListener {
            viewModel.dispatch(MainViewAction.FabClicked)
        }

    }

    @Throws(IOException::class)
    fun post(url: String?, json: String) {
        val body: RequestBody = json.toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url(url?:"")
            .post(body)
            .build()
       MockApi.createOkHttp().newCall(request).enqueue(object :Callback{
           override fun onFailure(call: Call, e: IOException) {
           }

           override fun onResponse(call: Call, response: Response) {
           }
       })
    }

    private fun initViewModel() {
        viewModel.viewStates.run {
            observeState(this@MVIMainActivity, MainViewState::newsList) {
               // newsRvAdapter.submitList(it)
            }
            observeState(this@MVIMainActivity, MainViewState::fetchStatus) {
                when (it) {
                    is FetchStatus.Fetched -> {
                        srlNewsHome.isRefreshing = false
                    }
                    is FetchStatus.NotFetched -> {
                        viewModel.dispatch(MainViewAction.FetchNews)
                        srlNewsHome.isRefreshing = false
                    }
                    is FetchStatus.Fetching -> {
                        srlNewsHome.isRefreshing = true
                    }
                }
            }
        }
        viewModel.viewEvents.observe(this) {
            renderViewEvent(it)
        }
    }
    private fun renderViewEvent(viewEvent: MainViewEvent) {
        when (viewEvent) {
            is MainViewEvent.ShowSnackbar -> {
                Snackbar.make(coordinatorLayoutRoot, viewEvent.message, Snackbar.LENGTH_SHORT)
                    .show()
            }
            is MainViewEvent.ShowToast -> {
                toast(message = viewEvent.message)
            }
        }
    }


    private fun initObserver() {
        mViewModel.wxArticleLiveData.observeState(this) {
            onSuccess { data: List<Any> ->
                showNetErrorPic(false)
               // mBinding.tvContent.text = data.toString()
            }

            onFailed { code, msg ->
            }

            onException {
                showNetErrorPic(true)
            }

            onEmpty {
            }

            onComplete {
               // dismissLoading()
            }
        }

        mViewModel.mediatorLiveDataLiveData.observe(this) {
            showNetErrorPic(false)
            //mBinding.tvContent.text = it.data.toString()
        }

        mViewModel.userLiveData.observeState(this) {
            onSuccess {
               // mBinding.tvContent.text = it.toString()
            }

//            onComplete {
            //自带loading，不需要手动取消loading
//                dismissLoading()
//            }
        }
    }

    private fun showNetErrorPic(isShowError: Boolean) {
        //mBinding.tvContent.isGone = isShowError
        //mBinding.ivContent.isVisible = isShowError
    }


}
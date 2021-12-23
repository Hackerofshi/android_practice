package com.shixin.ui.jetpack.mvi.mainscreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.shixin.R
import com.shixin.ui.jetpack.mvi.utils.FetchStatus
import com.shixin.ui.jetpack.mvi.utils.observeState
import com.shixin.ui.jetpack.mvi.utils.toast
import kotlinx.android.synthetic.main.activity_mvi_main.*



//https://juejin.cn/post/7027815347281477645#heading-9
class MVIMainActivity  : AppCompatActivity()  {
    private val viewModel: MainViewModel by viewModels()

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
}
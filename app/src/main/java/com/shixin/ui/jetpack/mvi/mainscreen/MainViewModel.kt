package com.shixin.ui.jetpack.mvi.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.shixin.ui.jetpack.mvi.repository.NewsItem
import com.shixin.ui.jetpack.mvi.repository.NewsRepository
import com.shixin.ui.jetpack.mvi.utils.*
import kotlinx.coroutines.flow.collect

class MainViewModel : ViewModel() {
    private var count: Int = 0
    private val repository: NewsRepository = NewsRepository.getInstance()
    private val _viewStates: MutableLiveData<MainViewState> = MutableLiveData(MainViewState())
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: SingleLiveEvent<MainViewEvent> = SingleLiveEvent() //一次性的事件，与页面状态分开管理
    val viewEvents = _viewEvents.asLiveData()

    fun dispatch(viewAction: MainViewAction) {
        when (viewAction) {
            is MainViewAction.NewsItemClicked -> newsItemClicked(viewAction.newsItem)
            MainViewAction.FabClicked -> fabClicked()
            MainViewAction.OnSwipeRefresh -> fetchNews()
            MainViewAction.FetchNews -> fetchNews()
        }
    }

    private fun newsItemClicked(newsItem: NewsItem) {
        _viewEvents.setEvent(MainViewEvent.ShowSnackbar(newsItem.title))
    }

    private fun fabClicked() {
        count++
        _viewEvents.setEvent(MainViewEvent.ShowToast(message = "Fab clicked count $count"))
    }

    private fun fetchNews() {
        _viewStates.setState {
            copy(fetchStatus = FetchStatus.Fetching)
        }
        viewModelScope.launch {
            when (val result = repository.getMockApiResponse()) {
                is PageState.Error -> {
                    _viewStates.setState {
                        copy(fetchStatus = FetchStatus.Fetched)
                    }
                    _viewEvents.setEvent(MainViewEvent.ShowToast(message = result.message))
                }
                is PageState.Success -> {
                    _viewStates.setState {
                        copy(fetchStatus = FetchStatus.Fetched, newsList = result.data)
                    }
                }
            }
        }



        viewModelScope.launch {
            repository.getMockApiResponse1().collect {
                when(it){
                    is PageState.Error -> {
                        _viewStates.setState {
                            copy(fetchStatus = FetchStatus.Fetched)
                        }
                        _viewEvents.setEvent(MainViewEvent.ShowToast(message = it.message))
                    }
                    is PageState.Success -> {
                        _viewStates.setState {
                            copy(fetchStatus = FetchStatus.Fetched, newsList = it.data)
                        }
                    }
                }
            }
        }


    }
}
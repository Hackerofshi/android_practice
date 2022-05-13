package com.shixin.ui.jetpack.hilt.ui.main


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shixin.ui.jetpack.hilt.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentViewModel @Inject constructor(
    val repository: Repository,
    private val savedState: SavedStateHandle
) : ViewModel()
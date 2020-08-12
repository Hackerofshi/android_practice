package com.shixin.jetpack.hilt.ui.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shixin.jetpack.hilt.repo.Repository

class FragmentViewModel @ViewModelInject constructor(
        val repository: Repository,
        @Assisted private val savedState: SavedStateHandle
) : ViewModel()
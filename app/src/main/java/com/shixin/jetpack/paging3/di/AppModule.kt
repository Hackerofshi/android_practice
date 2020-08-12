package com.shixin.jetpack.paging3.di


import com.shixin.jetpack.paging3.data.RepositoryFactory
import com.shixin.jetpack.paging3.data.remote.GitHubService
import com.shixin.jetpack.paging3.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/6/20
 *     desc  :
 * </pre>
 */

val viewModele = module {
    viewModel { MainViewModel(get()) }
}

val repoModule = module {
    single { RepositoryFactory(get()).makeGitHubRepository() }
}

val remodeModule = module {
    single { GitHubService.create() }
}

val appModule = listOf(viewModele, repoModule, remodeModule)
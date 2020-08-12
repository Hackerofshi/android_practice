package com.shixin.jetpack.paging3.data

import androidx.paging.PagingConfig
import com.shixin.jetpack.paging3.data.mapper.Model2GitHubMapper
import com.shixin.jetpack.paging3.data.remote.GitHubService
import com.shixin.jetpack.paging3.data.repository.GitHubRepositoryImpl
import com.shixin.jetpack.paging3.data.repository.Repository

class RepositoryFactory(
        val gitHubApi: GitHubService
) {
    fun makeGitHubRepository(): Repository = GitHubRepositoryImpl(
            pagingConfig,
            gitHubApi,
            Model2GitHubMapper()
    );

    val pagingConfig = PagingConfig(
            // 每页显示的数据的大小
            pageSize = 30,

            // 开启占位符
            enablePlaceholders = false

            // 预刷新的距离，距离最后一个 item 多远时加载数据
//        prefetchDistance = 3,

            /**
             * 初始化加载数量，默认为 pageSize * 3
             *
             * internal const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 3
             * val initialLoadSize: Int = pageSize * DEFAULT_INITIAL_PAGE_MULTIPLIER
             */

            /**
             * 初始化加载数量，默认为 pageSize * 3
             *
             * internal const val DEFAULT_INITIAL_PAGE_MULTIPLIER = 3
             * val initialLoadSize: Int = pageSize * DEFAULT_INITIAL_PAGE_MULTIPLIER
             */
//        initialLoadSize = 60
    )
}
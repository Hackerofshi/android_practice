package com.shixin.ui.jetpack.paging3.data.repository

import androidx.paging.*
import com.shixin.bean.GitHubAccount
import com.shixin.ui.jetpack.paging3.data.mapper.Mapper
import com.shixin.ui.jetpack.paging3.data.mediator.CustomRemoteMediator
import com.shixin.ui.jetpack.paging3.data.remote.GitHubService
import com.shixin.ui.jetpack.paging3.data.remote.GithubAccountModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@OptIn(androidx.paging.ExperimentalPagingApi::class)
class GitHubRepositoryImpl(
    private val pageConfig: PagingConfig,
    private val githubApi: GitHubService,
    private val mapper2Person: Mapper<GithubAccountModel, GitHubAccount>
) : Repository {
    override fun postOfData(id: Int): Flow<PagingData<GitHubAccount>> {
        return Pager(pageConfig, remoteMediator = CustomRemoteMediator(githubApi)) {
            //加载数据
            GitHubItemPagingSource(githubApi)
        }.flow.map { pagingData ->
            // 数据映射，数据源 GithubAccountModel ——>  上层用到的 GitHubAccount
            pagingData.map { mapper2Person.map(it) }
        }
    }
}
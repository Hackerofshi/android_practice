package com.shixin.ui.jetpack.paging3.data.mapper

import com.shixin.bean.GitHubAccount
import com.shixin.ui.jetpack.paging3.data.remote.GithubAccountModel


/**
 * <pre>
 *     author: dhl
 *     date  : 2020/6/20
 *     desc  : 数据映射， 上层用到的实体 GithubAccount ——> 数据库实体 GithubAccountModel
 * </pre>
 */
class GitHub2ModelMapper : Mapper<GitHubAccount, GithubAccountModel> {
    override fun map(input: GitHubAccount): GithubAccountModel =
        GithubAccountModel(
            input.id,
            input.login,
            input.node_id,
            input.avatar_url,
            input.gravatar_id,
            input.url,
            input.html_url,
            input.followers_url,
            input.following_url,
            input.gists_url,
            input.starred_url,
            input.subscriptions_url,
            input.organizations_url,
            input.repos_url,
            input.events_url,
            input.received_events_url,
            input.type,
            input.isSite_admin
        )
}
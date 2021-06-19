package com.shixin.ui.jetpack.paging3.data.mapper

import com.shixin.bean.GitHubAccount
import com.shixin.ui.jetpack.paging3.data.remote.GithubAccountModel

class Model2GitHubMapper : Mapper<GithubAccountModel, GitHubAccount> {
    override fun map(input: GithubAccountModel): GitHubAccount =
            GitHubAccount(
                    input.login,
                    input.id,
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
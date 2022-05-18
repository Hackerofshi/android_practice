package com.shixin.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shixin.ui.jetpack.paging3.data.remote.GithubAccountModel

@Dao
interface  GithubAccountModelDao {
    @Query("select * from githubaccountmodel")
    fun getMessage(): PagingSource<Int, GithubAccountModel>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    fun insertMessage(messages: List<GithubAccountModel>)

    @Query("delete from githubaccountmodel")
    fun clearMessage()
}
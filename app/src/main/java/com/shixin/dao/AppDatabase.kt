package com.shixin.dao

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shixin.ui.jetpack.paging3.data.mapper.Message
import com.shixin.ui.jetpack.paging3.data.remote.GithubAccountModel

/**
 *
 * @ProjectName:    JuLang
 * @Package:        com.tech.julang.data.dao
 * @ClassName:      AppDatabase
 * @Description:     java类作用描述
 * @Author:         shixin
 * @CreateDate:     2021/7/27 20:46
 * @UpdateUser:     shixin：
 * @UpdateDate:     2021/7/27 20:46
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
@Database(
    entities = arrayOf(
        Message::class,
        GithubAccountModel::class,
    ), version = 1
)
@TypeConverters()
abstract class AppDatabase() : RoomDatabase() {
    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

    abstract fun messageDao(): GithubAccountModelDao

    /**
     * Check whether the database already exists and expose it via [.getDatabaseCreated]
     */
    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }

    fun getDatabaseCreated(): LiveData<Boolean> {
        return mIsDatabaseCreated
    }

    companion object {
        private var sInstance: AppDatabase? = null

        @VisibleForTesting
        val DATABASE_NAME = "basic-db"
        fun getInstance(context: Context): AppDatabase? {
            if (sInstance == null) {
                synchronized(AppDatabase::class.java) {
                    if (sInstance == null) {
                        sInstance = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java, DATABASE_NAME
                        ).build()
                    }
                }
            }
            return sInstance
        }
    }
}
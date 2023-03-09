package com.example.rewardapp.di

import android.app.Application
import androidx.room.Room
import com.example.rewardapp.database.RewardDao
import com.example.rewardapp.database.iTdataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun DataAccessObject(database: iTdataBase): RewardDao {
        return database.rewardDao()
    }

    @Singleton
    @Provides
    fun providedatabase(
        app: Application,
        callBack: iTdataBase.Callback
    ): iTdataBase = Room.databaseBuilder(
        app,
        iTdataBase::class.java,
        "it_Database"
    ).addCallback(callBack).build()

    @ApplicationScope
    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

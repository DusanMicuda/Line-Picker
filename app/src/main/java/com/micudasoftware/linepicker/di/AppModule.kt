package com.micudasoftware.linepicker.di

import android.app.Application
import androidx.room.Room
import com.micudasoftware.linepicker.db.DictionaryDAO
import com.micudasoftware.linepicker.db.DictionaryDatabase
import com.micudasoftware.linepicker.other.Constants.DICTIONARY_DATABASE_NAME
import com.micudasoftware.linepicker.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDictionaryDatabase(
        app: Application
    ) = Room.databaseBuilder(
        app,
        DictionaryDatabase::class.java,
        DICTIONARY_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideTrackDao(db: DictionaryDatabase) = db.getDictionaryDao()

    @Singleton
    @Provides
    fun provideMainRepository(dictionaryDAO: DictionaryDAO) = MainRepositoryImpl(dictionaryDAO)
}
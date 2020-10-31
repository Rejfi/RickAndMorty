package com.example.rickandmorty.di

import android.content.Context
import com.example.rickandmorty.data.local.character.CharacterDatabase
import com.example.rickandmorty.data.local.character.CharacterDao
import com.example.rickandmorty.data.local.episode.EpisodeDao
import com.example.rickandmorty.data.local.episode.EpisodeDatabase
import com.example.rickandmorty.data.remote.CharacterEpisodeRemoteDataSource
import com.example.rickandmorty.data.remote.CharacterService
import com.example.rickandmorty.data.repository.CharacterRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCharacterService(retrofit: Retrofit): CharacterService = retrofit.create(CharacterService::class.java)

    @Singleton
    @Provides
    fun provideCharacterRemoteDataSource(characterService: CharacterService) = CharacterEpisodeRemoteDataSource(characterService)

    @Singleton
    @Provides
    fun provideCharacterDatabase(@ApplicationContext appContext: Context) = CharacterDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCharacterDao(db: CharacterDatabase) = db.characterDao()

    @Singleton
    @Provides
    fun provideEpisodeDatabase(@ApplicationContext appContext: Context) = EpisodeDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideEpisodeDao(db: EpisodeDatabase) = db.episodeDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: CharacterEpisodeRemoteDataSource,
                          characterLocalDataSource: CharacterDao,
                          episodeLocalDataSource: EpisodeDao) =
        CharacterRepository(remoteDataSource, characterLocalDataSource, episodeLocalDataSource)
}
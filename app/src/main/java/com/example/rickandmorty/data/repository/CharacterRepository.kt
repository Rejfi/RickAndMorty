package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.data.local.character.CharacterDao
import com.example.rickandmorty.data.local.episode.EpisodeDao
import com.example.rickandmorty.data.remote.CharacterEpisodeRemoteDataSource
import com.example.rickandmorty.utils.performGetOperation
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val remoteDataSource: CharacterEpisodeRemoteDataSource,
    private val characterLocalDataSource: CharacterDao,
    private val episodeLocalDataSource: EpisodeDao
) {

    fun getCharacter(id: Int) = performGetOperation(
        databaseQuery = { characterLocalDataSource.getCharacter(id) },
        networkCall = { remoteDataSource.getCharacter(id) },
        saveCallResult = { characterLocalDataSource.insert(it) }
    )

    fun getCharacters() = performGetOperation(
        databaseQuery = { characterLocalDataSource.getAllCharacters() },
        networkCall = { remoteDataSource.getCharacters() },
        saveCallResult = { characterLocalDataSource.insertAll(it.results) }
    )

    fun getEpisodes() = performGetOperation(
        databaseQuery = {episodeLocalDataSource.getEpisodes()},
        networkCall = {remoteDataSource.getEpisodes()},
        saveCallResult = {episodeLocalDataSource.insertAll(it.results)}

    )

    fun getEpisode(id: Int) = performGetOperation(
        databaseQuery = {episodeLocalDataSource.getEpisode(id)},
        networkCall = {remoteDataSource.getEpisode(id)},
        saveCallResult = {episodeLocalDataSource.getEpisode(id)}
    )

    fun updateCharacter(character: Character){
        characterLocalDataSource.updateCharacter(character)
    }
}
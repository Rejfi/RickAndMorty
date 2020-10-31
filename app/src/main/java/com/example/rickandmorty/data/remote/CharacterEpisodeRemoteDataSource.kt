package com.example.rickandmorty.data.remote

import javax.inject.Inject

class CharacterEpisodeRemoteDataSource @Inject constructor(
    private val characterService: CharacterService
): BaseDataSource() {

    suspend fun getCharacters() = getResult { characterService.getCharacters() }
    suspend fun getCharacter(id: Int) = getResult { characterService.getCharacter(id) }
    suspend fun getEpisodes() = getResult { characterService.getEpisodes() }
    suspend fun getEpisode(id: Int) = getResult { characterService.getCharacter(id) }
}
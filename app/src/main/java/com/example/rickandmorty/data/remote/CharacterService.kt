package com.example.rickandmorty.data.remote

import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.data.entities.character.CharacterList
import com.example.rickandmorty.data.entities.episode.EpisodeList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterService {
    @GET("character")
    suspend fun getCharacters() : Response<CharacterList>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<Character>

    @GET("episode")
    suspend fun getEpisodes(): Response<EpisodeList>
}
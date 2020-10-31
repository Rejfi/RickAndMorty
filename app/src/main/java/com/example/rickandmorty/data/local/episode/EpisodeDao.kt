package com.example.rickandmorty.data.local.episode

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.data.entities.episode.Episode

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM episodes")
    fun getEpisodes() : LiveData<List<Episode>>

    @Query("SELECT * FROM episodes WHERE id = :id")
    fun getEpisode(id: Int): LiveData<Episode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episode: List<Episode>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episode: Episode)


}
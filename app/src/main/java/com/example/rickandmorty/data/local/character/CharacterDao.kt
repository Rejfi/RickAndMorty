package com.example.rickandmorty.data.local.character

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rickandmorty.data.entities.character.Character

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY isFavourite DESC")
    fun getAllCharacters() : LiveData<List<Character>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacter(id: Int): LiveData<Character>

    @Update
    fun updateCharacter(character: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)


}
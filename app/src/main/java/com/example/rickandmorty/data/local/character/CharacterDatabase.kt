package com.example.rickandmorty.data.local.character

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.utils.RoomConverter

@Database(entities = [Character::class], version = 3, exportSchema = false)
@TypeConverters(RoomConverter::class)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile private var instance: CharacterDatabase? = null

        fun getDatabase(context: Context): CharacterDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, CharacterDatabase::class.java, "characters")
                .fallbackToDestructiveMigration()
                .build()
    }

}
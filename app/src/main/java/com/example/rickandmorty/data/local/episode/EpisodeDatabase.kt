package com.example.rickandmorty.data.local.episode

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.data.entities.episode.Episode
import com.example.rickandmorty.data.local.character.CharacterDao
import com.example.rickandmorty.utils.RoomConverter

@Database(entities = [Episode::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverter::class)
abstract class EpisodeDatabase : RoomDatabase() {

    abstract fun episodeDao(): EpisodeDao

    companion object {
        @Volatile private var instance: EpisodeDatabase? = null

        fun getDatabase(context: Context): EpisodeDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, EpisodeDatabase::class.java, "episodes")
                .fallbackToDestructiveMigration()
                .build()
    }

}
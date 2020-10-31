package com.example.rickandmorty.utils

import androidx.room.TypeConverter

/**
 * Class contains methods to convert data in Room.
 * List -> String -> List
 * Every record in list is convert to String and ended with ";" as END OF LINE
 */

class RoomConverter {

    @TypeConverter
    fun fromListToString(list: List<String>?): String? {
        var result = ""
        list?.forEach{ text ->
            result += "$text;"
        }
        return result
    }

    @TypeConverter
    fun fromStringToList(text: String): List<String>? {
        val arrayList = ArrayList<String>()
        var tempEpisode = ""

        for (i in text){
            if(i != ';') tempEpisode += i
            else{
                arrayList.add(tempEpisode)
                tempEpisode = ""
            }
        }

        return arrayList
    }

}
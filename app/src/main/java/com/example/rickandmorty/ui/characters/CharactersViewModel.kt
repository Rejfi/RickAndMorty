package com.example.rickandmorty.ui.characters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.data.repository.CharacterRepository

class CharactersViewModel @ViewModelInject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    val characters = repository.getCharacters()
    val episodes = repository.getEpisodes()

    /*
    private val _favouriteIds: MutableLiveData<List<Int>> = MutableLiveData()
    val favouriteIds : LiveData<List<Int>> = _favouriteIds
    fun setFavouriteIds(list: List<Int>){
        _favouriteIds.postValue(list)
    }

     */

}

package com.example.rickandmorty.ui.characterfavourite

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.utils.Resource

class CharactersFavouriteViewModel @ViewModelInject
constructor(
    private val repository: CharacterRepository
): ViewModel() {

    val characters = repository.getCharacters()
    val episodes = repository.getEpisodes()

    private val _favouriteIds: MutableLiveData<List<Int>> = MutableLiveData()
    val favouriteIds : LiveData<List<Int>> = _favouriteIds
    fun setFavouriteIds(list: List<Int>){
        _favouriteIds.postValue(list)
    }



}
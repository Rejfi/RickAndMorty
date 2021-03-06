package com.example.rickandmorty.ui.characterdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch

class CharacterDetailViewModel @ViewModelInject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _id = MutableLiveData<Int>()

    private val _character = _id.switchMap { id ->
        repository.getCharacter(id)
    }
    val character: LiveData<Resource<Character>> = _character
    fun start(id: Int) {
        _id.value = id
    }

    private val _favouriteIds: MutableLiveData<List<Int>> = MutableLiveData()
    val favouriteIds : LiveData<List<Int>> = _favouriteIds
    fun setFavouriteIds(list: List<Int>){
        _favouriteIds.postValue(list)
    }

    fun updateCharacter(character: Character){
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateCharacter(character)
        }
    }

}

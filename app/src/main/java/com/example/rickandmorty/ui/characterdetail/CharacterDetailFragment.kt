package com.example.rickandmorty.ui.characterdetail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.rickandmorty.R
import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.utils.Resource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.character_detail_fragment.*

@AndroidEntryPoint
class CharacterDetailFragment : Fragment() {
    private val DETAIL_DEBUG = "DETAIL_DEBUG"

    private val viewModel: CharacterDetailViewModel by viewModels()
    private lateinit var shPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.character_detail_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shPref = requireActivity().getSharedPreferences("favourite", Context.MODE_PRIVATE)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("id")?.let { viewModel.start(it) }
        getFavouriteIds()
        setupObservers()

        like_button_details.setOnClickListener {
            val character = viewModel.character.value?.data!!
            setFavouriteCharacterId(!character.isFavourite, character)

        }
    }

    private fun getFavouriteIds(){
        try {
            val mutMap = shPref.all as Map<String, Int>
            val listIds = mutMap.values.toList()
            viewModel.setFavouriteIds(listIds)
        }catch (t: Throwable){
            Log.d(DETAIL_DEBUG, t.message!!)
        }
    }

    private fun showHint(add: Boolean) {
        if(add) Snackbar.make(requireView(), "Added to Favourite", Snackbar.LENGTH_SHORT).show()
        else Snackbar.make(requireView(), "Remove from Favourite", Snackbar.LENGTH_SHORT).show()
    }

    private fun setFavouriteCharacterId(add: Boolean, character: Character){
        if(add){
            shPref.edit().apply {
                putInt("fav:${character.id}", character.id)
                apply()
                showHint(true)
            }
        }else{
            shPref.edit().apply {
                remove("fav:${character.id}")
                apply()
                showHint(false)
            }
        }
    }

    private fun setupObservers() {
        viewModel.character.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if(viewModel.favouriteIds.value!!.contains(it.data?.id)){
                        it.data?.isFavourite = true
                    }
                    bindCharacter(it.data!!)
                }

                Resource.Status.ERROR ->
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING -> {
                   //Progress bar -> future feature :)
                }
            }
        })
    }

    private fun bindCharacter(character: Character) {
        name_details.text = character.name
        species_details.text = (character.species)
        status_details.text = (character.status)
        gender_details.text = (character.gender)
        Glide.with(this)
            .load(character.image)
            //.transform(CircleCrop())
            .into(image_details)
        episode_details.text = (character.episode.toString().replace(Regex.fromLiteral("https://rickandmortyapi.com/api/episode/"), "").trim('[', ']'))
        if(character.isFavourite) like_button_details.setImageResource(R.drawable.ic_delete)
        else like_button_details.setImageResource(R.drawable.ic_favorite)
    }
}

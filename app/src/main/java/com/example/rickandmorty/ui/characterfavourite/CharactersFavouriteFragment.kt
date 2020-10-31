package com.example.rickandmorty.ui.characterfavourite

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.ConfigurationCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.BuildConfig
import com.example.rickandmorty.R
import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.ui.characters.CharactersAdapter
import com.example.rickandmorty.ui.characters.OnCharacterClickListener
import com.example.rickandmorty.ui.dialog.FilterFragmentDialog
import com.example.rickandmorty.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.characters_favourite_fragment.*
import kotlinx.android.synthetic.main.characters_fragment.*

@AndroidEntryPoint
class CharactersFavouriteFragment : Fragment(), OnCharacterClickListener {
    private val FAV_DEBUG = "FAV_DEBUG"

    private val viewModel: CharactersFavouriteViewModel by viewModels()
    private lateinit var shPref: SharedPreferences
    private val adapter = CharactersAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.characters_favourite_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shPref = requireActivity().getSharedPreferences("favourite", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        getFavouriteIds()
        setupRecyclerView()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    private fun setupRecyclerView() {
        val orientation = resources.configuration.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) recyclerView_fav.layoutManager = GridLayoutManager(requireContext(), 3)
        else recyclerView_fav.layoutManager = GridLayoutManager(requireContext(), 4)
    }

    private fun setupAdapter(list: List<Character>?){
        this.adapter.setAdapterList(list)
        recyclerView_fav.adapter = this.adapter
    }

    private fun setupObservers() {
        viewModel.characters.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()){
                        //Filter all characters to find favourite characters
                        val list = it.data.filter {
                                character -> viewModel.favouriteIds.value!!.contains(character.id)
                        }
                        setupAdapter(list)
                    }
                }
                Resource.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING ->{}

            }
        })
    }

    private fun getFavouriteIds(){
        try {
            val mutMap = shPref.all as Map<String, Int>
            val listIds = mutMap.values.toList()
            viewModel.setFavouriteIds(listIds)
        }catch (t: Throwable){
            Log.d(FAV_DEBUG, t.message!!)
        }
    }

    override fun onCharacterClick(character: Character, position: Int) {
        findNavController().navigate(R.id.action_charactersFavouriteFragment_to_characterDetailFragment,
            bundleOf("id" to character.id))
    }

    override fun onCharacterLongClick(character: Character, position: Int) {

    }
}
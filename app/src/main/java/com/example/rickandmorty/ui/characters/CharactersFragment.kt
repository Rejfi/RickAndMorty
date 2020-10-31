package com.example.rickandmorty.ui.characters

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.data.entities.character.Character
import com.example.rickandmorty.ui.dialog.FilterFragmentDialog
import com.example.rickandmorty.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.characters_favourite_fragment.*
import kotlinx.android.synthetic.main.characters_fragment.*
import kotlinx.android.synthetic.main.characters_fragment.clearFiltersCardView_home

@AndroidEntryPoint
class CharactersFragment : Fragment(), OnCharacterClickListener, FilterFragmentDialog.OnFilterDialogClickListener {
    private val CHAR_DEBUG = "CHAR_DEBUG"

    private val viewModel: CharactersViewModel by viewModels()
    private var adapter = CharactersAdapter(this)
    private lateinit var shPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.characters_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        shPref = requireActivity().getSharedPreferences("favourite", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupOnClicks()

        Log.d(CHAR_DEBUG,"Episodes: " + viewModel.episodes.value?.data.toString())
        Log.d(CHAR_DEBUG,"Characters: " +viewModel.characters.value?.data.toString())

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_view_menu, menu)
        val item = menu.findItem(R.id.search_action)
        val searchView = item.actionView as SearchView
        searchView.queryHint = "Search in all characters"


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { return false }
            override fun onQueryTextChange(newText: String): Boolean {
                searchCharacters(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.filter_action -> {
                val filterFragmentDialog = FilterFragmentDialog(viewModel.episodes.value?.data, this)
                filterFragmentDialog.show(parentFragmentManager, "FilterDialogFragment")
            }
        }
        return true
    }

    override fun onCharacterClick(character: Character, position: Int) {
        findNavController().navigate(R.id.action_charactersFragment_to_characterDetailFragment,
            bundleOf("id" to character.id))
    }

    override fun onCharacterLongClick(character: Character, position: Int) {
        //Long Click in future feature :)
    }

    override fun filterDialogClick(status: String?, episode: String?) {
        filterByEpisodesAndStatus(episode, status, viewModel.characters.value?.data)
    }


    private fun setupAdapter(list: List<Character>?){
        this.adapter.setAdapterList(list)
        recyclerView_home.adapter = this.adapter
    }
    private fun searchCharacters(searchText: String){
        val currentList = viewModel.characters.value?.data
        currentList?.let { list ->
            val result = list.filter { character -> character.name!!.toLowerCase().contains(searchText.toLowerCase()) }
            setupAdapter(result)
        }

    }
    private fun filterByEpisodesAndStatus(episodeNumber: String?,
                                          status: String?,
                                          filterList: List<Character>?) {

        val episodeFullAddress = "https://rickandmortyapi.com/api/episode/$episodeNumber"

        //In first order, if "Clear" button was clicked reset all filters and return
        if (status == null && episodeNumber == null){
            setupAdapter(viewModel.characters.value?.data!!)
            clearFiltersCardView_home.visibility = View.GONE
            return
        }

        //Status and episode weren't selected -> end filtering and return
        else if(status == "All" && episodeNumber == "All") {
            setupAdapter(viewModel.characters.value?.data!!)
            clearFiltersCardView_home.visibility = View.GONE
            return
        }

        //Status and episode were selected filter by them
        else if(status != "All" && episodeNumber != "All"){
            val beforeFilterList = viewModel.characters.value?.data
            val filteredList = beforeFilterList?.let {list ->
                list.filter { character -> character.episode.contains(episodeFullAddress) && character.status == status }
            }
            if(!filterList.isNullOrEmpty()) setupAdapter(filteredList)

            clearFiltersCardView_home.visibility = View.VISIBLE
            return
        }

        //Only status was selected
        else if (episodeNumber == "All"){
            val beforeFilterList = viewModel.characters.value?.data
            val filteredList =beforeFilterList?.let {list ->
                list.filter { character ->  character.status == status }
            }
            if(!filterList.isNullOrEmpty())  setupAdapter(filteredList)

            clearFiltersCardView_home.visibility = View.VISIBLE
            return
        }

        //Only episode was selected
        else if(status == "All"){
            val beforeFilterList = viewModel.characters.value?.data
            val filteredList =beforeFilterList?.let {list ->
                list.filter { character ->  character.episode.contains(episodeFullAddress) }
            }
            if(!filterList.isNullOrEmpty())  setupAdapter(filteredList)

            clearFiltersCardView_home.visibility = View.VISIBLE
            return
        }

    }

    private fun setupRecyclerView() {
        val orientation = resources.configuration.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) recyclerView_home.layoutManager = GridLayoutManager(requireContext(), 3)
        else recyclerView_home.layoutManager = GridLayoutManager(requireContext(), 4)
    }
    private fun setupObservers() {
        viewModel.characters.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    if (!it.data.isNullOrEmpty()) {
                        setupAdapter(it.data)
                    }
                }
                Resource.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING ->
                    progressBar.visibility = View.VISIBLE
            }
        }

        viewModel.episodes.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        Log.d(CHAR_DEBUG, it.data.toString())
                    }
                }
                Resource.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING ->{}
            }
        }
    }
    private fun setupOnClicks(){
        clearFiltersCardView_home.setOnClickListener {
            this.filterByEpisodesAndStatus(null, null, null)
        }
    }
}

package com.example.rickandmorty.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.rickandmorty.R
import com.example.rickandmorty.data.entities.episode.Episode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FilterFragmentDialog(private val listEpisodes: List<Episode>?,
                           private val listener : OnFilterDialogClickListener) : DialogFragment() {
    private val FILTER_FRAG_DEBUG = "FILTER_FRAG_DEBUG"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.filter_dialog_fragment, container, false)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.filter_dialog_fragment, null)

        val statusSpinner = view.findViewById(R.id.spinner_status) as Spinner
        //Set SpinnerAdapter for episodes options
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("All", "Alive", "Dead", "Unknown"))
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    statusSpinner.adapter = adapter
        }

        val episodeSpinner = view.findViewById(R.id.spinner_episode) as Spinner
        val listNameEpisode = ArrayList<String>().apply {
            //Add new an option "All" to list
            add(0, "All")

            //Map episode to episode's name
            listEpisodes?.forEach {episode ->
                add(episode.name)
            }
        }

        //Set SpinnerAdapter for episodes options
        listNameEpisode?.let { list ->
                ArrayAdapter(requireContext(),
                    android.R.layout.simple_spinner_item,
                    list)
                    .also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        episodeSpinner.adapter = adapter
                    }
            }

        return builder.setView(view)
            .setTitle("Filter list by")
            .setPositiveButton("Filter") { dialog, i ->
                listener.filterDialogClick(
                    statusSpinner.selectedItem as String,
                    //Return episode ID
                    if(episodeSpinner.selectedItem.toString() == "All") "All"
                    else listEpisodes?.get(episodeSpinner.selectedItemPosition-1)?.id.toString())
            }
            .setNegativeButton("Cancel"){ dialog , i -> }
            .setNeutralButton("Clear"){ dialog, i ->
                listener.filterDialogClick(null, null)
            }
            .create()
    }

    interface OnFilterDialogClickListener{
        fun filterDialogClick(status: String?, episode: String?)
    }
}
package com.example.rickandmorty.ui.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.rickandmorty.R
import com.example.rickandmorty.data.entities.character.Character


class CharactersAdapter(/*private val charactersList: List<Character>*/
                        private val listener: OnCharacterClickListener): RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    private val ADAPTER_DEBUG = "ADAPTER_DEBUG"
    private val charactersList: ArrayList<Character> = ArrayList()

    fun setAdapterList(list: List<Character>?){
        list?.let {
            charactersList.clear()
            charactersList.addAll(it)
        }
    }
    fun getAdapterList(): ArrayList<Character> {
        return charactersList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val image = holder.itemView.findViewById(R.id.character_image) as ImageView
        val name = holder.itemView.findViewById(R.id.character_name) as TextView

        Glide.with(holder.itemView)
            .load(charactersList[position].image)
            .fitCenter()
            .transform(CircleCrop())
            .placeholder(R.drawable.ic_person_black)
            .into(image)

        name.text = charactersList[position].name

    }

    override fun getItemCount(): Int {
        return charactersList.size
    }

    inner class CharacterViewHolder(view: View): RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                listener.onCharacterClick(charactersList[adapterPosition], adapterPosition)
            }
            view.setOnLongClickListener {
                listener.onCharacterLongClick(charactersList[adapterPosition], adapterPosition)
                true
            }
        }
    }
}

interface OnCharacterClickListener{
    fun onCharacterClick(character: Character, position: Int)
    fun onCharacterLongClick(character: Character, position: Int)
}

/*
class CharactersAdapter(private val listener: CharacterItemListener) : RecyclerView.Adapter<CharacterViewHolder>() {

    interface CharacterItemListener {
        fun onClickedCharacter(characterId: Int)
    }

    private val items = ArrayList<Character>()

    fun setItems(items: ArrayList<Character>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding: ItemCharacterBinding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) = holder.bind(items[position])
}

class CharacterViewHolder(private val itemBinding: ItemCharacterBinding, private val listener: CharactersAdapter.CharacterItemListener) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var character: Character

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: Character) {
        this.character = item
        itemBinding.name.text = item.name
        itemBinding.speciesAndStatus.text = """${item.species} - ${item.status}"""
        Glide.with(itemBinding.root)
            .load(item.image)
            .transform(CircleCrop())
            .into(itemBinding.image)
    }

    override fun onClick(v: View?) {
        listener.onClickedCharacter(character.id)
    }
}


 */

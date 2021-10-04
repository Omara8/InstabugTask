package com.planatech.instabugtask.main.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.planatech.instabugtask.databinding.WordItemBinding


class WordsAdapter(val words: List<Pair<String, Int>>?) :
    RecyclerView.Adapter<WordsAdapter.WordsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WordItemBinding.inflate(inflater, parent, false)
        return WordsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        val currentWord = words!![position]
        holder.binding.wordValue = currentWord.first
        holder.binding.frequency = currentWord.second.toString()
    }

    override fun getItemCount(): Int {
        return words?.size!!
    }

    class WordsViewHolder(val binding: WordItemBinding) : RecyclerView.ViewHolder(binding.root)

}
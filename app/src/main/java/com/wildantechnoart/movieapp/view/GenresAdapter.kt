package com.wildantechnoart.movieapp.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wildantechnoart.movieapp.databinding.ItemGenreBinding
import com.wildantechnoart.movieapp.model.Genres

class GenresAdapter (private val view: View) : ListAdapter<Genres, GenresAdapter.Holder>(MyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)

        with(holder.binding) {
            textGenre.text = data.name
            cardItem.setOnClickListener {
                val action = GenresFragmentDirections.actionGenresFragmentToMoviesFragment(
                    genreId = data.id.toString(),
                    genreName = data.name.toString()
                )
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyDiffCallback : DiffUtil.ItemCallback<Genres>() {
        override fun areItemsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(val binding: ItemGenreBinding) : RecyclerView.ViewHolder(binding.root)
}
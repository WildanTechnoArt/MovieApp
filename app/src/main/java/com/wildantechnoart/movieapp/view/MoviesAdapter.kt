package com.wildantechnoart.movieapp.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wildantechnoart.movieapp.R
import com.wildantechnoart.movieapp.databinding.ItemMovieBinding
import com.wildantechnoart.movieapp.model.ResultsMovie
import com.wildantechnoart.movieapp.network.RetrofitClient.BASE_URL
import com.wildantechnoart.movieapp.utils.Constant

class MoviesAdapter (private val view: View) : ListAdapter<ResultsMovie, MoviesAdapter.Holder>(MyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)
        val context = holder.itemView.context

        with(holder.binding) {
            Glide.with(context)
                .load("${Constant.IMAGE_POSTER}${data.posterPath}")
                .apply(Constant.glideRequestOption(R.drawable.baseline_image_100))
                .into(imageMovie)

            textTitle.text = data.title ?: data.originalTitle ?: "-"
            textDate.text = data.releaseDate?.take(4)
            textRating.text = String.format("%.1f", data.voteAverage ?: 0.0)

            cardItem.setOnClickListener {
                toDetailFragment(data.id.toString())
            }
            btnDetail.setOnClickListener {
                toDetailFragment(data.id.toString())
            }
        }
    }

    private fun toDetailFragment(id: String?){
        val action = MoviesFragmentDirections.actionMoviesFragmentToDetailMovieFragment(
            movieId = id.toString()
        )
        Navigation.findNavController(view).navigate(action)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyDiffCallback : DiffUtil.ItemCallback<ResultsMovie>() {
        override fun areItemsTheSame(oldItem: ResultsMovie, newItem: ResultsMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultsMovie, newItem: ResultsMovie): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)
}
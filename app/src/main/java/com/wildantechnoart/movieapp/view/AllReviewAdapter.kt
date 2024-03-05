package com.wildantechnoart.movieapp.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wildantechnoart.movieapp.R
import com.wildantechnoart.movieapp.databinding.ItemReviewBinding
import com.wildantechnoart.movieapp.databinding.ItemReviewLongBinding
import com.wildantechnoart.movieapp.model.ResultsReview
import com.wildantechnoart.movieapp.utils.Constant

class AllReviewAdapter : ListAdapter<ResultsReview, AllReviewAdapter.Holder>(MyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemReviewLongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)
        val context = holder.itemView.context

        with(holder.binding) {
            Glide.with(context)
                .load("${Constant.IMAGE_POSTER}${data.authorDetails?.avatarPath}")
                .apply(Constant.glideRequestOption(R.drawable.profile_placeholder))
                .into(imgProfile)

            val getRating: String = (data.authorDetails?.rating ?: "-").toString()
            ratingBar.text = "${getRating.take(1)}/10"
            textUsername.text = "${data.authorDetails?.username} (${data.createdAt?.take(4)})"
            textReview.text = data.content ?: "-"
            cardItem.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(it.context)
                    .apply {
                        setTitle("Review")
                        setMessage(data?.content ?: "")
                        setPositiveButton(
                            "Tutup"
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }
                    }
                builder.create()
                builder.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyDiffCallback : DiffUtil.ItemCallback<ResultsReview>() {
        override fun areItemsTheSame(oldItem: ResultsReview, newItem: ResultsReview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultsReview, newItem: ResultsReview): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(val binding: ItemReviewLongBinding) : RecyclerView.ViewHolder(binding.root)
}
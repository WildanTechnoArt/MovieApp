package com.wildantechnoart.movieapp.utils

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.wildantechnoart.movieapp.R
import com.wildantechnoart.movieapp.model.Response
import com.wildantechnoart.movieapp.network.ConnectivityStatus
import retrofit2.HttpException
import java.net.SocketTimeoutException

object Constant {
    const val TRAILER_KEY = "trailer_key"
    const val IMAGE_POSTER = "https://image.tmdb.org/t/p/w185"
    const val IMAGE_BACKDROP = "https://image.tmdb.org/t/p/w500"

    fun glideRequestOption(error: Int): RequestOptions {
        val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.PNG

        return RequestOptions()
            .encodeFormat(format)
            .placeholder(R.drawable.baseline_image_100)
            .error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    fun handleErrorApi(context: FragmentActivity, it: Throwable) {
        if (ConnectivityStatus.isConnected(context)) {
            when (it) {
                is HttpException -> {
                    try {
                        val gson = Gson()
                        val response = gson.fromJson(
                            it.response()?.errorBody()?.charStream(),
                            Response::class.java
                        )
                        val message = response?.message.toString()
                        handleSessionAndException(it, context, message)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.message_if_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is SocketTimeoutException -> // connection errors
                    Toast.makeText(
                        context, "Connection Timeout!",
                        Toast.LENGTH_SHORT
                    ).show()

                else -> {
                    Toast.makeText(
                        context, it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                context, context.getString(R.string.message_if_disconnect),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun <T, VH : RecyclerView.ViewHolder> handleData(
        page: Int?,
        takeItem: Boolean,
        data: List<T>?,
        adapter: ListAdapter<T, VH>,
        recyclerView: RecyclerView?,
        textView: MaterialTextView?
    ) {
        if (page != null) {
            val oldList = adapter.currentList
            if (page == 1) {
                adapter.submitList(data)
            } else {
                val newList = oldList.toMutableList().apply {
                    addAll(data ?: mutableListOf())
                }
                adapter.submitList(newList) {
                    recyclerView?.smoothScrollToPosition(adapter.itemCount - data?.size!!)
                }
            }
        } else {
            adapter.submitList(if (takeItem) data?.take(4) else data)
        }
        val isEmpty = data?.isEmpty() ?: false
        recyclerView?.visibility = if (isEmpty) View.GONE else View.VISIBLE
        textView?.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }


    private fun handleSessionAndException(
        t: HttpException,
        context: FragmentActivity,
        message: String?
    ) {
        if (t.code().toString() == "401") {
            Toast.makeText(
                context,
                "Maaf session anda telah berakhir",
                Toast.LENGTH_SHORT
            ).show()
            context.finishAffinity()
        } else {
            if (message != "null") {
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
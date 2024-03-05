package com.wildantechnoart.movieapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.wildantechnoart.movieapp.R
import com.wildantechnoart.movieapp.databinding.FragmentDetailBinding
import com.wildantechnoart.movieapp.utils.Constant
import com.wildantechnoart.movieapp.viewmodel.MovieViewModel
import kotlin.properties.Delegates

class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding
    private val viewModel: MovieViewModel by viewModels()
    private var mAdapter by Delegates.notNull<ReviewAdapter>()
    private var mMovieId: String? = null
    private var mIsShowTrailer = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() = with(binding) {
        mAdapter = ReviewAdapter()

        val bundle = Bundle(arguments)
        mMovieId = DetailMovieFragmentArgs.fromBundle(bundle).movieId

        this?.rvReview?.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }

        viewModel.getDetailMovie(mMovieId)
        viewModel.getReviewMovie(1, mMovieId)
        this?.swipeRefresh?.setOnRefreshListener {
            viewModel.getDetailMovie(mMovieId)
            viewModel.getReviewMovie(1, mMovieId)
        }

        this?.btnShowTrailer?.setOnClickListener {
            mIsShowTrailer = true
            viewModel.getTrailerMovie(mMovieId)
        }
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            getTrailerMovie.observe(viewLifecycleOwner) { data ->
                if(mIsShowTrailer){
                    if(data.isNotEmpty()){
                        var getTrailer = ""
                        data?.forEach {
                            if(it.type == "Trailer"){
                                getTrailer = it.key.toString()
                            }
                        }

                        val bundle = Bundle()
                        bundle.putString(Constant.TRAILER_KEY, getTrailer)

                        val intent = Intent(requireActivity(), VideoViewActivity::class.java)
                        intent.putExtras(bundle)
                        requireActivity().startActivity(intent)
                    }else{
                        Toast.makeText(requireContext(),
                            getString(R.string.message_if_trailer_not_found), Toast.LENGTH_SHORT).show()
                    }
                    mIsShowTrailer = false
                }
            }
            getDetailMovie.observe(viewLifecycleOwner) { data ->
                val genres = SpannableStringBuilder()
                data?.genres?.forEach {
                    genres.append("${it.name}, ")
                }

                this@with?.backdropImage?.let {
                    Glide.with(requireContext())
                        .load("${Constant.IMAGE_BACKDROP}${data?.backdropPath}")
                        .into(it)
                }

                this@with?.textTitle?.text = data?.title ?: "-"
                this@with?.textDate?.text = data?.releaseDate ?: "-"
                this@with?.textOverview?.text = data?.overview ?: "-"
                this@with?.textRating?.text = String.format("%.1f", data?.voteAverage ?: 0.0)
                this@with?.textGenresList?.text = genres.dropLast(2)

                this@with?.textShowReview?.setOnClickListener {
                    val action = DetailMovieFragmentDirections.actionDetailMovieFragmentToReviewFragment(
                        movieId = mMovieId.toString(),
                        movieName = data?.title ?: "-"
                    )
                    findNavController().navigate(action)
                }
            }
            getReviewList.observe(viewLifecycleOwner) { data ->
                Constant.handleData(
                    1, true, data?.results, mAdapter,
                    this@with?.rvReview, this@with?.textReviewNotFound
                )

                this@with?.textShowReview?.visibility = if(data?.results?.isNotEmpty() == true)
                    View.VISIBLE else View.GONE
            }
            error.observe(viewLifecycleOwner) {
                Constant.handleErrorApi(requireActivity(), it)
            }
            loading.observe(viewLifecycleOwner) {
                this@with?.swipeRefresh?.isRefreshing = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
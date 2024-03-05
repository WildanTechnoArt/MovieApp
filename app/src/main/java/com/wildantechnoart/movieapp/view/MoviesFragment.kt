package com.wildantechnoart.movieapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wildantechnoart.movieapp.databinding.FragmentMovieBinding
import com.wildantechnoart.movieapp.utils.Constant
import com.wildantechnoart.movieapp.viewmodel.MovieViewModel
import kotlin.properties.Delegates

class MoviesFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding
    private var mAdapter by Delegates.notNull<MoviesAdapter>()
    private val viewModel: MovieViewModel by viewModels()
    private var mGenreId: String? = null
    private var mGenreName: String? = null
    private var page: Int = 1
    private var totalPage: Int = 0
    private var isOnRefreshPage = false
    private var isLoading = false
    private lateinit var translateAnimation: TranslateAnimation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
        initListener()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() = with(binding) {
        val bundle = Bundle(arguments)
        mGenreId = MoviesFragmentArgs.fromBundle(bundle).genreId
        mGenreName = MoviesFragmentArgs.fromBundle(bundle).genreName
        this?.textGenreTitle?.text = "$mGenreName Movie"

        translateAnimation = TranslateAnimation(0F, 0F, 0F, 0F).apply {
            duration = 200
            fillAfter = true
            isFillEnabled = true
        }

        mAdapter = MoviesAdapter(requireView())
        this?.rvMovie?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mAdapter
        }

        viewModel.getMovieList(page, mGenreId)
        this?.swipeRefresh?.setOnRefreshListener {
            isOnRefreshPage = false
            page = 1
            viewModel.getMovieList(page, mGenreId)
        }
    }

    private fun initListener() {
        binding?.rvMovie?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition && page < totalPage) {
                    isOnRefreshPage = true
                    page = page.plus(1)
                    viewModel.getMovieList(page, mGenreId)
                }
            }
        })
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            getMovieList.observe(viewLifecycleOwner) { data ->
                totalPage = data?.totalPages ?: 0

                Constant.handleData(
                    page, false, data?.results, mAdapter,
                    this@with?.rvMovie, this@with?.textMessageNoData
                )
            }
            error.observe(viewLifecycleOwner) {
                isLoading = false
                isOnRefreshPage = false
                Constant.handleErrorApi(requireActivity(), it)
            }
            loading.observe(viewLifecycleOwner) {
                if (it) {
                    isLoading = true
                    if (isOnRefreshPage) {
                        this@with?.progressBar?.startAnimation(translateAnimation)
                        this@with?.progressBar?.visibility = View.VISIBLE
                    } else {
                        this@with?.swipeRefresh?.isRefreshing = true
                    }
                } else {
                    isLoading = false
                    isOnRefreshPage = false
                    this@with?.swipeRefresh?.isRefreshing = false
                    this@with?.progressBar?.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
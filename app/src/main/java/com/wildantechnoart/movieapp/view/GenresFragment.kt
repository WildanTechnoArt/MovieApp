package com.wildantechnoart.movieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.wildantechnoart.movieapp.databinding.FragmentGenresBinding
import com.wildantechnoart.movieapp.utils.Constant
import com.wildantechnoart.movieapp.viewmodel.MovieViewModel
import kotlin.properties.Delegates

class GenresFragment : Fragment() {

    private var _binding: FragmentGenresBinding? = null
    private val binding get() = _binding
    private var mAdapter by Delegates.notNull<GenresAdapter>()
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGenresBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
    }

    private fun initView() = with(binding) {
        mAdapter = GenresAdapter(requireView())
        this?.rvGenres?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mAdapter
        }

        viewModel.getGenreList()
        this?.swipeRefresh?.setOnRefreshListener {
            viewModel.getGenreList()
        }
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            getGenreList.observe(viewLifecycleOwner) { data ->
                Constant.handleData(
                    null, false, data, mAdapter,
                    this@with?.rvGenres, this@with?.textMessageNoData
                )
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
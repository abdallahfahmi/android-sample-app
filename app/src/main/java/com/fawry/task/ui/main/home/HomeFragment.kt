package com.fawry.task.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fawry.task.databinding.FragmentHomeBinding
import com.fawry.task.ui.base.BaseFragment
import com.fawry.task.ui.main.home.adapters.CategorizedMoviesAdapter
import com.fawry.task.utils.autoCleared
import com.fawry.task.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var binding by autoCleared<FragmentHomeBinding>()

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var adapter: CategorizedMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        observeResult(viewModel.movies) {
            adapter.updateList(it)
        }

    }

    override fun isLoading(state: Boolean) {
        binding.progressBar.show(state)
    }

    private fun setupRecyclerView() {
        adapter = CategorizedMoviesAdapter {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(it.movieId)
            )
        }
        binding.genreList.adapter = adapter
    }

}
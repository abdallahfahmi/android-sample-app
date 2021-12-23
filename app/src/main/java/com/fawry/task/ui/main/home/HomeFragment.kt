package com.fawry.task.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.databinding.FragmentHomeBinding
import com.fawry.task.ui.base.BaseFragment
import com.fawry.task.ui.main.home.adapters.GenresAdapter
import com.fawry.task.utils.autoCleared
import com.fawry.task.utils.show
import com.fawry.task.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var binding by autoCleared<FragmentHomeBinding>()

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var adapter: GenresAdapter

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

        viewModel.movies.observe(viewLifecycleOwner) {
            when (it.status) {
                RemoteResult.Status.SUCCESS -> {
                    binding.progressBar.show(false)
                    adapter.updateList(it.data ?: listOf())
                }
                RemoteResult.Status.ERROR -> {
                    binding.progressBar.show(false)
                    showToast(it.error?.message)
                }
                RemoteResult.Status.LOADING -> binding.progressBar.show(true)
            }
        }

    }

    private fun setupRecyclerView() {
        adapter = GenresAdapter {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(it.id)
            )
        }
        binding.genreList.adapter = adapter
    }

}
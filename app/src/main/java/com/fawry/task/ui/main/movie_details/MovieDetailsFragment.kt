package com.fawry.task.ui.main.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.databinding.FragmentMovieDetailsBinding
import com.fawry.task.ui.base.BaseFragment
import com.fawry.task.utils.autoCleared
import com.fawry.task.utils.show
import com.fawry.task.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {

    private var binding by autoCleared<FragmentMovieDetailsBinding>()

    private val viewModel by viewModels<MovieDetailsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movie.observe(viewLifecycleOwner) {
            when (it.status) {
                RemoteResult.Status.SUCCESS -> {
                    binding.progressBar.show(false)
                    binding.movie = it.data
                }
                RemoteResult.Status.ERROR -> {
                    binding.progressBar.show(false)
                    showToast(it.error?.message)
                }
                RemoteResult.Status.LOADING -> {
                    binding.progressBar.show(true)
                }
            }
        }

    }

}
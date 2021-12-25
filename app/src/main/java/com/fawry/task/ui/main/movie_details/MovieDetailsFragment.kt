package com.fawry.task.ui.main.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.fawry.task.databinding.FragmentMovieDetailsBinding
import com.fawry.task.ui.base.BaseFragment
import com.fawry.task.utils.autoCleared
import com.fawry.task.utils.show
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

        observeResult(viewModel.movie) {
            binding.movie = it
        }

    }

    override fun isLoading(state: Boolean) {
        binding.progressBar.show(state)
    }

}
package com.fawry.task.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.fawry.task.R
import com.fawry.task.data.network.ErrorHandler
import com.fawry.task.data.network.RemoteResult
import com.fawry.task.data.network.ResultError
import com.fawry.task.utils.showToast

open class BaseFragment : Fragment() {

    fun <D : Any> observeResult(
        liveData: LiveData<RemoteResult<D>>,
        onSuccess: (D) -> Unit
    ) {
        liveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    RemoteResult.Status.SUCCESS -> {
                        isLoading(false)
                        it.data?.let { data -> onSuccess(data) }
                    }
                    RemoteResult.Status.ERROR -> {
                        isLoading(false)
                        it.error?.let { err -> handleError(err) }
                    }
                    RemoteResult.Status.LOADING -> {
                        isLoading(true)
                    }
                }
            }
        }
    }

    open fun isLoading(state: Boolean) {}

    open fun handleError(resultError: ResultError) {
        showDefaultError(resultError)
    }

    //show default toast message for errors unless user overrides handleError() for custom error ui
    private fun showDefaultError(resultError: ResultError) {
        val errorMessage = when (resultError.type) {
            ErrorHandler.ErrorType.BAD_REQUEST_ERROR -> getString(R.string.bad_request_error_default_message)
            ErrorHandler.ErrorType.AUTHENTICATION_ERROR -> getString(R.string.authentication_error_default_message)
            ErrorHandler.ErrorType.AUTHORIZATION_ERROR -> getString(R.string.authorization_error_default_message)
            ErrorHandler.ErrorType.NOT_FOUND_ERROR -> getString(R.string.not_found_error_default_message)
            ErrorHandler.ErrorType.SERVER_ERROR -> getString(R.string.server_error_default_message)
            ErrorHandler.ErrorType.TIME_OUT_ERROR -> getString(R.string.time_out_error_default_message)
            ErrorHandler.ErrorType.NO_INTERNET_CONNECTION -> getString(R.string.no_internet_error_default_message)
            ErrorHandler.ErrorType.UNKNOWN_ERROR -> getString(R.string.unknown_error_message)
        }
        showToast(errorMessage)
    }

}
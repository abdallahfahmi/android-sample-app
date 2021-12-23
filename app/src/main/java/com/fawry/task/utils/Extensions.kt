package com.fawry.task.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.showToast(message: String?) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun View.show(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
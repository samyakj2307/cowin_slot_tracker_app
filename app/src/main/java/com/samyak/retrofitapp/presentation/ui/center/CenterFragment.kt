package com.samyak.retrofitapp.presentation.ui.center

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.samyak.retrofitapp.presentation.ui.center_list.CenterListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CenterFragment : Fragment() {

    private val viewModel: CenterListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                val currentCenter = viewModel.getCurrentCenter()
                Text("Hello Center Fragment ${currentCenter.name}")
            }
        }
    }
}
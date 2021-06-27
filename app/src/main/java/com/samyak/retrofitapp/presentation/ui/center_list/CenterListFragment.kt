package com.samyak.retrofitapp.presentation.ui.center_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.samyak.retrofitapp.presentation.components.CenterCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CenterListFragment : Fragment() {

    private val viewModel: CenterListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val centers = viewModel.centers.value

                LazyColumn {
                    itemsIndexed(items = centers) { index, center ->
                        CenterCard(center = center, onClick = {})
                    }
                }

            }
        }
    }
}

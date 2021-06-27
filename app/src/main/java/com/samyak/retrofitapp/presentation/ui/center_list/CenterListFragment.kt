package com.samyak.retrofitapp.presentation.ui.center_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.samyak.retrofitapp.R

class CenterListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Column {
                    Text("Hello Center List Fragment")
                    Button(onClick = { findNavController().navigate(R.id.viewCenter) }) {
                        Text(text = "Open")
                    }

                }
            }
        }
    }
}
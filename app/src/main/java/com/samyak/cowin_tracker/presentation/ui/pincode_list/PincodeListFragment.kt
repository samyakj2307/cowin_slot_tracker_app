package com.samyak.cowin_tracker.presentation.ui.pincode_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.samyak.cowin_tracker.presentation.components.BottomNavigationBar

class PincodeListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold(
                    bottomBar = { BottomNavigationBar(findNavController()) }
                ) {
                    Column(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                        Text(text = "Hello in Pincode Fragment")
                    }
                }
            }
        }
    }
}
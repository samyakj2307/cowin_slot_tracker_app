package com.samyak.cowin_tracker.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.findNavController
import com.samyak.cowin_tracker.R
import com.samyak.cowin_tracker.TAG
import com.samyak.cowin_tracker.presentation.components.navigation.BottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: $viewModel")

        val bottomNavigationView = findViewById<ComposeView>(R.id.bottom_navigation_view)

        bottomNavigationView.setContent {
            BottomNavigationBar(navController = findNavController(R.id.main_nav_host_fragment))
        }
    }

}
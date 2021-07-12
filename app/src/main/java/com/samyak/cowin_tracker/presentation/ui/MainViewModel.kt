package com.samyak.cowin_tracker.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(

) : ViewModel() {
    private lateinit var navController: NavController

    fun setCurrentNavController(navController: NavController) {
        this.navController = navController
    }

    fun getCurrentNavController(): NavController {
        return this.navController

    }
}
package com.samyak.retrofitapp.presentation.ui.center_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CenterListViewModel

@Inject
constructor(
    randomString: String
) : ViewModel() {
    init {
        println(randomString)
    }
}
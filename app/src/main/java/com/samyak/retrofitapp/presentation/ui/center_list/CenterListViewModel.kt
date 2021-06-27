package com.samyak.retrofitapp.presentation.ui.center_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samyak.retrofitapp.domain.model.Center
import com.samyak.retrofitapp.repository.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CenterListViewModel
@Inject
constructor(
    private val repository: CenterRepository
) : ViewModel() {

    val centers: MutableState<List<Center>> = mutableStateOf(listOf())

    init {
        newSearch()
    }

    fun newSearch(){
        viewModelScope.launch {
            val result = repository.getCenters(
                pincode = "452001",
                date = "28-6-21"
            )
            centers.value = result
        }
    }
}
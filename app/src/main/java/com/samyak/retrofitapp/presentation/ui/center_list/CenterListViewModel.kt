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

    val query = mutableStateOf("")
    val isSearchEnabled = mutableStateOf(false)
    val isError = mutableStateOf(false)

    val index = mutableStateOf(-1)

//    init {
//        //TODO change this default Value
//        newSearch("452001")
//    }

    fun newSearch(query: String) {
        viewModelScope.launch {
            val result = repository.getCenters(
                pincode = query,
                date = "28-6-21"
            )
            centers.value = result
        }
    }

    fun onQueryChanged(query: String) {
        this.query.value = query
        this.isSearchEnabled.value = query.length == 6
    }

    fun getCurrentCenter(): Center {
        return centers.value[index.value]
    }

    fun setCurrentIndex(index: Int) {
        this.index.value = index
    }
}
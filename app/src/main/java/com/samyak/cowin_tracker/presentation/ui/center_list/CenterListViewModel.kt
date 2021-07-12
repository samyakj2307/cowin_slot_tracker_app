package com.samyak.cowin_tracker.presentation.ui.center_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samyak.cowin_tracker.domain.model.Center
import com.samyak.cowin_tracker.repository.searchRepository.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Double.parseDouble
import java.time.LocalDate
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

    fun newSearch(query: String) {
        val day = LocalDate.now().dayOfMonth
        val month = LocalDate.now().monthValue
        val year = LocalDate.now().year

        viewModelScope.launch {
            val result = repository.getCenters(
                pincode = query,
                date = "$day-$month-$year"
            )
            centers.value = result
        }
    }

    fun onQueryChanged(query: String) {
        this.query.value = query


        var numeric = true

        try {
            val num = parseDouble(query)
        } catch (e: NumberFormatException) {
            numeric = false
        }

        this.isSearchEnabled.value = query.length == 6 && numeric
    }

    fun getCurrentCenter(): Center {
        return centers.value[index.value]
    }

    fun setCurrentIndex(index: Int) {
        this.index.value = index
    }
}
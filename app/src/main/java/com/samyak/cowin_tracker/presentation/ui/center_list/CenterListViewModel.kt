package com.samyak.cowin_tracker.presentation.ui.center_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samyak.cowin_tracker.TAG
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

    val isSearchComplete = mutableStateOf(true)

    private val _isNotificationSearchDone = MutableLiveData<Boolean>(false)
    val isNotificationSearchDone: LiveData<Boolean> = _isNotificationSearchDone


    val isNetworkAvailable = mutableStateOf(false)


    fun newSearch(query: String) {
        Log.d(TAG, "newSearch: Inside NewSearch")
        isSearchComplete.value = false
        val day = LocalDate.now().dayOfMonth
        val month = LocalDate.now().monthValue
        val year = LocalDate.now().year

        viewModelScope.launch {
            val result = repository.getCenters(
                pincode = query,
                date = "$day-$month-$year"
            )
            centers.value = result
            isSearchComplete.value = true


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

    fun newSearchForNotification(query: String, center_id: Int) {
        isSearchComplete.value = false
        val day = LocalDate.now().dayOfMonth
        val month = LocalDate.now().monthValue
        val year = LocalDate.now().year

        viewModelScope.launch {
            val result = repository.getCenters(
                pincode = query,
                date = "$day-$month-$year"
            )
            centers.value = result
            centers.value.forEachIndexed { index, center ->
                if (center.center_id == center_id) {
                    setCurrentIndex(index)
                    _isNotificationSearchDone.value = true
                }
            }
            isSearchComplete.value = true
        }
    }
}
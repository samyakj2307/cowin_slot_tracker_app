package com.samyak.retrofitapp.presentation.ui.center_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.samyak.retrofitapp.R
import com.samyak.retrofitapp.TAG
import com.samyak.retrofitapp.domain.model.Center
import com.samyak.retrofitapp.presentation.components.CenterCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CenterListFragment : Fragment() {

    private val viewModel: CenterListViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreateCenterListFragment: $viewModel")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val centers = viewModel.centers.value

                val query = viewModel.query.value
                val isSearchEnabled = viewModel.isSearchEnabled.value
                val isErrorState = viewModel.isError.value

                Column {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = Color.White,
                        elevation = 8.dp
                    ) {

                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(8.dp),
                                value = query,
                                onValueChange = { newValue ->
                                    viewModel.onQueryChanged(newValue)
                                },
                                label = {
                                    Text("Search Pincode")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Search
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Search Pincode"
                                    )
                                },
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        if (isSearchEnabled) {
                                            viewModel.isError.value = false
                                            clearFocus()
                                            viewModel.newSearch(
                                                query
                                            )
                                        } else {
                                            viewModel.isError.value = true
                                        }
                                    }
                                ),
                                textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.surface,
                                    focusedIndicatorColor = Color.Blue,
                                    focusedLabelColor = Color.Blue
                                ),
                                isError = isErrorState,
                            )
                        }
                    }
                    BottomListView(centers = centers)
                }

            }
        }
    }

    @Composable
    fun BottomListView(centers: List<Center>) {
        if (centers.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 12.dp,
                        bottom = 12.dp,
                    )
            ) {
                itemsIndexed(items = centers) { index, center ->
                    CenterCard(
                        center = center,
                        onClick = {
                            viewModel.setCurrentIndex(index)
                            findNavController().navigate(R.id.viewCenter)
                        }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Centers Available at this Pincode",
                    style = TextStyle(
                        color = Color.Gray,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Justify,
                    )
                )
            }
        }
    }
}

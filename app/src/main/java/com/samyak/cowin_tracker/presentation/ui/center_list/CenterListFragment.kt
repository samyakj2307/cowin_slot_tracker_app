package com.samyak.cowin_tracker.presentation.ui.center_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.samyak.cowin_tracker.R
import com.samyak.cowin_tracker.TAG
import com.samyak.cowin_tracker.domain.model.Center
import com.samyak.cowin_tracker.presentation.components.CenterCard
import com.samyak.cowin_tracker.presentation.ui.pincode_list.PincodeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CenterListFragment : Fragment() {

    private val viewModel: CenterListViewModel by activityViewModels()
    private val pincodeViewModel: PincodeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BackHandler(onBack = {
                    findNavController().popBackStack(
                        findNavController().graph.startDestinationId,
                        false
                    )
                    activity?.finish()
                })
                val query = viewModel.query.value
                val isSearchEnabled = viewModel.isSearchEnabled.value
                val isErrorState = viewModel.isError.value

                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier.fillMaxHeight(),
                    topBar = { SearchBar(query, isSearchEnabled, isErrorState) },
                    scaffoldState = scaffoldState
                ) {
                    val centers = viewModel.centers.value
                    Column(
                        modifier = Modifier.padding(
                            bottom = it.calculateBottomPadding(),
                            top = it.calculateTopPadding()
                        )
                    ) {
                        BottomListView(centers = centers)
                    }
                }

            }
        }
    }

    @Composable
    fun SearchBar(query: String, isSearchEnabled: Boolean, isErrorState: Boolean) {
        val focusManager = LocalFocusManager.current
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
                                Log.d(TAG, "SearchBar: Inside isSearchEnabled")
                                viewModel.isError.value = false
                                focusManager.clearFocus()
                                if (viewModel.isNetworkAvailable.value) {
                                    Log.d(TAG, "SearchBar: Inside isNetworkAvailable")
                                    viewModel.newSearch(query)
                                }
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

                val isExpanded = remember { mutableStateOf(false) }


                ConstraintLayout(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    val (menu) = createRefs()
                    IconButton(
                        modifier = Modifier
                            .constrainAs(menu) {
                                end.linkTo(parent.end)
                                linkTo(top = parent.top, bottom = parent.bottom)
                            },
                        onClick = { isExpanded.value = true },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Sign Out Menu"
                        )
                    }
                }
                SignOutDropdown(isExpanded = isExpanded)
            }
        }
    }

    @Composable
    fun SignOutDropdown(isExpanded: MutableState<Boolean>) {
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
        ) {
            DropdownMenu(
                expanded = isExpanded.value,
                onDismissRequest = { isExpanded.value = false },
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(onClick = {
                    isExpanded.value = false
                    context?.let {
                        AuthUI.getInstance()
                            .signOut(it)
                            .addOnCompleteListener {
                                pincodeViewModel.setUserId("")
                                pincodeViewModel.pincodes.value = mutableListOf()
                                viewModel.centers.value = listOf()
                            }

                    }
                }) {
                    Text(
                        text = "Sign Out",
                        style = TextStyle(color = Color.Black)
                    )
                }
            }
        }
    }


    @Composable
    fun BottomListView(centers: List<Center>) {

        if (!viewModel.isSearchComplete.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color.Blue,
                    strokeWidth = 6.dp,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }
        }

        if (centers.isNotEmpty()) {
            ConnectivityMonitor(isNetworkAvailable = viewModel.isNetworkAvailable.value)
            LazyColumn(
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 12.dp,
                        bottom = 12.dp,
                    )
                    .fillMaxHeight()
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
            Column {
                ConnectivityMonitor(isNetworkAvailable = viewModel.isNetworkAvailable.value)
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

    @Composable
    fun ConnectivityMonitor(
        isNetworkAvailable: Boolean,
    ) {
        if (!isNetworkAvailable) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Gray)
            ) {
                Text(
                    "No Network Connection",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp),
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
            }
        }
    }
}


package com.samyak.cowin_tracker.presentation.ui.add_pincode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.samyak.cowin_tracker.R
import com.samyak.cowin_tracker.domain.model.Pincode
import com.samyak.cowin_tracker.presentation.ui.pincode_list.PincodeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPincodeFragment : Fragment() {

    private val pincodeViewModel: PincodeViewModel by activityViewModels()
    private val viewModel: AddPincodeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val query = viewModel.pincodeString
                val isSaveEnabled = viewModel.isSaveEnabled
                val isErrorInPincode = viewModel.isErrorInPincode
                Scaffold {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "Add a Pincode to Track",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, bottom = 30.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            style = TextStyle(
                                color = Color(android.graphics.Color.parseColor("#002060")),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Black
                            )
                        )


                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                value = query,
                                onValueChange = { newValue ->
                                    viewModel.onChangePincodeString(newValue)
                                },
                                label = {
                                    Text("Enter Pincode")
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.LocationOn,
                                        contentDescription = "Enter Pincode"
                                    )
                                },
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        if (isSaveEnabled) {
                                            viewModel.isErrorInPincode = false
                                            clearFocus()
//                                viewModel.newSearch(
//                                    query
//                                )
                                        } else {
                                            viewModel.isErrorInPincode = true
                                        }
                                    }
                                ),
                                textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.surface,
                                    focusedIndicatorColor = Color.Blue,
                                    focusedLabelColor = Color.Blue
                                ),
                                isError = isErrorInPincode,
                            )
                        }
                        Column {
                            Text(
                                text = "Select an AgeGroup",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 25.dp, bottom = 10.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                style = TextStyle(
                                    color = Color(android.graphics.Color.parseColor("#002060")),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )
                            DisplayAgeGroupRadioGroup()
                            Text(
                                text = "Select Fee Type",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 25.dp, bottom = 10.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                style = TextStyle(
                                    color = Color(android.graphics.Color.parseColor("#002060")),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )
                            DisplayPriceRadioGroup()
                        }
                        Row(
                            modifier = Modifier
                                .padding(vertical = 40.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        ) {
                            Button(
                                onClick = {
                                    if (
                                        viewModel.isSaveEnabled &&
                                        viewModel.ageGroupSelected.value != "" &&
                                        viewModel.feeTypeSelected.value != ""
                                    ) {

                                        val newPincode = Pincode(
                                            pincode = viewModel.pincodeString,
                                            slot_tracking = viewModel.ageGroupSelected.value,
                                            fee_type = viewModel.feeTypeSelected.value
                                        )

                                        //Adding New Pincode to Firebase
                                        viewModel.addPincode(
                                            newPincode,
                                            pincodeViewModel.userId.value
                                        )

                                        findNavController().navigate(R.id.viewAllPincodes)

                                        viewModel.isSaveEnabled = false
                                        viewModel.pincodeString = ""
                                        viewModel.ageGroupSelected.value = ""
                                        viewModel.feeTypeSelected.value = ""
                                    }
                                },
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(android.graphics.Color.parseColor("#002060")),
                                    contentColor = Color.White
                                )

                            ) {
                                Text(
                                    text = "Add Pincode",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )

                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DisplayAgeGroupRadioGroup() {
        val ageGroupSelected = viewModel.ageGroupSelected
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
        ) {
            MyCustomRadioButton(selected = ageGroupSelected, category = "18+")
            MyCustomRadioButton(selected = ageGroupSelected, category = "45+")
            MyCustomRadioButton(selected = ageGroupSelected, category = "All")
        }
    }

    @Composable
    fun DisplayPriceRadioGroup() {
        val feeTypeSelected = viewModel.feeTypeSelected
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
        ) {
            MyCustomRadioButton(selected = feeTypeSelected, category = "Free")
            MyCustomRadioButton(selected = feeTypeSelected, category = "Paid")
            MyCustomRadioButton(selected = feeTypeSelected, category = "Both")
        }
    }

    @Composable
    fun MyCustomRadioButton(
        selected: MutableState<String>,
        category: String,
    ) {
        RadioButton(
            selected = selected.value == category,
            onClick = { selected.value = category },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Blue,
                unselectedColor = Color.Black,
                disabledColor = Color.Gray
            )
        )
        Text(
            text = "$category Slots",
            modifier = Modifier
                .clickable(onClick = { selected.value = category })
                .padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))

    }
}
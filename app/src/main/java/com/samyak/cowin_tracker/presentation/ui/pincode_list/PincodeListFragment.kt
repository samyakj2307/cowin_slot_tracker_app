package com.samyak.cowin_tracker.presentation.ui.pincode_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.samyak.cowin_tracker.R
import com.samyak.cowin_tracker.domain.model.Pincode
import com.samyak.cowin_tracker.presentation.components.PincodeCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PincodeListFragment : Fragment() {

    private val viewModel: PincodeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { findNavController().navigate(R.id.addPincode) },
                            shape = CircleShape,
                            backgroundColor = Color(android.graphics.Color.parseColor("#002060")),
                            contentColor = Color.White
                        ) { Icon(Icons.Filled.Add, "Add Pincode") }
                    }
                ) {
                    Column {
                        Text(
                            text = "Tracking Pincodes",
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .wrapContentHeight(Alignment.CenterVertically)
                                .padding(15.dp),
                            style = TextStyle(
                                color = Color(android.graphics.Color.parseColor("#002060")),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .background(color = Color.Black)
                        )
                        Column(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                            PincodeListView(viewModel.pincodes.value)
                        }
                    }

                }
            }
        }
    }

    @Composable
    fun PincodeListView(pincodes: MutableList<Pincode>) {
        if (pincodes.isNotEmpty()) {
            LazyColumn {
                itemsIndexed(items = pincodes) { index, value ->
                    PincodeCard(pincode = value.pincode, slot_tracking = value.slot_tracking,fee_type = value.fee_type,removePincode = { viewModel.removePincode(value) })
                }
            }
        }else{
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Pincode Added to Track.\nTap on the + Button to Add",
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
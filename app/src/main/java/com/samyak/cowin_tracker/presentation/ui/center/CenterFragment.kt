package com.samyak.cowin_tracker.presentation.ui.center

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.samyak.cowin_tracker.TAG
import com.samyak.cowin_tracker.domain.model.Center
import com.samyak.cowin_tracker.domain.model.Session
import com.samyak.cowin_tracker.presentation.components.BottomNavigationBar
import com.samyak.cowin_tracker.presentation.components.SessionCard
import com.samyak.cowin_tracker.presentation.ui.center_list.CenterListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CenterFragment : Fragment() {

    private val viewModel: CenterListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreateCenterFragment: $viewModel")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Scaffold(
                    bottomBar = { BottomNavigationBar(findNavController()) }
                ) {
                    val currentCenter = viewModel.getCurrentCenter()
                    val sessions = currentCenter.sessions

                    Column(
                        modifier = Modifier.padding(bottom = it.calculateBottomPadding())
                    ) {

                        CenterInfoSection(center = currentCenter)

                        if (sessions != null) {
                            SessionListView(sessions = sessions)
                        }
                    }
                }


            }
        }
    }

    @Composable
    fun CenterInfoSection(center: Center) {
        Surface(
            modifier = Modifier
                .background(color = Color.White),
            elevation = 5.dp
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                center.name?.let { name ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 6.dp)
                    ) {
                        Text(
                            text = name,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start),
                            style = TextStyle(
                                color = Color(android.graphics.Color.parseColor("#002060")),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )

                        center.pincode?.let { pincode ->
                            Text(
                                text = "$pincode",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.End),
                                style = TextStyle(
                                    color = Color(android.graphics.Color.parseColor("#002060")),
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            )
                        }
                    }
                }


                center.address?.let { address ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = address,
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .wrapContentWidth(Alignment.Start),
                            style = TextStyle(
                                color = Color.Gray,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 16.sp,
                            )
                        )
                    }
                }

                center.district_name?.let { district_name ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        center.state_name?.let { state_name ->
                            Text(
                                text = "$district_name, $state_name",
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .wrapContentWidth(Alignment.Start),
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 16.sp,
                                )
                            )
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun SessionListView(sessions: List<Session>) {
        val currentCenterSession = viewModel.getCurrentCenter()
        if (sessions.isNotEmpty()) {
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
                itemsIndexed(items = sessions) { index, session ->
                    if (currentCenterSession.fee_type == "Paid") {
                        var fee: String? = null
                        for (vaccineDetails in currentCenterSession.vaccine_fees!!) {
                            if (vaccineDetails.vaccine == session.vaccine) {
                                fee = vaccineDetails.fee
                                break
                            }
                        }

                        if (fee != null) {
                            SessionCard(
                                session = session,
                                price = fee
                            )
                        }
                    } else {
                        SessionCard(
                            session = session,
                            price = "0"
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Sessions Available at this Center",
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

    @Preview
    @Composable
    fun CenterFragmentPreview() {
        return CenterInfoSection(
            center = Center(
                name = "Child Care Hospital",
                address = "E-103 104 Bakhtawar Ram Nagar Indore",
                state_name = "Madhya Pradesh",
                district_name = "Indore",
                block_name = "INDORE",
                pincode = 452001,
                latitude = 22.0,
                longitude = 75.0,
                from = "09:00:00",
                to = "18:00:00",
                fee_type = "Paid",
            )
        )
    }
}

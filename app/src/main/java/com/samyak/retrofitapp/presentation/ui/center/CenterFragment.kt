package com.samyak.retrofitapp.presentation.ui.center

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.samyak.retrofitapp.TAG
import com.samyak.retrofitapp.domain.model.Session
import com.samyak.retrofitapp.presentation.components.SessionCard
import com.samyak.retrofitapp.presentation.ui.center_list.CenterListViewModel
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

                val currentCenter = viewModel.getCurrentCenter()
                val sessions = currentCenter.sessions

                Column() {
                    if (sessions != null) {
                        SessionListView(sessions = sessions)
                    }
                }
            }
        }
    }

    @Composable
    fun SessionListView(sessions: List<Session>) {
        if (sessions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 12.dp,
                        bottom = 12.dp,
                    )
            ) {
                itemsIndexed(items = sessions) { index, session ->
                    SessionCard(
                        session = session,
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
}
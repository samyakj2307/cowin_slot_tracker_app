package com.samyak.cowin_tracker.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samyak.cowin_tracker.domain.model.Center

@Composable
fun CenterCard(
    center: Center,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 8.dp,
        border = BorderStroke(1.dp, Color.LightGray)
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
                            .fillMaxWidth(0.7f)
                            .wrapContentWidth(Alignment.Start),
                        style = TextStyle(
                            color = Color(android.graphics.Color.parseColor("#002060")),
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                    center.fee_type?.let { fee_type ->
                        val colorString: String = if (fee_type == "Paid") {
                            "#000080"
                        } else {
                            "#006400"
                        }
                        Text(
                            text = fee_type,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.End)
                                .background(
                                    color = Color(android.graphics.Color.parseColor(colorString)),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(3.dp),
                            style = TextStyle(
                                color = Color.White,
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
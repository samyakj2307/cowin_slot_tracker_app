package com.samyak.retrofitapp.presentation.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samyak.retrofitapp.domain.model.Center

@Composable
fun CenterCard(
    center: Center,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .padding(
                bottom = 6.dp,
                top = 6.dp,
            )
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 8.dp,
    ) {
        Column {
            center.name?.let { name ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 6.dp, start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = name,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black,
                        )
                    )
                }
            }
            center.address?.let { address ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = address,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        style = TextStyle(
                            color = Color.Gray,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 18.sp,
                        )
                    )
                }
            }

            center.district_name?.let { district_name ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = district_name,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        style = TextStyle(
                            color = Color.Gray,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 18.sp,
                        )
                    )
                }
            }
            center.block_name?.let { block_name ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                ) {
                    center.state_name?.let { state_name ->
                        Text(
                            text = "$block_name, $state_name",
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .wrapContentWidth(Alignment.Start),
                            style = TextStyle(
                                color = Color.Gray,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 18.sp,
                            )
                        )
                    }
                }
            }


        }
    }
}


@Preview
@Composable
fun PreviewFunc() {
    return CenterCard(
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
        ),
        onClick = {})
}
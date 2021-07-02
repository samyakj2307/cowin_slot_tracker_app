package com.samyak.retrofitapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samyak.retrofitapp.domain.model.Session

@Composable
fun SessionCard(
    session: Session,
) {
    // TODO Change this to {false}
    val isExpanded = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable(onClick = { isExpanded.value = !isExpanded.value }),
        elevation = 8.dp,
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                session.vaccine?.let { vaccine ->
                    Text(
                        text = vaccine,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start),
                        style = TextStyle(
                            color = Color(android.graphics.Color.parseColor("#002060")),
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }


                session.date?.let { date ->
                    Text(
                        text = date,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                session.min_age_limit?.let { min_age_limit ->
                    val ageTag: String
                    val textBackgroundColor: Color
                    if (session.max_age_limit == null || session.max_age_limit < session.min_age_limit) {
                        ageTag = "$min_age_limit & Above"
                        textBackgroundColor = if (min_age_limit == 45) {
                            Color(android.graphics.Color.parseColor("#970897"))
                        } else {
                            Color.Blue
                        }
                    } else {
                        ageTag = "$min_age_limit - ${session.max_age_limit} Years"
                        textBackgroundColor = Color.Blue
                    }

                    Text(
                        modifier = Modifier
                            .background(
                                color = textBackgroundColor,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(4.dp),
                        text = ageTag,
                        color = Color.White
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End),
                    text = "Total Doses Available : ${session.available_capacity}",
                    style = TextStyle(fontWeight = FontWeight.Medium),
                )
            }

            if (isExpanded.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 3.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.Start),
                        text = "Dose 1 Available Capacity : ${session.available_capacity_dose1}",
                        style = TextStyle(fontWeight = FontWeight.Medium)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp, bottom = 3.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.Start),
                        text = "Dose 2 Available Capacity : ${session.available_capacity_dose2}",
                        style = TextStyle(fontWeight = FontWeight.Medium)
                    )
                }
            }

        }
    }
}


@Preview
@Composable
fun SessionCardPreview() {
    return SessionCard(
        session = Session(
            date = "12-07-2021",
            available_capacity = 79,
            min_age_limit = 18,
            max_age_limit = 45,
            vaccine = "COVISHIELD",
            slots = listOf(
                "10:00AM-11:00AM",
                "11:00AM-12:00PM",
                "12:00PM-01:00PM",
                "01:00PM-04:00PM"
            ),
            available_capacity_dose1 = 37,
            available_capacity_dose2 = 42,
        ),
    )
}
package com.samyak.cowin_tracker.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PincodeCard(pincode: String, slot_tracking: String) {

    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable(onClick = {}),
        elevation = 8.dp,
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row {
            Text(
                text = pincode,
                modifier = Modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.Start)
                    .padding(26.dp)
            )
            Text(
                text = slot_tracking,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(20.dp)
                    .background(color = Color.Blue, shape = CircleShape)
                    .padding(6.dp),
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun PincodeCardPreview() {
    return PincodeCard(pincode = "465441", slot_tracking = "18+")
}
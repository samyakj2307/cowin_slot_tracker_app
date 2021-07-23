package com.samyak.cowin_tracker.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun PincodeCard(
    pincode: String,
    slot_tracking: String,
    fee_type: String,
    removePincode: () -> Unit
) {
    val isOpen = remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable(onClick = { isOpen.value = true }),
        elevation = 8.dp,
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row {

            Text(
                text = slot_tracking,
                modifier = Modifier
                    .wrapContentWidth(Alignment.Start)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(vertical = 20.dp, horizontal = 10.dp)
                    .background(
                        color = if (slot_tracking == "45+") {
                            Color(android.graphics.Color.parseColor("#970897"))
                        } else {
                            Color.Blue
                        },
                        shape = CircleShape
                    )
                    .padding(8.dp),
                color = Color.White,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            ConstraintLayout(modifier = Modifier.align(Alignment.CenterVertically)) {
                val (menu) = createRefs()
                Text(
                    text = pincode,
                    modifier = Modifier
                        .wrapContentHeight(Alignment.CenterVertically)
                        .wrapContentWidth(Alignment.Start)
                        .constrainAs(menu) {
                            end.linkTo(parent.end)
                            linkTo(top = parent.top, bottom = parent.bottom)
                        },
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Text(
                text = fee_type,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(vertical = 20.dp, horizontal = 10.dp)
                    .background(
                        color = if (fee_type == "Paid") {
                            Color(android.graphics.Color.parseColor("#000080"))
                        } else {
                            Color(android.graphics.Color.parseColor("#006400"))
                        },
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(8.dp),
                color = Color.White,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
        if (isOpen.value) {
            DeletePincodeAlertBox(isOpen = isOpen, removePincode = removePincode)
        }
    }
}


@Composable
fun DeletePincodeAlertBox(isOpen: MutableState<Boolean>, removePincode: () -> Unit) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(0.75f),
        onDismissRequest = { isOpen.value = false },
        confirmButton = {
            Button(
                onClick = {
                    isOpen.value = false
                    // TODO Remove Pincode from Database
                    removePincode()

                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Remove")
            }
        },
        dismissButton = {
            Button(
                onClick = { isOpen.value = false },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Cancel")
            }
        },
        title = { Text(text = "Remove Pincode") },
        text = { Text(text = "Do you want to stop tracking this Pincode?") }
    )
}


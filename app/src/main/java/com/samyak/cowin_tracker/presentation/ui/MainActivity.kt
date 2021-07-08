package com.samyak.cowin_tracker.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.samyak.cowin_tracker.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
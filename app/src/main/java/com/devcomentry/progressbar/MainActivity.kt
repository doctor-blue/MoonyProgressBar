package com.devcomentry.progressbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devcomentry.progressbarlibrary.MoonyProgressBar
import com.devcomentry.progressbarlibrary.ProgressState
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val progressBar = findViewById<MoonyProgressBar>(R.id.progress_bar)
        GlobalScope.launch {
            for (i in 0..110 step 10) {
                delay(1000L)
                withContext(Dispatchers.Main) {
                    if (i == 80) {
                        progressBar.setState(ProgressState.PAUSED, i.toFloat())

                    } else if (i < 80f) {
                        progressBar.setState(ProgressState.IN_PROGRESS, i.toFloat())

                    }
                }
            }
        }
        progressBar.setOnClickListener {

        }
    }
}
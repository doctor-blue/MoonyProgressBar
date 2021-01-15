package com.doctorblue.nonameprogressbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doctorblue.library.ProgressState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch {
            for (i in 0..110 step 10) {
                delay(1000L)
                withContext(Dispatchers.Main) {
                    if (i == 80){
                        progress_bar.setState(ProgressState.PAUSED, i.toFloat())

                    }else if (i <80f){
                        progress_bar.setState(ProgressState.IN_PROGRESS, i.toFloat())

                    }
                }
            }
        }
        progress_bar.setOnClickListener {

        }
    }

}
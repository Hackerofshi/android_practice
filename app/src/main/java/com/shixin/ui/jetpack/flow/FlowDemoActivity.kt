package com.shixin.ui.jetpack.flow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log
import android.widget.Button

import com.shixin.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.Dispatcher

public class FlowDemoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_demo)


        findViewById<Button>(R.id.btn1).setOnClickListener {
            demo1()
        }

    }

    private fun demo1() {
        CoroutineScope(Dispatchers.Main).launch {
            flowOf(1, 2, 3, 4, 5)
                    .onEach {
                        delay(100)
                    }
                    .collect {
                        println(it)
                    }
        }
    }

}
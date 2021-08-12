package com.shixin.ui.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.shixin.R
import com.shixin.ui.view.customviewgroup.DragScaleRelativeLayout

class CustomDragScaleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_drag_scale)
        var tvContent = findViewById<TextView>(R.id.tv_content)
        val rl = findViewById<DragScaleRelativeLayout>(R.id.rl)
        val btn = findViewById<Button>(R.id.btn);



        val pp =rl.layoutParams as ViewGroup.MarginLayoutParams

        btn.setOnClickListener {

            pp.width = 200
            pp.height = 200
            pp.topMargin = 200
            pp.bottomMargin = 200

            rl.layoutParams = pp
        }


        rl.setValueChange { width, height,left,top ->
          //  Log.i("setValueChange", "onCreate: $width")
           // Log.i("setValueChange", "onCreate: $height")



//
          //  tvContent.width = width
         //   tvContent.height = height
//
//
//            pp.width = width
//            pp.height = height
//            pp.leftMargin = left
//            pp.topMargin = top
//
//            Log.i("TAG", "onCreate: $left")
//            Log.i("TAG", "onCreate: $top")
//
//            rl.layoutParams = pp


        }

    }
}
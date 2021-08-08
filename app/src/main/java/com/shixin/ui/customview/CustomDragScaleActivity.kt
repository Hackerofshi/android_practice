package com.shixin.ui.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.shixin.R
import com.shixin.view.customview.DragScaleRelativeLayout
import com.shixin.view.customview.scroller.TextViewScroller
import kotlinx.android.synthetic.main.activity_constraint.*

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
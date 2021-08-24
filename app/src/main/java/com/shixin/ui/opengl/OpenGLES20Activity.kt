package com.shixin.ui.opengl

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shixin.R
import com.shixin.ui.opengl.widget.MyGLSurfaceView

class OpenGLES20Activity : AppCompatActivity() {

    private lateinit var gLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gLView = MyGLSurfaceView(this)
        //setContentView(gLView)
        // setContentView(R.layout.activity_open_gles20)
    }
}
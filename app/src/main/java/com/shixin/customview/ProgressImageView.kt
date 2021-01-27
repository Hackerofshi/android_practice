package com.shixin.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class ProgressImageView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
    : AppCompatImageView(context, attrs, defStyleAttr
) {

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    private var pWidht = 0
    private var pHeight = 0

    private var pDrawable: Drawable? = null
    private var progress: Float = 0f

    init {
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val cmf = ColorMatrixColorFilter(cm)
        colorFilter = cmf
    }

    override fun onDraw(canvas: Canvas?) {

        if (pDrawable != null && canvas != null && progress != 0f) {
            canvas.save()
            if (imageMatrix != null) {
                canvas.concat(imageMatrix)

            }
            canvas.clipRect(createClipRect())

            pDrawable!!.draw(canvas)
            canvas.restore()

        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (pDrawable != drawable && null != drawable) {
            pDrawable = drawable.constantState?.newDrawable()?.mutate()
            configBound()
        }
    }

    private fun createClipRect(): Rect {

        val height = pHeight * (progress / 100f)
        val rect = Rect(0, 0, pWidht, height.toInt())
        return rect;
    }

    private fun configBound() {
        pDrawable?.apply {
            this.clearColorFilter()
            pWidht = this.minimumWidth
            pHeight = this.minimumHeight
            this.setBounds(0, 0, pWidht, pHeight)
        }
    }

    fun setProgress(progress: Float) {
        if (progress < this.progress) {
            return
        }
        this.progress = progress
        if (this.progress > 100f) {
            this.progress = 100f
        }
        invalidate()
    }


}
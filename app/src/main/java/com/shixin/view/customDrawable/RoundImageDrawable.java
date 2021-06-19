package com.shixin.view.customDrawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by admin on 2017/3/9 0009.
 */

public class RoundImageDrawable extends Drawable {


    private Paint  mPaint;
    private Bitmap mBitmap;
    private RectF  rectF;

    public RoundImageDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        mPaint = new Paint();
        rectF = new RectF();
        mPaint.setAntiAlias(true);

        mPaint.setShader(bitmapShader);

    }


    //    下面几个方法都是要实现的
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(rectF, 30, 30, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();

    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }
}

package com.shixin.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by shixin on 2017/4/17 0017.
 */

public class BitmapCanvasView extends View {
    private Path mPath = new Path();



    private Bitmap mBmp;
    private Paint mPaint;
    private Canvas mBmpCanvas;
    public BitmapCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);


        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mBmp = Bitmap.createBitmap(1000 ,1000 , Bitmap.Config.ARGB_8888);
        mBmpCanvas = new Canvas(mBmp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                mPath.moveTo(event.getX(), event.getY());
                return true;
            }
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                postInvalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
       // mBmpCanvas.drawText("启舰大SB",0,100,mPaint);
       mBmpCanvas.drawPath(mPath,mPaint);
        canvas.drawBitmap(mBmp,0,0,mPaint);
    }


    public void reset(){
        mPath.reset();
        invalidate();
    }

    public Bitmap getmBmp() {
        return mBmp;
    }



}

package com.shixin.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.shixin.R;

/**
 * Created by admin on 2017/3/7 0007.
 */

public class RoundImageView extends ImageView {
    private              int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND  = 1;

    /**
     * 圆角大小的默认值
     */
    private static final int BODER_RADIUS_DEFAULT = 10;

    /**
     * 圆角的大小
     */
    private int   _BorderRadius;
    /**
     * 绘图的Paint
     */
    private Paint _BitmapPaint;


    /**
     * 圆角的半径
     */
    private int    _Radius;
    /**
     * 3X3矩阵   主要用于缩小放大
     */
    private Matrix _Matrix;

    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader _BitmapShader;

    /**
     * view 的宽度
     */

    private int mWidth;

    private RectF mRoundRect;

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _Matrix = new Matrix();
        _BitmapPaint = new Paint();
        _BitmapPaint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        _BorderRadius = a.getDimensionPixelOffset
                (R.styleable.RoundImageView_borderRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BODER_RADIUS_DEFAULT,
                        getResources().getDisplayMetrics()));
        type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);
        a.recycle();

    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            _Radius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        setUpShader();

        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(mRoundRect, _Radius, _BorderRadius, _BitmapPaint);

        } else {
            canvas.drawCircle(_Radius, _Radius, _Radius, _BitmapPaint);
            // drawSomeThing(canvas);
        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圆角图片的范围
        if (type == TYPE_ROUND) {
            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
        }
    }

    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        Bitmap bmp = drableToBitmap(drawable);

        _BitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE) {
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bSize;
        } else if (type == TYPE_ROUND) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；
            // 缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
        }
        //shader的变换矩阵  我们这里主要用于放大或缩小
        _Matrix.setScale(scale, scale);
        //设置变换矩阵
        _BitmapShader.setLocalMatrix(_Matrix);
        //设置shader
        _BitmapPaint.setShader(_BitmapShader);
    }

    private Bitmap drableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int    w      = drawable.getIntrinsicWidth();
        int    h      = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public void setBorderRadius(int borderRadius) {
        int pxVal = dp2px(borderRadius);
        if (this._BorderRadius != pxVal) {
            this._BorderRadius = pxVal;
            invalidate();
        }
    }

    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}

package com.shixin.ui.view.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.xutils.common.util.DensityUtil;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class PressFrameLayout extends FrameLayout {
    private int width  = 0;
    private int height = 0;
    private int padding;
    private int cornerRadius;
    private int shadeOffset;
    Paint  paintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
    Camera camera  = new Camera();
    float  cameraX = 0f;
    float  cameraY = 0f;

    private int colorBg;//按压播放动画控制
    private int shadeAlpha = 0xaa000000;//相机旋转（按压偏移）动画控制

    private float touchProgress  = 1f;
    private float cameraProgress = 0f;

    boolean isInPressArea = true;//按压位置是内圈还是外圈
    private int   maxAngle = 5;// 倾斜是的相机最大倾斜角度
    private float scale    = 0.98f;//整体按压时的形变控制

    private long pressTime = 0;//计算按压时间，小于500ms响应onClick()
    Bitmap bitmap;
    Rect   srcRectF = new Rect();
    RectF  dstRectF = new RectF();


    TouchArea pressArea = new TouchArea(0, 0, 0, 0);//按压效果


    public PressFrameLayout(@NonNull Context context) {
        super(context);
    }

    public PressFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PressFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    {
        //取消硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //开启ViewGroup的onDraw()
        setWillNotDraw(false);

        padding = DensityUtil.dip2px(20);
        cornerRadius = DensityUtil.dip2px(5);
        shadeOffset = DensityUtil.dip2px(5);

        //View的background为颜色或者图片的两种情况
        Drawable backGround = getBackground();
        if (backGround instanceof ColorDrawable) {
            colorBg = ((ColorDrawable) backGround).getColor();
            paintBg.setColor(colorBg);
        } else {
            bitmap = ((BitmapDrawable) backGround).getBitmap();
            srcRectF = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        }
        setBackground(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInPressArea) {
            camera.save();

            //相机在控件中心上方，在x，y轴方向旋转，形成控件倾斜效果
            camera.rotateX(maxAngle * cameraX * cameraProgress);
            camera.rotateY(maxAngle * cameraY * cameraProgress);
            camera.translate(width / 2f, height / 2f, 0);
            camera.applyToCanvas(canvas);

            //还原canvas坐标系
            canvas.translate(-width / 2f, -height / 2f);

            camera.restore();

        }

        paintBg.setShadowLayer(shadeOffset * touchProgress, 0, 0, (colorBg & 0x00FFFFFF) | shadeAlpha);
        if (null != bitmap) {
            canvas.drawBitmap(bitmap, srcRectF, dstRectF, paintBg);

        } else {
            canvas.drawRoundRect(dstRectF
                    , cornerRadius, cornerRadius, paintBg);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        dstRectF.set(padding, padding, width - padding, height - padding);
        pressArea.set((width - 2 * padding) / 4f + padding, (height - 2 * padding) / 4f + padding, width -
                (width - 2 * padding) / 4f - padding, height - (width - 2 * padding) / 4f - padding);
    }

    private boolean isInPressArea(float x, float y) {
        return x > pressArea.getLeft() && x < pressArea.getRight()
                && y > pressArea.getTop() && y < pressArea.getBottom();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        AnimatorSet animatorSet = new AnimatorSet();
        int         duration    = 100;
        int         type        = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressTime = System.currentTimeMillis();
                type = 1;
                isInPressArea = isInPressArea(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_CANCEL:
                type = 2;
                break;
            case MotionEvent.ACTION_UP:
                if ((System.currentTimeMillis() - pressTime) < 500) {
                    performClick();
                }
                break;
        }

        if (isInPressArea) {
            if (type != 0) {
                ObjectAnimator animX = ObjectAnimator.ofFloat(this,
                        "scaleX", type == 1 ? scale : 1, type == 1 ? scale : 1).setDuration(duration);
                ObjectAnimator animY = ObjectAnimator.ofFloat(this, "scaleY",
                        type == 1 ? 1 : scale, type == 1 ? scale : 1
                ).setDuration(duration);
                ObjectAnimator animZ = ObjectAnimator.ofFloat(this,
                        "touchProgress", type == 1 ? 1 : 0, type == 1 ? 0 : 1
                ).setDuration(duration);
                animX.setInterpolator(new DecelerateInterpolator());
                animY.setInterpolator(new DecelerateInterpolator());
                animZ.setInterpolator(new DecelerateInterpolator());

                animatorSet.playTogether(animX, animY, animZ);
                animatorSet.start();

            }
        } else {
            cameraX = (event.getX() - width / 2f) / ((width - 2 * padding) / 2f);
            if (cameraX > 1) cameraX = 1;
            if (cameraX < -1) cameraX = -1;
            cameraY = (event.getY() - height / 2f) / ((height - 2 * padding) / 2f);
            if (cameraY > 1) cameraY = 1;
            if (cameraY < -1) cameraY = -1;

            //坐标系调整
            float tmp = cameraX;
            cameraX = -cameraY;
            cameraY = tmp;

            switch (type) {
                case 1:
                    ObjectAnimator.ofFloat(this, "cameraProgress", 0, 1).setDuration(duration).start();
                    break;
                case 2:
                    ObjectAnimator.ofFloat(this, "cameraProgress", 1, 0).setDuration(duration).start();
                    break;
                default:
                    break;
            }
        }


        return true;
    }

    public float getTouchProgress() {
        return touchProgress;
    }

    public void setTouchProgress(float touchProgress) {
        this.touchProgress = touchProgress;
        invalidate();
    }

    public float getCameraProgress() {
        return cameraProgress;
    }

    public void setCameraProgress(float cameraProgress) {
        this.cameraProgress = cameraProgress;
        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}

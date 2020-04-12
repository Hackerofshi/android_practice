package com.shixin.rxjava;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import org.xutils.x;

import com.shixin.customview.BitmapCanvasView;

public class DrawBitmapAvtivity extends AppCompatActivity {

    private ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setContentView(R.layout.activity_draw_bitmap_avtivity);
        final BitmapCanvasView myView = (BitmapCanvasView)findViewById(R.id.myview);
        img1 = (ImageView) findViewById(R.id.img1);
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = myView.getmBmp();
                int width = bitmap.getWidth();
                img1.setImageBitmap(bitmap);
                System.out.println("------"+width);

            }
        });

    }

}

package com.chuntingyu.picme.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditableView extends ViewGroup {
    private Context context;
    private ImageView imageView = null;
    private DrawableView paintingView = null;

    public EditableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initParam(context, attrs);
    }

    private void initParam(Context context, AttributeSet attrs) {
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.BLACK);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(12);
    }

    public void setImageBitmap(Bitmap bitmap) {
        imageView = new ImageView(context);
        imageView.setCropToPadding(true);
        imageView.setAdjustViewBounds(true);

        if (bitmap.getWidth() >= bitmap.getHeight()) {
            float i = ((float) getMeasuredWidth()) / ((float) bitmap.getWidth());
            float imageHeight = i * (bitmap.getHeight());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(getMeasuredWidth(), (int) imageHeight));
        } else if (bitmap.getWidth() < bitmap.getHeight()) {
            float j = ((float) getMeasuredHeight()) / ((float) bitmap.getHeight());
            float imageWidth = j * (bitmap.getWidth());
            imageView.setLayoutParams(new LinearLayout.LayoutParams((int) imageWidth, getMeasuredHeight()));
        }

        imageView.setImageBitmap(bitmap);

        paintingView = new DrawableView(context);
        Bitmap transparentBitmap = makeTransparent(bitmap, 0);
        paintingView.setImageBitmap(transparentBitmap);

        addView(imageView);
        addView(paintingView);

//        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (imageView != null) {
            int imageWidth = imageView.getMeasuredWidth();
            int imageHeight = imageView.getMeasuredHeight();
            imageView.measure(MeasureSpec.makeMeasureSpec(imageWidth, MeasureSpec.getMode(widthMeasureSpec)), MeasureSpec.makeMeasureSpec(imageHeight, MeasureSpec.getMode(heightMeasureSpec)));
            paintingView.measure(MeasureSpec.makeMeasureSpec(imageWidth, MeasureSpec.getMode(widthMeasureSpec)), MeasureSpec.makeMeasureSpec(imageHeight, MeasureSpec.getMode(heightMeasureSpec)));
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (imageView != null) {
            int ivl = (getMeasuredWidth() - imageView.getMeasuredWidth()) / 2;
            int ivt = (getMeasuredHeight() - imageView.getMeasuredHeight()) / 2;
            int ivr = imageView.getMeasuredWidth();
            int ivb = imageView.getMeasuredHeight();

            imageView.layout(ivl, ivt, ivr + ivl, ivb + ivt);
            paintingView.layout(ivl, ivt, ivr + ivl, ivb + ivt);
        }
    }

    public Bitmap makeTransparent(Bitmap src, int value) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // config paint
        final Paint paint = new Paint();
        paint.setAlpha(value);
        canvas.drawBitmap(src, 0, 0, paint);
        return transBitmap;
    }

    public void undoDrawing() {
        this.paintingView.undoDrawing();
    }
}

package com.chuntingyu.picme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by Kevin on 2018/4/22.
 */

public class MyCanvasView extends View{

    Bitmap mBitmap;
    private Paint mPaint;
    private Path mPath;
    private int mDrawColor;
    private int mBackgroundColor;
    private Canvas mExtraCanvas;
    private Bitmap mExtraBitmap;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    MyCanvasView(Context context) {
        this(context, null);
    }



    public MyCanvasView(Context context, AttributeSet attributeSet) {
        super(context);

//        mBackgroundColor = ResourcesCompat.getColor(getResources(),
//                R.color.opaque_orange, null);

//        mExtraBitmap = bitmap;
//        mExtraCanvas = new Canvas(mExtraBitmap);

        mDrawColor = ResourcesCompat.getColor(getResources(),
                R.color.opaque_yellow, null);

        // Holds the path we are currently drawing.
        mPath = new Path();
        // Set up the paint with which to draw.
        mPaint = new Paint();
        mPaint.setColor(mDrawColor);
        // Smoothes out edges of what is drawn without affecting shape.
        mPaint.setAntiAlias(true);
        // Dithering affects how colors with higher-precision device
        // than the are down-sampled.
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE); // default: FILL
        mPaint.setStrokeJoin(Paint.Join.ROUND); // default: MITER
        mPaint.setStrokeCap(Paint.Cap.ROUND); // default: BUTT
        mPaint.setStrokeWidth(12); // default: Hairline-width (really thin)
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {

        super.onSizeChanged(width, height, oldWidth, oldHeight);

        // Create bitmap, create canvas with bitmap, fill canvas with color.
        if (mBitmap == null){
            mExtraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } else {
            mExtraBitmap = Bitmap.createBitmap(mBitmap);
        }


//        mExtraBitmap = Bitmap.createBitmap(mBitmap);
        mExtraCanvas = new Canvas(mExtraBitmap);

        // Fill the Bitmap with the background color.
//        mExtraCanvas.drawColor(mBackgroundColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        // Draw the bitmap that stores the path the user has drawn.
        // Initially the user has not drawn anything
        // so we see only the colored bitmap.

//        canvas.drawBitmap(mExtraBitmap, 0, 0, null);
        canvas.drawBitmap(mExtraBitmap, 0, 0, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // Invalidate() is inside the case statements because there are many
        // other types of motion events passed into this listener,
        // and we don't want to invalidate the view for those.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                // No need to invalidate because we are not drawing anything.
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                // No need to invalidate because we are not drawing anything.
                break;
            default:
                // Do nothing.
        }
        return true;
    }

    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            // Reset mX and mY to the last drawn point.
            mX = x;
            mY = y;
            // Save the path in the extra bitmap,
            // which we access through its canvas.
            mExtraCanvas.drawPath(mPath, mPaint);
        }
    }

    private void touchUp() {
        // Reset the path so it doesn't get drawn again.
        mPath.reset();
    }

    public void setPicture (Bitmap bitmap) {

        mBitmap = bitmap;
//        setBackground(bitmap);

    }
}

package com.chuntingyu.picme.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class DrawableView extends ImageView {
    private Paint paint;
    private Path path;
    private float mX, mY, nX, nY;

    public DrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initParam(context, attrs);
    }

    private void initParam(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawStart(event.getX(), event.getY());
                invalidate();
                Log.e("========", "touch DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("========", "touch MOVE");
                invalidate();
                drawMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.e("========", "touch UP");
                invalidate();
                drawEnd();
                break;
        }
        return true;
    }

    private void drawStart(float x, float y) {
//        paths.clear();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void drawMove(float x, float y) {
        nX = mX;
        nY = mY;

        path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
        // Reset mX and mY to the last drawn point.
        mX = x;
        mY = y;
    }

    private void drawEnd() {
        // Reset the path so it doesn't get drawn again.
//        paths.add(path);
//        path.reset();
    }
}

package com.chuntingyu.picme.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class DrawableView extends AppCompatImageView {
    private Paint paint;
    private Path path;
    private float mX, mY;
    private List<Path> paths = new ArrayList<>();
    private int color = Color.BLACK;

    public DrawableView(Context context) {
        super(context);
        initParam();
    }

    private void initParam() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paths.size() > 0) {
            for (Path path : paths) {
                canvas.drawPath(path, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawStart(event.getX(), event.getY());
//                Log.e("========", "touch DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e("========", "touch MOVE");
                drawMove(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
//                Log.e("========", "touch UP");
                drawEnd();
                break;
        }
        return true;
    }

    public void undoDrawing() {
        if (paths.size() > 0) {
            paths.remove(paths.size() - 1);
        }
        invalidate();
    }

    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    public void setPaintErase(Boolean b) {
        if (b) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            paint.setXfermode(null);
        }
    }

    private void drawStart(float x, float y) {
        path = new Path();
        paths.add(path);
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void drawMove(float x, float y) {
        path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
        // Reset mX and mY to the last drawn point.
        mX = x;
        mY = y;
    }

    private void drawEnd() {
        // Reset the path so it doesn't get drawn again.
    }
}

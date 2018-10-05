package com.chuntingyu.picme.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class DrawableView extends AppCompatImageView {
    private Paint paint = null;
    private Path path = null;
    private float mX, mY;
    private List<PathPaint> pathPaints = new ArrayList<>();
    private int paintColor = Color.BLACK;
    private int paintSize = 12;
    private PorterDuffXfermode paintMode = null;

    public DrawableView(Context context) {
        super(context);
        initParam();
    }

    private void initParam() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pathPaints.size() > 0) {
            for (PathPaint pathPaint : pathPaints) {
                canvas.drawPath(pathPaint.getPath(), pathPaint.getPaint());
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
        if (pathPaints.size() > 0) {
            pathPaints.remove(pathPaints.size() - 1);
        }
        invalidate();
    }

    public void setPaintSize(int size) {
        this.paintSize = size;
    }

    public void setPaintColor(int color) {
        this.paintColor = color;
    }

    public void setPaintErase(Boolean mode) {
        if (mode) {
            this.paintMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        } else {
            this.paintMode = null;
        }
    }

    private void drawStart(float x, float y) {
        path = new Path();
        path.moveTo(x, y);
        mX = x;
        mY = y;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(paintColor);
        paint.setStrokeWidth(paintSize);
        paint.setXfermode(paintMode);

        pathPaints.add(new PathPaint(path, paint));
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

    private class PathPaint {
        private Path path;
        private Paint paint;

        private PathPaint(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }

        private Paint getPaint() {
            return this.paint;
        }

        private Path getPath() {
            return path;
        }
    }
}

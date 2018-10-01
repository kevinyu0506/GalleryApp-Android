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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawableView extends ImageView {
    private Paint paint;
    private Path path;
    private float mX, mY;
    private List<Path> paths = new ArrayList<>();
    private Map<Integer, List<Path>> pathsMap = new HashMap<>();
    private Integer count = 0;

    public DrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParam(context, attrs);
    }

    private void initParam(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
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
        if (paths.size() > 0) {
            for (Path path : paths) {
                canvas.drawPath(path, paint);
            }
        }

        if (pathsMap.size() > 0) {
            for (int i = 0; i < pathsMap.size() - 1; i++) {
                List<Path> paths = pathsMap.get(i);
                for (Path path : paths) {
                    canvas.drawPath(path, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawStart(event.getX(), event.getY());
                Log.e("========", "touch DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("========", "touch MOVE");
                drawMove(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("========", "touch UP");
                drawEnd();
                break;
        }
        return true;
    }

    public void undoDrawing() {
        paths.clear();
        pathsMap.remove(count);
        if (count > 0) {
            count--;
        }
        invalidate();
    }

    private void drawStart(float x, float y) {
        paths = new ArrayList<>();
        path = new Path();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void drawMove(float x, float y) {
        path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
        paths.add(path);
        // Reset mX and mY to the last drawn point.
        mX = x;
        mY = y;
        pathsMap.put(count, paths);
    }

    private void drawEnd() {
        // Reset the path so it doesn't get drawn again.
        count++;
    }
}

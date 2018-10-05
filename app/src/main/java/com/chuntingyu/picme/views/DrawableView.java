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

public class DrawableView extends ViewGroup {
    private Context context;
    private ImageView imageView = null;
//    private ImageView paintingView;
    private Paint paint;
    private Path path;
    private float mX, mY;
    private List<Path> paths = new ArrayList<>();
    private Map<Integer, List<Path>> pathsMap = new HashMap<>();
    private Integer count = 0;

    public DrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initParam(context, attrs);
    }

    private void initParam(Context context, AttributeSet attrs) {
//        imageView = new ImageView(context);
//        imageView.setCropToPadding(true);
//        imageView.setAdjustViewBounds(true);

//        paintingView = new ImageView(context);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);

//        addView(imageView);
//        addView(paintingView);
    }

    public void setImageBitmap(Bitmap bitmap) {
        imageView = new ImageView(context);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        imageView.setLayoutParams(layoutParams);
        imageView.setCropToPadding(true);
        imageView.setAdjustViewBounds(true);

        if (bitmap.getWidth() > bitmap.getHeight()) {
            float i = ((float)getMeasuredWidth())/((float)bitmap.getWidth());
            float imageHeight = i * (bitmap.getHeight());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(getMeasuredWidth(), (int) imageHeight));
        } else if (bitmap.getWidth() < bitmap.getHeight()) {
            float j = ((float)getMeasuredHeight())/((float)bitmap.getHeight());
            float imageWidth = j * (bitmap.getWidth());
            imageView.setLayoutParams(new LinearLayout.LayoutParams((int) imageWidth, getMeasuredHeight()));
        }

        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener(imageDrawListener);

        addView(imageView);

//        paintingView.setImageBitmap(bitmap);
//        paintingView.setBackgroundColor(Color.BLACK);
//        paintingView.setAlpha(0.9f);

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
        }

//        int imageWidth = imageView.getMeasuredWidth();
//        int imageHeight = imageView.getMeasuredHeight();
//
//        imageView.measure(MeasureSpec.makeMeasureSpec(imageWidth, MeasureSpec.getMode(widthMeasureSpec)), MeasureSpec.makeMeasureSpec(imageHeight, MeasureSpec.getMode(heightMeasureSpec)));
//        paintingView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)), MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)));
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (imageView != null) {
            int ivl = (getMeasuredWidth() - imageView.getMeasuredWidth())/2;
            int ivt = (getMeasuredHeight() - imageView.getMeasuredHeight())/2;
            int ivr = imageView.getMeasuredWidth();
            int ivb = imageView.getMeasuredHeight();
            imageView.layout(ivl, ivt, ivr + ivl, ivb + ivt);
            Log.e("=======", "width: " + getMeasuredWidth() + ", height: " + getMeasuredHeight());
            Log.e("=======", "ivl: " + ivl + ", ivt: " + ivt + ", ivr: " + ivr + ", ivb: " + ivb );
        }
//        int pvh = paintingView.getMeasuredHeight();
//        int pvw = paintingView.getMeasuredWidth();
//        paintingView.layout(ivl, ivt, ivr, ivb);
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

    private View.OnTouchListener imageDrawListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
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
    };

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                drawStart(event.getX(), event.getY());
//                Log.e("========", "touch DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e("========", "touch MOVE");
//                drawMove(event.getX(), event.getY());
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e("========", "touch UP");
//                drawEnd();
//                break;
//        }
//        return true;
//    }

    public void undoDrawing() {
        paths.clear();
        pathsMap.remove(count);
        if (count > 0) {
            count--;
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

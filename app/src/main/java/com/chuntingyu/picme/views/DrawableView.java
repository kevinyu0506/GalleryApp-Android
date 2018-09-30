package com.chuntingyu.picme.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class DrawableView extends ImageView {
    public DrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                drawStart();
                Log.e("========", "touch DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("========", "touch MOVE");
//                drawMove();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("========", "touch UP");
//                drawEnd();
                break;
        }

        return true;
    }
}

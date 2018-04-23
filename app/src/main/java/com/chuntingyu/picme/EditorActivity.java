package com.chuntingyu.picme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yalantis.ucrop.UCrop;

public class EditorActivity extends AppCompatActivity{

    int album;
    int index;
    Uri uri;
    MyCanvasView myCanvasView;
    ViewTarget viewTarget;
    Button btn;
    Bitmap mBitmap = null;

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        img = (ImageView)findViewById(R.id.img);
        btn = (Button) findViewById(R.id.btn);

        album = getIntent().getIntExtra("album", 0);
        index = getIntent().getIntExtra("value", 0);

        uri = Uri.parse("file://" + MainActivity.al_images.get(album).getAl_imagepath().get(index));

//        UCrop.of(uri, uri)
////                .withAspectRatio(16, 9)
//                .withMaxResultSize(350, 350)
//                .start(this);


//        myCanvasView = new MyCanvasView(this);

        Glide.with(this).asBitmap().load(uri).into(img);

//        myCanvasView = new MyCanvasView(this);
//        myCanvasView.setTest(9);
//
//        loadImageSimpleTarget();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


//    private BaseTarget target = new BaseTarget<Bitmap>() {
//        @Override
//        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//            // do something with the bitmap
////            myCanvasView = new MyCanvasView(getApplicationContext());
////            myCanvasView.setPicture(bitmap.getBitmap());
//            if (bitmap != null){
//
//                mBitmap = bitmap.copy(bitmap.getConfig(),true);
//
////                myCanvasView.setmBitmap(bitmap.copy(bitmap.getConfig(),true));
//                myCanvasView.setmBitmap(mBitmap);
//
//            } else {
//
//            }
//
//        }
//
//        @Override
//        public void getSize(SizeReadyCallback cb) {
//            cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL);
//        }
//
//        @Override
//        public void removeCallback(SizeReadyCallback cb) {}
//    };
//
//
//    private void loadImageSimpleTarget() {
//        Glide.with(this) // could be an issue!
//                .asBitmap()
//                .load(uri)
//                .into(target);
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//            final Uri resultUri = UCrop.getOutput(data);
//
//            Glide.with(this).load(resultUri).into(img);
//
//        } else if (resultCode == UCrop.RESULT_ERROR) {
//            final Throwable cropError = UCrop.getError(data);
//        }
//    }


}

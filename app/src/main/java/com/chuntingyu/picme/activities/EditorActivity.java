package com.chuntingyu.picme.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chuntingyu.picme.R;
import com.chuntingyu.picme.views.DrawableView;

import java.util.UUID;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class EditorActivity extends AppCompatActivity{
    int album;
    int index;
    Uri uri;

    DrawableView img;
    RelativeLayout output;
    Canvas canvas;
    Paint paint;
    Path path;

    FloatingActionButton eraseBtn;

    private float smallBrush, mediumBrush, largeBrush;
    private float brushSize, lastBrushSize;
    private boolean erase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        img = findViewById(R.id.img);
        img.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        eraseBtn = findViewById(R.id.eraseBtn);
        output = findViewById(R.id.output);

        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (erase) {
//                    setErase(false);
                    img.setPaintErase(false);
                }
                else {
//                    setErase(true);
                    img.setPaintErase(true);
                }
            }
        });

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        album = getIntent().getIntExtra("album", 0);
        index = getIntent().getIntExtra("value", 0);

        uri = Uri.parse("file://" + AlbumActivity.imagePaths.get(album).getImagePaths().get(index));

//        UCrop.of(uri, uri)
////                .withAspectRatio(16, 9)
//                .withMaxResultSize(350, 350)
//                .start(this);

        loadImageSimpleTarget();

        initPaint();

        initFabMenu();
    }

    private void initFabMenu() {

        final FABsMenu menu = findViewById(R.id.fabs_menu);
        menu.setMenuButtonIcon(R.drawable.ic_brush_white_24dp);
        menu.setMenuButtonColor(getResources().getColor(R.color.fabColor1));
        menu.setMenuButtonRippleColor(getResources().getColor(R.color.fabColor1a));
        menu.setMenuListener(new FABsMenuListener() {
            // You don't need to override all methods. Just the ones you want.

            @Override
            public void onMenuClicked(FABsMenu fabsMenu) {
                super.onMenuClicked(fabsMenu);
                // Default implementation opens the menu on click
//                setErase(false);
                img.setPaintErase(false);
            }

            @Override
            public void onMenuCollapsed(FABsMenu fabsMenu) {
                super.onMenuCollapsed(fabsMenu);
            }

            @Override
            public void onMenuExpanded(FABsMenu fabsMenu) {
                super.onMenuExpanded(fabsMenu);
            }
        });

        TitleFAB painterSize = findViewById(R.id.painter_size);
        painterSize.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Dialog painterDialog = new Dialog(EditorActivity.this);
                painterDialog.setTitle("Painter size:");
                painterDialog.setContentView(R.layout.painter_chooser);

                ImageButton smallBtn = (ImageButton)painterDialog.findViewById(R.id.small_brush);
                smallBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        paint.setStrokeWidth(smallBrush);
                        painterDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton mediumBtn = (ImageButton)painterDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        paint.setStrokeWidth(mediumBrush);
                        painterDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton largeBtn = (ImageButton)painterDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        paint.setStrokeWidth(largeBrush);
                        painterDialog.dismiss();
                        menu.collapse();
                    }
                });

                painterDialog.show();

            }
        });
        TitleFAB colorPalette = findViewById(R.id.color_palette);
        colorPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog colorDialog = new Dialog(EditorActivity.this);
                colorDialog.setTitle("Painter color:");
                colorDialog.setContentView(R.layout.color_chooser);

                ImageButton color1Btn = (ImageButton)colorDialog.findViewById(R.id.color_1);
                color1Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
//                        setErase(false);
                        img.setPaintErase(false);
                        img.setPaintColor(getResources().getColor(R.color.painterColor1));
//                        paint.setColor(getResources().getColor(R.color.painterColor1));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color2Btn = (ImageButton)colorDialog.findViewById(R.id.color_2);
                color2Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
//                        setErase(false);
                        img.setPaintErase(false);
                        img.setPaintColor(getResources().getColor(R.color.painterColor2));
//                        paint.setColor(getResources().getColor(R.color.painterColor2));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color3Btn = (ImageButton)colorDialog.findViewById(R.id.color_3);
                color3Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
//                        setErase(false);
                        img.setPaintErase(false);
                        img.setPaintColor(getResources().getColor(R.color.painterColor3));
//                        paint.setColor(getResources().getColor(R.color.painterColor3));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color4Btn = (ImageButton)colorDialog.findViewById(R.id.color_4);
                color4Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
//                        setErase(false);
                        img.setPaintErase(false);
                        img.setPaintColor(getResources().getColor(R.color.painterColor4));
//                        paint.setColor(getResources().getColor(R.color.painterColor4));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color5Btn = (ImageButton)colorDialog.findViewById(R.id.color_5);
                color5Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
//                        setErase(false);
                        img.setPaintErase(false);
                        img.setPaintColor(getResources().getColor(R.color.painterColor5));
//                        paint.setColor(getResources().getColor(R.color.painterColor5));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color6Btn = (ImageButton)colorDialog.findViewById(R.id.color_6);
                color6Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
//                        setErase(false);
                        img.setPaintErase(false);
                        img.setPaintColor(getResources().getColor(R.color.painterColor6));
//                        paint.setColor(getResources().getColor(R.color.painterColor6));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                colorDialog.show();

            }
        });

        TitleFAB savePhoto = findViewById(R.id.save_photo);
        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(EditorActivity.this);
                saveDialog.setTitle("Save drawing");
                saveDialog.setMessage("Save drawing to device Gallery?");
                saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        //save drawing
                        output.setDrawingCacheEnabled(true);
                        String imgSaved = MediaStore.Images.Media.insertImage(
                                getContentResolver(), output.getDrawingCache(),
                                UUID.randomUUID().toString()+".png", "drawing");

                        output.destroyDrawingCache();
                    }
                });
                saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                saveDialog.show();
            }
        });

        TitleFAB textBtn = findViewById(R.id.text_field);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setErase(true);
                img.setPaintErase(true);
                menu.collapse();
            }
        });

        TitleFAB undoBtn = findViewById(R.id.undo);
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.undoDrawing();
//                menu.collapse();
            }
        });
    }

    private void initPaint(){
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        paint = new Paint();
        paint.setColor(Color.parseColor("#fff59d"));
        path = new Path();
        // Smoothes out edges of what is drawn without affecting shape.
        paint.setAntiAlias(true);
        // Dithering affects how colors with higher-precision device
        // than the are down-sampled.
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE); // default: FILL
        paint.setStrokeJoin(Paint.Join.ROUND); // default: MITER
        paint.setStrokeCap(Paint.Cap.ROUND); // default: BUTT
        paint.setStrokeWidth(brushSize); // default: Hairline-width (really thin)
    }

//    private void undoDrawing() {
//        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
//        img.invalidate();

//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        paint.setColor(getResources().getColor(R.color.black));
//        for (Path p : paths) {
//            canvas.drawPath(p, paint);
//            img.invalidate();
//        }
//        canvas.drawPath(paths.get(paths.size()-1),paint);
//        img.invalidate();
//
//        paths.clear();
//        paint.setXfermode(null);
//    }

    private void loadImageSimpleTarget() {
        Glide.with(this) // could be an issue!
                .load(uri)
                .asBitmap()
//                .into(target);
                .into(img);
    }

    public void setErase(boolean isErase){
        //set erase true or false
        erase = isErase;
        if(erase) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

//            final FABsMenu menu = findViewById(R.id.fabs_menu);
//            menu.setMenuButtonIcon(R.drawable.ic_eraser_variant_white_24dp);
//            menu.setMenuButtonColor(getResources().getColor(R.color.fabEraser));
//            menu.setMenuButtonRippleColor(getResources().getColor(R.color.fabEraser));

//            statusBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_brush_white_24dp));
//            statusBtn.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.fabColor1))));

        } else {
            paint.setXfermode(null);

//            final FABsMenu menu = findViewById(R.id.fabs_menu);
//            menu.setMenuButtonIcon(R.drawable.ic_brush_white_24dp);
//            menu.setMenuButtonColor(getResources().getColor(R.color.fabColor1));
//            menu.setMenuButtonRippleColor(getResources().getColor(R.color.fabColor1a));

//            statusBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_eraser_variant_white_24dp));
//            statusBtn.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.fabEraser))));

        }
    }

//    public void setBrushSize(float newSize){
//        //update size
//        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
//        brushSize = pixelAmount;
//        paint.setStrokeWidth(brushSize);
//    }
//
//    public void setLastBrushSize(float lastSize){
//        lastBrushSize=lastSize;
//    }
//    public float getLastBrushSize(){
//        return lastBrushSize;
//    }
}

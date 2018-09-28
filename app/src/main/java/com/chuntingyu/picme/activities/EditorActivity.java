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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.chuntingyu.picme.MyCanvasView;
import com.chuntingyu.picme.R;

import java.util.UUID;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class EditorActivity extends AppCompatActivity{

    int album;
    int index;
    Uri uri;

    ImageView img;
    ImageView img2;
    RelativeLayout output;

    MyCanvasView photoCanvas;

    Bitmap imageBitmap;
    Bitmap emptyBitmap;
    Canvas canvas;
    Paint paint;
    Path path;

    FloatingActionButton eraseBtn;

    private float smallBrush, mediumBrush, largeBrush;
    private float brushSize, lastBrushSize;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private boolean erase=false;

//    private ArrayList<Path> paths = new ArrayList<Path>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        img = findViewById(R.id.img);
        img.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        img2 = findViewById(R.id.img2);
        eraseBtn = findViewById(R.id.eraseBtn);
        output = findViewById(R.id.output);
//        photoCanvas = findViewById(R.id.photo_canvas);

        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (erase) {
                    setErase(false);
                }
                else {
                    setErase(true);
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
                setErase(false);
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
                        setErase(false);
                        paint.setColor(getResources().getColor(R.color.painterColor1));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color2Btn = (ImageButton)colorDialog.findViewById(R.id.color_2);
                color2Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        setErase(false);
                        paint.setColor(getResources().getColor(R.color.painterColor2));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color3Btn = (ImageButton)colorDialog.findViewById(R.id.color_3);
                color3Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        setErase(false);
                        paint.setColor(getResources().getColor(R.color.painterColor3));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color4Btn = (ImageButton)colorDialog.findViewById(R.id.color_4);
                color4Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        setErase(false);
                        paint.setColor(getResources().getColor(R.color.painterColor4));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color5Btn = (ImageButton)colorDialog.findViewById(R.id.color_5);
                color5Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        setErase(false);
                        paint.setColor(getResources().getColor(R.color.painterColor5));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color6Btn = (ImageButton)colorDialog.findViewById(R.id.color_6);
                color6Btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        setErase(false);
                        paint.setColor(getResources().getColor(R.color.painterColor6));
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
                setErase(true);
                menu.collapse();
            }
        });

        TitleFAB undoBtn = findViewById(R.id.undo);
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                undoDrawing();
                img.invalidate();

                menu.collapse();
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

        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

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
                        img.invalidate();
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
        });
    }

    private void touchStart(float x, float y) {
//        paths.clear();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            // Reset mX and mY to the last drawn point.
            mX = x;
            mY = y;
            // Save the path in the extra bitmap,
            // which we access through its canvas.

//            paths.add(path);

//            for (Path p : paths) {
//                canvas.drawPath(p, paint);
//            }

            canvas.drawPath(path, paint);
        }
    }

    private void touchUp() {
        // Reset the path so it doesn't get drawn again.
//        paths.add(path);
        path.reset();
    }

    private void undoDrawing() {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
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
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>(500, 500) {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//            imageBitmap = bitmap;
////            emptyBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), imageBitmap.getConfig());
//            emptyBitmap = makeTransparentBitmap(imageBitmap, 0);
//            canvas = new Canvas(emptyBitmap);
//            canvas.drawBitmap(emptyBitmap, 0,0, paint);
//
//            img.setImageBitmap(emptyBitmap);
//            img2.setImageBitmap(imageBitmap);

            photoCanvas = findViewById(R.id.photo_canvas);
            photoCanvas.setmBitmap(bitmap);

        }
    };

    private void loadImageSimpleTarget() {
        Glide.with(this) // could be an issue!
                .load(uri)
                .asBitmap()
                .into(target);
//                .into(photoCanvas);
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

    private static Bitmap makeTransparentBitmap(Bitmap bmp, int alpha) {
        Bitmap transBmp = Bitmap.createBitmap(bmp.getWidth(),
                bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBmp);
        final Paint paint = new Paint();
        paint.setAlpha(alpha);
        canvas.drawBitmap(bmp, 0, 0, paint);
        return transBmp;
    }

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

package com.chuntingyu.picme;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

import com.bumptech.glide.request.animation.GlideAnimation;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class EditorActivity extends AppCompatActivity{

    int album;
    int index;
    Uri uri;
    Button btn;

    ImageView img;
    ImageView img2;
    RelativeLayout output;

    Bitmap bmp;
    Bitmap alteredBitmap;
    Canvas canvas;
    Paint paint;
    Path path;
    RadioRealButtonGroup group;

    private float smallBrush, mediumBrush, largeBrush;
    private float brushSize, lastBrushSize;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private boolean erase=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        img = (ImageView)findViewById(R.id.img);
        img.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        img2 = (ImageView)findViewById(R.id.img2);
        btn = (Button) findViewById(R.id.btn);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        final RadioRealButton button1 = (RadioRealButton) findViewById(R.id.color1);
        final RadioRealButton button2 = (RadioRealButton) findViewById(R.id.color2);
        final RadioRealButton button3 = (RadioRealButton) findViewById(R.id.color3);
        final RadioRealButton button4 = (RadioRealButton) findViewById(R.id.color4);
        final RadioRealButton button5 = (RadioRealButton) findViewById(R.id.color5);

        group = (RadioRealButtonGroup)findViewById(R.id.group);
        group.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {

                int id = button.getId();
                setErase(false);

                switch (id){
                    case R.id.color1:
                        paint.setColor(Color.parseColor("#fff59d"));
                        break;
                    case R.id.color2:
                        paint.setColor(Color.parseColor("#e6ee9c"));
                        break;
                    case R.id.color3:
                        paint.setColor(Color.parseColor("#c5e1a5"));
                        break;
                    case R.id.color4:
                        paint.setColor(Color.parseColor("#a5d6a7"));
                        break;
                    case R.id.color5:
                        paint.setColor(Color.parseColor("#80cbc4"));
                        break;

                }
            }
        });


        album = getIntent().getIntExtra("album", 0);
        index = getIntent().getIntExtra("value", 0);

        uri = Uri.parse("file://" + MainActivity.al_images.get(album).getAl_imagepath().get(index));

//        UCrop.of(uri, uri)
////                .withAspectRatio(16, 9)
//                .withMaxResultSize(350, 350)
//                .start(this);

        loadImageSimpleTarget();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setErase(true);
            }
        });

        initPaint();

        initFabMenu();

    }

    private void initFabMenu() {

        final FABsMenu menu = findViewById(R.id.fabs_menu);
        menu.setMenuButtonIcon(R.drawable.ic_add_white_24dp);
        menu.setMenuButtonColor(getResources().getColor(R.color.fabColor1));
        menu.setMenuButtonRippleColor(getResources().getColor(R.color.fabColor1a));
        menu.setMenuListener(new FABsMenuListener() {
            // You don't need to override all methods. Just the ones you want.

            @Override
            public void onMenuClicked(FABsMenu fabsMenu) {
                super.onMenuClicked(fabsMenu);
                // Default implementation opens the menu on click

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
                    }
                });

                ImageButton mediumBtn = (ImageButton)painterDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        paint.setStrokeWidth(mediumBrush);
                        painterDialog.dismiss();
                    }
                });

                ImageButton largeBtn = (ImageButton)painterDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        paint.setStrokeWidth(largeBrush);
                        painterDialog.dismiss();
                    }
                });

                painterDialog.show();

            }
        });
        TitleFAB colorPalette = findViewById(R.id.color_palette);
        colorPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        TitleFAB savePhoto = findViewById(R.id.save_photo);
        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    private SimpleTarget target = new SimpleTarget<Bitmap>(500, 500) {

        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

            bmp = bitmap;
//            alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
            alteredBitmap = makeTransparentBitmap(bmp, 0);
            canvas = new Canvas(alteredBitmap);
            canvas.drawBitmap(alteredBitmap, 0,0, paint);

            img.setImageBitmap(alteredBitmap);
            img2.setImageBitmap(bmp);


        }

    };

    private void touchStart(float x, float y) {
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
            canvas.drawPath(path, paint);
        }
    }

    private void touchUp() {
        // Reset the path so it doesn't get drawn again.
        path.reset();
    }


    private void loadImageSimpleTarget() {
        Glide.with(this) // could be an issue!
                .load(uri)
                .asBitmap()
                .into(target);
    }

    public void setErase(boolean isErase){
        //set erase true or false
        erase = isErase;
        if(erase) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            paint.setXfermode(null);
        }
    }

    public void setBrushSize(float newSize){
        //update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        paint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

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

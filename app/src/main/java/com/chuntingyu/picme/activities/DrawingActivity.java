package com.chuntingyu.picme.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chuntingyu.picme.R;
import com.chuntingyu.picme.views.EditableView;

import java.util.UUID;

import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

public class DrawingActivity extends AppCompatActivity {
    private int album;
    private int index;
    private Uri uri;
    private EditableView drawingImage;
    private FloatingActionButton eraseBtn;
    private boolean erase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawingImage = findViewById(R.id.drawing_image);
        drawingImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        eraseBtn = findViewById(R.id.drawing_erase_btn);

        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (erase) {
                    drawingImage.setPaintErase(false);
                } else {
                    drawingImage.setPaintErase(true);
                }
            }
        });

        album = getIntent().getIntExtra("album", 0);
        index = getIntent().getIntExtra("value", 0);

        uri = Uri.parse("file://" + AlbumActivity.imagePaths.get(album).getImagePaths().get(index));

//        UCrop.of(uri, uri)
////                .withAspectRatio(16, 9)
//                .withMaxResultSize(350, 350)
//                .start(this);

        loadImageSimpleTarget();

        initFabMenu();
    }

    private void initFabMenu() {
        final FABsMenu menu = findViewById(R.id.fab_menu);
        menu.setMenuButtonIcon(R.drawable.ic_brush_white_24dp);
        menu.setMenuButtonColor(getResources().getColor(R.color.fabColor1));
        menu.setMenuButtonRippleColor(getResources().getColor(R.color.fabColor1a));
        menu.setMenuListener(new FABsMenuListener() {
            @Override
            public void onMenuClicked(FABsMenu fabsMenu) {
                super.onMenuClicked(fabsMenu);
                drawingImage.setPaintErase(false);
            }
        });

        TitleFAB painterSize = findViewById(R.id.fab_painter_size);
        painterSize.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Dialog painterDialog = new Dialog(DrawingActivity.this);
                painterDialog.setTitle("Painter size:");
                painterDialog.setContentView(R.layout.painter_chooser);

                ImageView smallBtn = painterDialog.findViewById(R.id.small_brush);
                smallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        paint.setStrokeWidth(smallBrush);
                        drawingImage.setPaintSize(12);
                        painterDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageView mediumBtn = painterDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        paint.setStrokeWidth(mediumBrush);
                        drawingImage.setPaintSize(24);
                        painterDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageView largeBtn = painterDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        paint.setStrokeWidth(largeBrush);
                        drawingImage.setPaintSize(36);
                        painterDialog.dismiss();
                        menu.collapse();
                    }
                });

                painterDialog.show();

            }
        });
        TitleFAB colorPalette = findViewById(R.id.fab_color_palette);
        colorPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog colorDialog = new Dialog(DrawingActivity.this);
                colorDialog.setTitle("Painter color:");
                colorDialog.setContentView(R.layout.color_chooser);

                ImageButton color1Btn = (ImageButton) colorDialog.findViewById(R.id.color_1);
                color1Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawingImage.setPaintErase(false);
                        drawingImage.setPaintColor(getResources().getColor(R.color.painterColor1));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color2Btn = (ImageButton) colorDialog.findViewById(R.id.color_2);
                color2Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawingImage.setPaintErase(false);
                        drawingImage.setPaintColor(getResources().getColor(R.color.painterColor2));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color3Btn = (ImageButton) colorDialog.findViewById(R.id.color_3);
                color3Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawingImage.setPaintErase(false);
                        drawingImage.setPaintColor(getResources().getColor(R.color.painterColor3));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color4Btn = (ImageButton) colorDialog.findViewById(R.id.color_4);
                color4Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawingImage.setPaintErase(false);
                        drawingImage.setPaintColor(getResources().getColor(R.color.painterColor4));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color5Btn = (ImageButton) colorDialog.findViewById(R.id.color_5);
                color5Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawingImage.setPaintErase(false);
                        drawingImage.setPaintColor(getResources().getColor(R.color.painterColor5));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                ImageButton color6Btn = (ImageButton) colorDialog.findViewById(R.id.color_6);
                color6Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawingImage.setPaintErase(false);
                        drawingImage.setPaintColor(getResources().getColor(R.color.painterColor6));
                        colorDialog.dismiss();
                        menu.collapse();
                    }
                });

                colorDialog.show();

            }
        });

        TitleFAB savePhoto = findViewById(R.id.fab_save_photo);
        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(DrawingActivity.this);
                saveDialog.setTitle("Save drawing");
                saveDialog.setMessage("Save drawing to device Gallery?");
                saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //save drawing
                        drawingImage.setDrawingCacheEnabled(true);
                        String imgSaved = MediaStore.Images.Media.insertImage(
                                getContentResolver(), drawingImage.getDrawingCache(),
                                UUID.randomUUID().toString() + ".png", "drawing");

                        drawingImage.destroyDrawingCache();
                    }
                });
                saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                saveDialog.show();
            }
        });

        TitleFAB textBtn = findViewById(R.id.fab_text_field);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingImage.setPaintErase(true);
                menu.collapse();
            }
        });

        TitleFAB undoBtn = findViewById(R.id.fab_undo);
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingImage.undoDrawing();
            }
        });
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            Log.e("====", "load image bitmap success!");
            drawingImage.setImageBitmap(bitmap);
        }
    };

    private void loadImageSimpleTarget() {
        Glide.with(this) // could be an issue!
                .load(uri)
                .asBitmap()
                .into(target);
    }
}

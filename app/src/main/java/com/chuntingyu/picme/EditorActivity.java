package com.chuntingyu.picme;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class EditorActivity extends AppCompatActivity {

    int album;
    int index;
    Uri uri;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        img = (ImageView)findViewById(R.id.img);

        uri = Uri.parse("file://" + MainActivity.al_images.get(album).getAl_imagepath().get(index));

        album = getIntent().getIntExtra("album", 0);
        index = getIntent().getIntExtra("value", 0);

        Glide.with(this).load(uri).into(img);
    }


}

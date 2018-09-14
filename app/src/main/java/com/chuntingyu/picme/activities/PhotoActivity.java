package com.chuntingyu.picme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.chuntingyu.picme.tools.PhotoAdapter;
import com.chuntingyu.picme.R;

/**
 * Created by deepshikha on 20/3/17.
 */

public class PhotoActivity extends AppCompatActivity {

    int position;
    GridView gridView;
    PhotoAdapter adapter;
    TextView titleTxt, startBtnTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        position = getIntent().getIntExtra("value", 0);
        adapter = new PhotoAdapter(this, MainActivity.imagePaths, position);

        initView();
    }

    private void initView() {
        startBtnTxt = findViewById(R.id.start_btn_text);
        gridView = findViewById(R.id.main_recyclerView);
        titleTxt = findViewById(R.id.main_title_text);

        startBtnTxt.setVisibility(View.GONE);
        titleTxt.setVisibility(View.VISIBLE);
        titleTxt.setText(MainActivity.imagePaths.get(position).getFolderString().toUpperCase());

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(photoClickListener);
    }

    private AdapterView.OnItemClickListener photoClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
            intent.putExtra("album", position);
            intent.putExtra("value", i);
            startActivity(intent);
        }
    };
}

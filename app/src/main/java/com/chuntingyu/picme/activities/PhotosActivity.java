package com.chuntingyu.picme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.chuntingyu.picme.tools.GridViewAdapter;
import com.chuntingyu.picme.R;

/**
 * Created by deepshikha on 20/3/17.
 */

public class PhotosActivity extends AppCompatActivity {

    int int_position;
    private GridView gridView;
    GridViewAdapter adapter;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_btn_text).setVisibility(View.GONE);
        gridView = (GridView)findViewById(R.id.main_gridView);
        int_position = getIntent().getIntExtra("value", 0);
        txt = (TextView)findViewById(R.id.main_title_text);
        txt.setVisibility(View.VISIBLE);
        txt.setText(MainActivity.imagePaths.get(int_position).getFolderString().toUpperCase());

        adapter = new GridViewAdapter(this, MainActivity.imagePaths,int_position);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                intent.putExtra("album", int_position);
                intent.putExtra("value", i);
                startActivity(intent);
            }
        });
    }
}

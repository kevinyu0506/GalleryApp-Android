package com.chuntingyu.picme.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chuntingyu.picme.R;
import com.chuntingyu.picme.tools.KYMath;

import static com.chuntingyu.picme.activities.AlbumActivity.imagePaths;

public class PhotoActivity extends AppCompatActivity {
    int albumPosition;
    RecyclerView recyclerView;
    PhotoAdapter adapter;
    LayoutInflater layoutInflater;
    TextView titleTxt, startBtnTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        albumPosition = getIntent().getIntExtra("value", 0);
        adapter = new PhotoAdapter();

        initView();
    }

    private void initView() {
        startBtnTxt = findViewById(R.id.start_btn_text);
        recyclerView = findViewById(R.id.main_recyclerView);
        titleTxt = findViewById(R.id.main_title_text);

        startBtnTxt.setVisibility(View.GONE);
        titleTxt.setVisibility(View.VISIBLE);
        titleTxt.setText(imagePaths.get(albumPosition).getFolderString().toUpperCase());

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new PhotoRecyclerViewDecoration());
        recyclerView.setAdapter(adapter);
    }

    private View.OnClickListener photoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int photoPosition = (Integer) view.getTag();
            Intent intent = new Intent(PhotoActivity.this, EditorActivity.class);
            intent.putExtra("album", albumPosition);
            intent.putExtra("value", photoPosition);
            startActivity(intent);
        }
    };

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
        @Override
        public int getItemCount() {
            return imagePaths.get(albumPosition).getImagePaths().size();
        }
        @NonNull
        @Override
        public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.viewholder_photo, parent, false);
            return new PhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder holder, int photoPosition) {
            holder.root.setTag(albumPosition);
            holder.root.setTag(photoPosition);

            holder.root.setOnClickListener(photoClickListener);

            Glide.with(PhotoActivity.this).load("file://" + imagePaths.get(albumPosition).getImagePaths().get(photoPosition))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                    .centerCrop()
                    .into(holder.photo);
        }
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        View root, rootLayout;
        ImageView photo;

        private PhotoViewHolder(View view) {
            super(view);
            root = view;
            rootLayout = view.findViewById(R.id.root);
            photo = view.findViewById(R.id.photo_displayed_image);

            rootLayout.getLayoutParams().height = KYMath.screenSize().y * 200 / 667;
        }
    }

    private class PhotoRecyclerViewDecoration extends RecyclerView.ItemDecoration {
        int spacing = 15;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.set(spacing, spacing, spacing, spacing);
        }
    }
}

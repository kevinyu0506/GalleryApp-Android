package com.chuntingyu.picme.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chuntingyu.picme.tools.KYMath;
import com.chuntingyu.picme.R;
import com.chuntingyu.picme.models.AlbumModel;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AlbumActivity extends AppCompatActivity {
    public static List<AlbumModel> imagePaths = new ArrayList<>();
    boolean boolean_folder;
    LayoutInflater inflater;
    AlbumAdapter albumAdapter;
    RecyclerView recyclerView;
    Button startBtn;
    TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.main_recyclerView);
        startBtn = findViewById(R.id.start_btn_text);
        pageTitle = findViewById(R.id.main_title_text);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            startBtn.setVisibility(View.VISIBLE);
            pageTitle.setVisibility(View.GONE);
            startBtn.setOnClickListener(startClickListener);
        } else {
            startBtn.setVisibility(View.GONE);
            pageTitle.setVisibility(View.VISIBLE);
            pageTitle.setText("ALBUMS");
            AlbumActivityPermissionsDispatcher.findImagePathWithPermissionCheck(AlbumActivity.this);
        }

        recyclerView.addItemDecoration(new AlbumRecyclerViewDecoration());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void findImagePath() {
        imagePaths.clear();

        int position = 0;
        int column_index_data, column_index_folder_name;
        Uri uri;
        Cursor cursor;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < imagePaths.size(); i++) {
                if (imagePaths.get(i).getFolderString().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }

            if (boolean_folder) {
                List<String> allPaths = new ArrayList<>();
                allPaths.addAll(imagePaths.get(position).getImagePaths());
                allPaths.add(absolutePathOfImage);
                imagePaths.get(position).setImagePaths(allPaths);
            } else {
                List<String> allPaths = new ArrayList<>();
                allPaths.add(absolutePathOfImage);
                AlbumModel image = new AlbumModel();
                image.setFolderString(cursor.getString(column_index_folder_name));
                image.setImagePaths(allPaths);
                imagePaths.add(image);
            }
        }
        for (int i = 0; i < imagePaths.size(); i++) {
            Log.e("FOLDER", imagePaths.get(i).getFolderString());
            for (int j = 0; j < imagePaths.get(i).getImagePaths().size(); j++) {
                Log.e("FILE", imagePaths.get(i).getImagePaths().get(j));
            }
        }

        albumAdapter = new AlbumAdapter();
        recyclerView.setAdapter(albumAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        AlbumActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private View.OnClickListener startClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlbumActivityPermissionsDispatcher.findImagePathWithPermissionCheck(AlbumActivity.this);
        }
    };

    private View.OnClickListener folderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            Intent intent = new Intent(AlbumActivity.this, PhotoActivity.class);
            intent.putExtra("value", position);
            startActivity(intent);
        }
    };

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
        @Override
        public int getItemCount() {
            return imagePaths.size();
        }

        @NonNull
        @Override
        public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.viewholder_album, parent, false);
            return new AlbumViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
            AlbumModel albumModel = imagePaths.get(position);
            holder.root.setTag(position);
            holder.folderName.setText(albumModel.getFolderString());
            holder.folderSize.setText(albumModel.getImagePaths().size() + "");

            holder.root.setOnClickListener(folderClickListener);

            Glide.with(AlbumActivity.this).load("file://" + albumModel.getImagePaths().get(0))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                    .centerCrop()
                    .into(holder.displayedImage);
        }
    }

    private class AlbumViewHolder extends RecyclerView.ViewHolder {
        View root, rootLayout;
        TextView folderName, folderSize;
        ImageView displayedImage;

        private AlbumViewHolder(View view) {
            super(view);

            root = view;
            rootLayout = view.findViewById(R.id.root);
            folderName = view.findViewById(R.id.folder_name_text);
            folderSize = view.findViewById(R.id.folder_count_text);
            displayedImage = view.findViewById(R.id.folder_displayed_image);

//            rootLayout.getLayoutParams().width = KYMath.screenSize().x * 170/375;
            rootLayout.getLayoutParams().height = KYMath.screenSize().y * 200 / 667;
        }
    }

    private class AlbumRecyclerViewDecoration extends RecyclerView.ItemDecoration {
        int spacing = 15;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.set(spacing, spacing, spacing, spacing);
        }
    }
}

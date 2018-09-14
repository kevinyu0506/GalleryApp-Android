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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chuntingyu.picme.tools.KYMath;
import com.chuntingyu.picme.tools.PhotoFolderAdapter;
import com.chuntingyu.picme.R;
import com.chuntingyu.picme.models.ImageModel;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    public static List<ImageModel> imagePaths = new ArrayList<>();
    boolean boolean_folder;
    LayoutInflater inflater;
    //    PhotoFolderAdapter photoFolderAdapter;
    FolderAdapter folderAdapter;
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
            MainActivityPermissionsDispatcher.findImagePathWithPermissionCheck(MainActivity.this);
        }

        recyclerView.addItemDecoration(new FolderRecyclerViewDecoration());
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
                ImageModel image = new ImageModel();
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

        folderAdapter = new FolderAdapter();
        recyclerView.setAdapter(folderAdapter);
        Log.e("==============", "list size:" + imagePaths.size());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private View.OnClickListener startClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivityPermissionsDispatcher.findImagePathWithPermissionCheck(MainActivity.this);
        }
    };

//    private AdapterView.OnItemClickListener imageClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
//            intent.putExtra("value", i);
//            startActivity(intent);
//        }
//    };

    private View.OnClickListener folderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
            intent.putExtra("value", position);
            startActivity(intent);
        }
    };

    private class FolderAdapter extends RecyclerView.Adapter<FolderViewHolder> {
        @Override
        public int getItemCount() {
            return imagePaths.size();
        }

        @NonNull
        @Override
        public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.adapter_photosfolder, parent, false);
            return new FolderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
            ImageModel imageModel = imagePaths.get(position);
            holder.root.setTag(position);
            holder.folderName.setText(imageModel.getFolderString());
            holder.folderSize.setText(imageModel.getImagePaths().size() + "");
            holder.root.setOnClickListener(folderClickListener);

            Glide.with(MainActivity.this).load("file://" + imageModel.getImagePaths().get(0))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                    .centerCrop()
                    .into(holder.displayedImage);
        }
    }

    private class FolderViewHolder extends RecyclerView.ViewHolder {
        View root, rootLayout;
        TextView folderName, folderSize;
        ImageView displayedImage;

        private FolderViewHolder(View view) {
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

    private class FolderRecyclerViewDecoration extends RecyclerView.ItemDecoration {
        int spacing = 15;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.set(spacing, spacing, spacing, spacing);
        }
    }
}

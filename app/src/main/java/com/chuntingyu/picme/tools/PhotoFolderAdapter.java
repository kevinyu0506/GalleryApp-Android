package com.chuntingyu.picme.tools; /**
 * Created by Kevin on 2018/4/20.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chuntingyu.picme.R;
import com.chuntingyu.picme.models.ImageModel;

import java.util.ArrayList;
import java.util.List;


public class PhotoFolderAdapter extends ArrayAdapter<ImageModel> {

    private Context context;
    private ViewHolder viewHolder;
    private List<ImageModel> insidePhotos;

    public PhotoFolderAdapter(Context context, ArrayList<ImageModel> insidePhotos) {
        super(context, R.layout.adapter_photosfolder, insidePhotos);
        this.context = context;
        this.insidePhotos = insidePhotos;
    }

    @Override
    public int getCount() {
        Log.e("ADAPTER LIST SIZE", insidePhotos.size() + "");
        return insidePhotos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (insidePhotos.size() > 0) {
            return insidePhotos.size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_photosfolder, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.folderName = view.findViewById(R.id.folder_name_text);
            viewHolder.folderSize = view.findViewById(R.id.folder_count_text);
            viewHolder.displayedImage = view.findViewById(R.id.folder_displayed_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.folderName.setText(insidePhotos.get(position).getFolderString());
        viewHolder.folderSize.setText(insidePhotos.get(position).getImagePaths().size() + "");

        Glide.with(context).load("file://" + insidePhotos.get(position).getImagePaths().get(0))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .centerCrop()
                .into(viewHolder.displayedImage);

        return view;
    }

    private class ViewHolder {
        TextView folderName, folderSize;
        ImageView displayedImage;
    }
}

package com.chuntingyu.picme.tools; /**
 * Created by Kevin on 2018/4/20.
 */

import android.content.Context;
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


public class PhotoAdapter extends ArrayAdapter<ImageModel> {

    private Context context;
    private ViewHolder viewHolder;
    private List<ImageModel> al_menu;
    private int int_position;

    public PhotoAdapter(Context context, List<ImageModel> al_menu, int int_position) {
        super(context, R.layout.adapter_photosfolder, al_menu);

        this.al_menu = al_menu;
        this.context = context;
        this.int_position = int_position;
    }

    @Override
    public int getCount() {
        Log.e("ADAPTER LIST SIZE", al_menu.get(int_position).getImagePaths().size() + "");
        return al_menu.get(int_position).getImagePaths().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.get(int_position).getImagePaths().size() > 0) {
            return al_menu.get(int_position).getImagePaths().size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_photosfolder, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.folderName = view.findViewById(R.id.folder_name_text);
            viewHolder.folderSize = view.findViewById(R.id.folder_count_text);
            viewHolder.image = view.findViewById(R.id.folder_displayed_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.folderName.setVisibility(View.GONE);
        viewHolder.folderSize.setVisibility(View.GONE);

        Glide.with(context).load("file://" + al_menu.get(int_position).getImagePaths().get(position))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .centerCrop()
                .into(viewHolder.image);

        return view;
    }

    private static class ViewHolder {
        TextView folderName, folderSize;
        ImageView image;
    }
}

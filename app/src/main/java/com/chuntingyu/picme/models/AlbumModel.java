package com.chuntingyu.picme.models;

import java.util.List;

/**
 * Created by Kevin on 2018/4/20.
 */

public class AlbumModel {

    private String folderString;
    private List<String> imagePaths;

    public String getFolderString() {
        return folderString;
    }

    public void setFolderString(String folderString) {
        this.folderString = folderString;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
package com.assignment.androidphoto91.components;


import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class Photo implements Comparable<Photo> {
    private String name;
    private String path;
    private String encodedImage;
    private ArrayList<Tag> tagList;
    private ImageView imageView;

    private String albumName;
    public Photo(String name, String path, String encodedImage,String albumName) {
        this.name = name;
        this.path = path;
        this.encodedImage = encodedImage;
        this.tagList = new ArrayList<>();
        this.albumName = albumName;
    }

    public Bitmap getBitMap()
    {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public String getPath() {
        return path;
    }

    public String getAlbumName(){ return albumName;}
    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public ArrayList<Tag> getTagList() { return tagList; }

    public void setTagList(ArrayList<Tag> tagList) { this.tagList = tagList; }

    public void addTag(Tag tag) {
        tagList.add(tag);
    }

    public void removeTag(Tag tag) {
        tagList.remove(tag);
    }

    @Override
    public boolean equals(Object other) {
        if (other  != null && (other instanceof Photo)) {
            Photo otherPhoto = (Photo) other;
            return (this.name.toLowerCase().equals(otherPhoto.getName().toLowerCase()) && this.name.toLowerCase().equals(otherPhoto.getName().toLowerCase()));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format(this.name);
    }

    @Override
    public int compareTo(Photo o) {
        return (this.name.compareToIgnoreCase(o.name));
    }
}

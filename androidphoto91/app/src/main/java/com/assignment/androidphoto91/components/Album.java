package com.assignment.androidphoto91.components;

import com.assignment.androidphoto91.components.Photo;

import java.util.ArrayList;
import java.util.Locale;

public class Album implements Comparable<Album> {
    private String name;
    private ArrayList<Photo> photoList;

    public Album(String name) {
        this.name = name.trim();
        this.photoList = new ArrayList<>();
    }

    public String getName() {
        return name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public ArrayList<Photo> getPhotoList() {
        return this.photoList;
    }

    public void setPhotoList(ArrayList<Photo> photoList) {
        this.photoList = photoList;
    }

    public void addPhoto(Photo photo) {
        this.photoList.add(photo);
    }

    public void removePhoto(Photo photo) {
        this.photoList.remove(photo);
    }

    @Override
    public boolean equals(Object other) {
            if (other != null) {
                if (other instanceof Album) {
                    Album otherAlbum = (Album) other;
                    String thisAlbumName = this.name.toLowerCase();
                    String otherAlbumName = otherAlbum.getName().toLowerCase();
                    return (thisAlbumName.equals(otherAlbumName));
                }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name.toUpperCase();
    }

    @Override
    public int compareTo(Album o) {
        return (this.name.compareToIgnoreCase(o.name));
    }
}

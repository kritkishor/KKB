package com.assignment.androidphoto91.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.assignment.androidphoto91.R;
import com.assignment.androidphoto91.components.Album;
import com.assignment.androidphoto91.components.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class MovePhotoActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView_moveAlbum;
    private ArrayList<Album> albumList;
    private ArrayList<Album> displayAlbumList;
    private int albumSelected;
    private Album moveToAlbum;

    private ArrayAdapter arrayAlbumAdapter;

    private Button movePhotoMove;
    private Button movePhotoCancel;

    private ArrayList<Photo> photoList;
    private int photoSelectedPosition;
    private Photo photoSelected;

    private Album currentAlbum;

    public void saveAlbumData() {
        Log.d("Main Gallery ", "here0:" + MainActivity.SAVEDPREFERENCES);
        if(albumList != null)
        {
            // Setup reference to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SAVEDPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

            // Convert albumList object to JSON
            Gson gson = new Gson();
            String json = gson.toJson(albumList);

            //    Log.d("Main Gallery ", "here1");

            // Save JSON file to SharedPreferences
            sharedPreferencesEditor.putString(MainActivity.SAVEDPREFERENCES_ALBUMLIST, json);
            //      Log.d("Main Gallery ", "here2");
            sharedPreferencesEditor.apply();
        /*    Log.d("Main Gallery ", "here3");
            Log.d("Gallery main save","Albums: " + albumList.toString());
            for(Album album: albumList)
            {
                Log.d("gallery main Photos",album.getPhotoList().toString());
            }
        */
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_photo);

        // Add top navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        albumList = MainActivity.albumList;
        if(albumList == null)
        {

            // Setup reference to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SAVEDPREFERENCES, MODE_PRIVATE);

            // Convert JSON to albumList object
            Gson gson = new Gson();
            String json = sharedPreferences.getString(MainActivity.SAVEDPREFERENCES_ALBUMLIST, null);
            Type type = (new TypeToken<ArrayList<Album>>(){}).getType();
            albumList = gson.fromJson(json, type);
            if(albumList == null)
            {
                albumList = new ArrayList<Album>();
            }

        }
        // Setup Buttons
        movePhotoMove = findViewById(R.id.btn_movePhotoMove);
        movePhotoCancel = findViewById(R.id.btn_movePhotoCancel);
        autoCompleteTextView_moveAlbum = findViewById(R.id.autoCompleteTextView_moveAlbum);

        movePhotoMove.setOnClickListener(v -> movePhoto());
        movePhotoCancel.setOnClickListener(v -> cancelMovePhoto());

        // Get the value passed by the PhotoGallery
        Intent intent = getIntent();
        photoSelectedPosition = intent.getIntExtra(MainActivity.EXTRA_PHOTO_SELECTED, -1);
        albumSelected = intent.getIntExtra(MainActivity.EXTRA_ALBUM_SELECTED, -1);

        currentAlbum = PhotoGalleryActivity.currentAlbum;
        if(currentAlbum == null && albumSelected > -1 && albumList != null)
        {
            currentAlbum = albumList.get(albumSelected);
        }
        // Get the list of photos in the current album
        photoList = currentAlbum.getPhotoList();
        if(photoList == null && albumList != null && albumSelected > -1)
        {
            photoList = albumList.get(albumSelected).getPhotoList();
        }

        // Get the selected photo from PhotoGallery
        if(photoSelectedPosition > -1 && photoList != null)
        {
            photoSelected = photoList.get(photoSelectedPosition);
        }

        // Copy Album List
        displayAlbumList = new ArrayList<>(MainActivity.albumList);

        if (displayAlbumList != null && currentAlbum != null) {
            displayAlbumList.remove(currentAlbum);
            arrayAlbumAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, displayAlbumList);

            if (arrayAlbumAdapter != null && autoCompleteTextView_moveAlbum != null) {
                autoCompleteTextView_moveAlbum.setAdapter(arrayAlbumAdapter);
                autoCompleteTextView_moveAlbum.setOnItemClickListener((parent, view, position, id) -> {
                    moveToAlbum = (Album) parent.getItemAtPosition(position);
                });
            }
        }
    }

    public void movePhoto() {
        // Come back here and clean up
        if (!moveToAlbum.getPhotoList().contains(photoSelected)) {
            moveToAlbum.addPhoto(photoSelected);
            currentAlbum.removePhoto(photoSelected);
            saveAlbumData();
        } else {
            Toast.makeText(getApplicationContext(), "Photo already exists in target album", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(MovePhotoActivity.this, PhotoGalleryActivity.class);
        intent.putExtra(MainActivity.EXTRA_ALBUM_SELECTED, albumSelected);
        startActivity(intent);
    }

    public void cancelMovePhoto() {
        Intent intent = new Intent(MovePhotoActivity.this, PhotoGalleryActivity.class);
        intent.putExtra(MainActivity.EXTRA_ALBUM_SELECTED, albumSelected);
        startActivity(intent);
    }
}
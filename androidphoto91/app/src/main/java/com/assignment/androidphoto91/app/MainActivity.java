package com.assignment.androidphoto91.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.assignment.androidphoto91.R;
import com.assignment.androidphoto91.app.controllers.RecycleViewAlbumAdapter;
import com.assignment.androidphoto91.app.dialogs.AddAlbumDialogFragment;
import com.assignment.androidphoto91.components.Album;
import com.assignment.androidphoto91.components.AutoCompleteArray;
import com.assignment.androidphoto91.components.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements RecycleViewAlbumAdapter.OnItemClickListener, AddAlbumDialogFragment.AddAlbumDialogListener {

    private boolean isNewClick = true;
    public static final String EXTRA_ALBUM_SELECTED = "com.assignment.androidphoto91.albumSelected";
    public static final String EXTRA_PHOTO_SELECTED = "com.assignment.androidphoto91.photoSelected";
    public static final String SAVEDPREFERENCES_ALBUMLIST = "com.assignment.androidphoto91.albumList";
    public static final String SAVEDPREFERENCES_LOCATIONLIST = "com.assignment.androidphoto91.locationList";
    public static final String SAVEDPREFERENCES_PERSONLIST = "com.assignment.androidphoto91.personList";
    public static final String SAVEDPREFERENCES = "com.assignment.androidphoto91.sharedPreferences";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Button btn_search;
    private Button btn_add;
    private Button btn_remove;
    private Button btn_rename;
    private Button btn_open;

    static public ArrayList<Album> albumList;
    private int currentItemSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Look for Permissions
        getPermission();

        // Load album Data
        loadSavedAlbumData();

        // Initialize RecyclerList current selection to nothing
        currentItemSelected = -1;

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView_albumsList);
        recyclerView.setHasFixedSize(true);

        // Setup RecyclerView / Linear Layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Setup RecyclerView / Adapter
        mAdapter = new RecycleViewAlbumAdapter(albumList, MainActivity.this, MainActivity.this);
        recyclerView.setAdapter(mAdapter);

        // Setup button references
        btn_search = findViewById(R.id.btn_search);
        btn_add = findViewById(R.id.btn_addAlbum);
        btn_remove = findViewById(R.id.btn_removeAlbum);
        btn_rename = findViewById(R.id.btn_renameAlbum);
        btn_open = findViewById(R.id.btn_openAlbum);

        // Listeners // =======================
        // ====================================

        // SEARCH: Setup the listener action
        btn_search.setOnClickListener(v -> openSearch());

        // DELETE: Setup the listener action
        btn_open.setOnClickListener(v -> openPhotoGallery());

        // DELETE: Setup the listener action
        btn_remove.setOnClickListener(v -> removeAlbum());

        // ADD: Setup the listener action
        btn_add.setOnClickListener(v -> addAlbumDialog());

        // RENAME: Setup the listener action
        btn_rename.setOnClickListener(v -> renameAlbumDialog());
    }

    public void loadSavedAlbumData() {

        // Setup reference to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SAVEDPREFERENCES, MODE_PRIVATE);
        // Convert JSON to albumList object
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SAVEDPREFERENCES_ALBUMLIST, null);
        Type type = (new TypeToken<ArrayList<Album>>(){}).getType();

        albumList = gson.fromJson(json, type);
        // If no data was retrieved initialize a new Album array
        if (albumList == null)
        {
            albumList = new ArrayList<Album>();
        }

        if(albumList != null)
        {
            Log.d("Main Load","Albums: " + albumList.toString());
            for(Album album: albumList)
            {
                Log.d("Main Loaded photos",album.getPhotoList().toString());
            }

        }

    }
    public void saveAlbumData() {

        if(albumList != null)
        {
            Log.d("Main Save","Albums: " + albumList.toString());
            for(Album album: albumList)
            {
                Log.d("Main Photos",album.getPhotoList().toString());
            }

        }
        // Setup reference to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SAVEDPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        // Convert albumList object to JSON
        Gson gson = new Gson();
        String json = gson.toJson(albumList);
        // Save JSON file to SharedPreferences
        sharedPreferencesEditor.putString(SAVEDPREFERENCES_ALBUMLIST, json);
        sharedPreferencesEditor.apply();
    }

    private static final int READ_EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 1;
    public void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {

                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);

                if (showRationale) {
                    Toast.makeText(this, "Permission is needed for the app to function", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Read Storage permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

// No need in new version, Kept just in case
    @Override
    protected void onPause() {
        super.onPause();
       // Save Data
        saveAlbumData();
    }

    @Override
    public void onItemClick(int position) {
        // Set the position of the currently selected item
        // in the RecyclerList
        // Set the position of the currently selected item in RecyclerList

        // Background Color for clicked Item:
        // If this a second click reset the background color for previously clicked Item
            if(!isNewClick && currentItemSelected > -1)
            {
                RecyclerView.ViewHolder oldHolder = recyclerView.findViewHolderForAdapterPosition(currentItemSelected);
                oldHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
            isNewClick = false;
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
            currentItemSelected = position;
            holder.itemView.setBackgroundColor(Color.LTGRAY);

    }

    private void addAlbumDialog() {
        DialogFragment addAlbumDialogFragment = new AddAlbumDialogFragment();
        addAlbumDialogFragment.show(getSupportFragmentManager(), "");

    }

    private void renameAlbumDialog() {
        if (currentItemSelected > -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = getLayoutInflater().inflate(R.layout.dialog_add_album, null);
            EditText editText = (EditText) view.findViewById(R.id.editText_albumName);

            builder.setView(view)
                    .setMessage("Rename album")
                    .setCancelable(true)
                    .setNegativeButton("No", (dialog, which) -> {
                        // do nothing
                        dialog.cancel();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Album album = albumList.get(currentItemSelected);
                        String newName = editText.getText().toString().trim();

                        if (album != null) {
                            // Remove selection and resort
                            if (!newName.equals("") && !albumList.contains(new Album(newName))) {
                                album.setName(newName);
                                Collections.sort(albumList);
                                saveAlbumData();
                                // Notify the adapter the data has changed
                              mAdapter.notifyDataSetChanged();
                              //  mAdapter.notifyItemRemoved(currentItemSelected);

                                // Clear selection

                            } else {
                                if (newName.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Album name cannot be empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), newName + " already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void openSearch() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    private void openPhotoGallery() {
        if (currentItemSelected > -1) {
            Intent intent = new Intent(MainActivity.this, PhotoGalleryActivity.class);
            intent.putExtra(EXTRA_ALBUM_SELECTED, currentItemSelected);
            startActivity(intent);
        }
    }

    private void removeAlbum() {
        if (currentItemSelected > -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete album?")
                    .setCancelable(true)
                    .setNegativeButton("No", (dialog, which) -> {
                        // do nothing
                        dialog.cancel();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Album album = albumList.get(currentItemSelected);

                        if (album != null) {
                            // Remove selection and resort
                            albumList.remove(album);
                            Collections.sort(albumList);
                            saveAlbumData();
                            // Notify the adapter the data has changed
                            mAdapter.notifyItemRemoved(currentItemSelected);
                          //  mAdapter.notifyDataSetChanged();
                            // Clear selection
                            currentItemSelected = -1;
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onAddAlbumDialogPositiveClick(DialogFragment dialog) {
        AddAlbumDialogFragment addAlbumDialogFragment = (AddAlbumDialogFragment) dialog;
        String albumName = addAlbumDialogFragment.getAlbumName().trim();
        Album newAlbum = new Album(albumName);

        if (!albumName.equals("") && !albumList.contains(newAlbum)) {
            albumList.add(newAlbum);
            Collections.sort(albumList);
            saveAlbumData();
            mAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(getApplicationContext(), newAlbum.getName() + " already exists", Toast.LENGTH_SHORT).show();
        }
    }
}
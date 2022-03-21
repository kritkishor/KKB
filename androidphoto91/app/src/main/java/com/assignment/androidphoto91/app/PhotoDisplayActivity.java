package com.assignment.androidphoto91.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.androidphoto91.R;
import com.assignment.androidphoto91.components.Album;
import com.assignment.androidphoto91.components.AutoCompleteArray;
import com.assignment.androidphoto91.components.Photo;
import com.assignment.androidphoto91.components.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class PhotoDisplayActivity extends AppCompatActivity {

    private ImageView imageView_photo;
    private Button btn_photoDisplayPrevious;
    private Button btn_photoDisplayNext;
    private Button btn_addLocationTag;
    private Button btn_addPersonTag;
    private Button btn_deleteTag;
    private Button btn_photoDisplayExit;



    private  View oldView;
    private ListView listView_tagList;

    private boolean fromSearch = false;
    private Photo photoSelected;
    private ArrayList<Photo> photoList;
    private int photoSelectedPosition;
    private Album currentAlbum;
    private int albumPositionSelected;
    private String person,location;
    private ArrayAdapter<Tag> tagListArrayAdapter;
    private ArrayList<Tag> tagList;
    private Tag selectedTag;

    private TextView photoNameLabel;
    private TextView photoTagsLabel;
    private ArrayList<Album> albumList;
    private AutoCompleteArray autoCompleteLocationList;
    private AutoCompleteArray autoCompletePersonList;

    String type1,value1,type2,value2,category;
    public void loadSavedAlbumData() {

        albumList = MainActivity.albumList;
        if (albumList == null)
        {
            // Setup reference to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SAVEDPREFERENCES, MODE_PRIVATE);
            // Convert JSON to albumList object
            Gson gson = new Gson();
            String json = sharedPreferences.getString(MainActivity.SAVEDPREFERENCES_ALBUMLIST, null);
            Type type = (new TypeToken<ArrayList<Album>>(){}).getType();

            albumList = gson.fromJson(json, type);
            // If no data was retrieved initialize a new Album array
            if (albumList == null)
            {
                albumList = new ArrayList<Album>();
            }
        }
/*
        if(albumList != null)
        {
            Log.d("Main Load","Albums: " + albumList.toString());
            for(Album album: albumList)
            {
                Log.d("Main Loaded photos",album.getPhotoList().toString());
            }

        }
*/
    }
    public void setCurrentAlbum()
    {
        //Get current albumList
        albumList = MainActivity.albumList;
        if(albumList != null)
        {
            for(Album album: albumList)
            {
                if(album.getName().compareToIgnoreCase(photoSelected.getAlbumName()) == 0)
                {
                    currentAlbum = album;
                }
                else
                {
                    Log.d("DANGER","ANOTHER DANGEROUS TERRITORY");
                }
            }
        }
        else
        {
            loadSavedAlbumData();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);

        //load Data
        loadSavedAlbumData();
        // Add top navigation
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the value passed by the PhotoGallery
        Intent intent = getIntent();
        photoSelectedPosition = intent.getIntExtra(MainActivity.EXTRA_PHOTO_SELECTED, -1);
        if(intent.getStringExtra("search") != null )
        {
            Log.d("here","here");
            fromSearch = true;


            photoList = SearchResultsActivity.searchResults;
            photoSelected = photoList.get(photoSelectedPosition);

           setCurrentAlbum();


            if(intent.getStringExtra("person1") != null ){
                type1 = "person1";
                value1 = intent.getStringExtra("person1");
            }
            if(intent.getStringExtra("person2") != null ){
                type2 = "person2";
                value2 = intent.getStringExtra("person2");
            }
            if(intent.getStringExtra("location1") != null ){
                type1 = "location1";
                value1 = intent.getStringExtra("location1");
            }
            if(intent.getStringExtra("location2") != null ){
                type2 = "location2";
                value2 = intent.getStringExtra("location2");
            }
            if(intent.getStringExtra("category") != null)
            {
                category = intent.getStringExtra("category");
            }
        }
        else
        {

            albumPositionSelected = intent.getIntExtra(MainActivity.EXTRA_ALBUM_SELECTED, -1);

            // Get the current album via static reference
           if(albumPositionSelected > -1 && albumList != null )
           {
               currentAlbum = albumList.get(albumPositionSelected);
           }

            // Get the list of photos in the current album
           if(currentAlbum != null)
           {
               photoList = currentAlbum.getPhotoList();
           }

            // Get the selected photo from PhotoGallery
           if(photoList != null && photoSelectedPosition > -1)
           {
               photoSelected = photoList.get(photoSelectedPosition);
           }
        }

        imageView_photo = findViewById(R.id.imageView_photo);
        photoNameLabel = findViewById(R.id.textView_photoNameLabel);
        photoTagsLabel = findViewById(R.id.textView_photoTagsLabel);

        setPhoto();

        btn_photoDisplayPrevious = findViewById(R.id.btn_photoDisplayPrevious);
        btn_photoDisplayExit = findViewById(R.id.btn_photoDisplayExit);
        btn_photoDisplayNext = findViewById(R.id.btn_photoDisplayNext);
        btn_addLocationTag = findViewById(R.id.btn_addLocationTag);
        btn_addPersonTag = findViewById(R.id.btn_addPersonTag);
        btn_deleteTag = findViewById(R.id.btn_deleteTag);

       if(photoSelected != null)
       {
           tagList = photoSelected.getTagList();
       }


        listView_tagList = findViewById(R.id.listView_tagList);
        if(tagList != null)
        {
            tagListArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagList);
            listView_tagList.setAdapter(tagListArrayAdapter);
            listView_tagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedTag = tagList.get(position);
                    if(oldView != null)
                    {
                        oldView.setBackgroundColor(Color.TRANSPARENT);
                    }

                    view.setBackgroundColor(Color.LTGRAY);
                    oldView = view;
                }
            });
        }

        // Listeners // =======================
        // ====================================

        // PREVIOUS: Setup the listener action
        btn_photoDisplayPrevious.setOnClickListener(v -> navigatePrevious());

        // EXIT: Setup the listener action
        btn_photoDisplayExit.setOnClickListener(v -> navigateExit());

        // NEXT: Setup the listener action
        btn_photoDisplayNext.setOnClickListener(v -> navigateNext());

        // ADD LOCATION TAG: Setup the listener action
        btn_addLocationTag.setOnClickListener(v -> addLocationTag());

        // ADD PERSON TAG: Setup the listener action
        btn_addPersonTag.setOnClickListener(v -> addPersonTag());

        // DELETE TAG: Setup the listener action
        btn_deleteTag.setOnClickListener(v -> removeTag());
    }

    public void saveAlbumData() {

        // Setup reference to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SAVEDPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        // Convert albumList object to JSON
        Gson gson = new Gson();
        String json = gson.toJson(albumList);

        // Save JSON file to SharedPreferences
        sharedPreferencesEditor.putString(MainActivity.SAVEDPREFERENCES_ALBUMLIST, json);
        sharedPreferencesEditor.apply();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Blindly save all data. Perhaps come back here
        // For finer control on saves
        saveAlbumData();
    }


    public void addPersonTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoDisplayActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_generic_edittext, null);
        EditText editText = (EditText) view.findViewById(R.id.editText_entry);
        editText.setHint("Person");

        builder.setView(view)
                .setMessage("Add Person Tag")
                .setCancelable(true)
                .setNegativeButton("No", (dialog, which) -> {
                    // do nothing
                    dialog.cancel();
                })
                .setPositiveButton("Yes", (dialog, which) -> {
                    String newPersonValue = editText.getText().toString().trim();
                    Tag newPersonTag = new Tag("Person", newPersonValue);

                    if (!newPersonValue.equals("") && !tagList.contains(newPersonTag)) {
                        photoSelected.getTagList().add(newPersonTag);
                        saveAlbumData();
                        tagListArrayAdapter.notifyDataSetChanged();
                    } else {
                        if (newPersonValue.equals("")) {
                            Toast.makeText(getApplicationContext(), "Entry cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), newPersonValue + " already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void addLocationTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoDisplayActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_generic_edittext, null);
        EditText editText = (EditText) view.findViewById(R.id.editText_entry);
        editText.setHint("Location");

        builder.setView(view)
                .setMessage("Add Location Tag")
                .setCancelable(true)
                .setNegativeButton("No", (dialog, which) -> {
                    // do nothing
                    dialog.cancel();
                })
                .setPositiveButton("Yes", (dialog, which) -> {
                    String newLocationValue = editText.getText().toString().trim();
                    Tag newLocationTag = new Tag("Location", newLocationValue);

                    if (!newLocationValue.equals("") && !tagList.contains(newLocationTag)) {
                        photoSelected.getTagList().add(newLocationTag);
                        saveAlbumData();
                        tagListArrayAdapter.notifyDataSetChanged();
                    } else {
                        if (newLocationValue.equals("")) {
                            Toast.makeText(getApplicationContext(), "Entry cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), newLocationValue + " already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void removeTag() {
        if (selectedTag != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete tag?")
                    .setCancelable(true)
                    .setNegativeButton("No", (dialog, which) -> {
                        // do nothing
                        dialog.cancel();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        tagList.remove(selectedTag);
                        saveAlbumData();

                        // Notify the adapter the data has changed
                        tagListArrayAdapter.notifyDataSetChanged();

                        // Clear selection
                        selectedTag = null;
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void setPhoto()
    {
      //  Uri uri = Uri.parse(photoSelected.getPath());
     //   imageView_photo.setImageURI(uri);
        imageView_photo.setImageBitmap(photoSelected.getBitMap());
        photoNameLabel.setText(photoSelected.getName());

        /* Replaced with listview so that you can click and delete
        String tags = "";

        for (Tag tag: photoSelected.getTagList())
        {
            tags = tags + tag.getName() + ": " + tag.getValue() + "  ";
        }

        photoTagsLabel.setText(tags);
        */
    }

    public void updateTagListAdapter(Photo photoSelected) {
        tagList = photoSelected.getTagList();
        tagListArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagList);
        listView_tagList.setAdapter(tagListArrayAdapter);
        tagListArrayAdapter.notifyDataSetChanged();
    }

    public void navigateExit() {
        Intent intent;
        if(!fromSearch)
        {
            intent = new Intent(PhotoDisplayActivity.this, PhotoGalleryActivity.class);
            intent.putExtra(MainActivity.EXTRA_ALBUM_SELECTED, albumPositionSelected);
        }
        else
        {

            intent = new Intent(PhotoDisplayActivity.this,SearchResultsActivity.class);
            if(!type1.isEmpty()){
                intent.putExtra(type1,value1);
            }
            if(!type2.isEmpty()){
                intent.putExtra(type2,value2);
            }
            if(!category.isEmpty())
            {
                intent.putExtra("category",category);
            }

        }
        startActivity(intent);
    }

    public void navigatePrevious() {
        if (photoSelectedPosition-1 > -1)
        {
            photoSelectedPosition--;
            photoSelected = photoList.get(photoSelectedPosition);
            setCurrentAlbum();
            setPhoto();
            updateTagListAdapter(photoSelected);
        }
    }

    public void navigateNext() {
        if (photoSelectedPosition+1 < photoList.size())
        {
            photoSelectedPosition++;
            photoSelected = photoList.get(photoSelectedPosition);

           setCurrentAlbum();
            //Uri uri = Uri.parse(photoSelected.getPath());
            //imageView_photo.setImageURI(uri);

        //    photoNameLabel.setText(photoSelected.getName());
            setPhoto();
            updateTagListAdapter(photoSelected);
        }
    }


}
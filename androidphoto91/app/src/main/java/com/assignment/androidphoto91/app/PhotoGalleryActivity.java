package com.assignment.androidphoto91.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.assignment.androidphoto91.R;
import com.assignment.androidphoto91.app.controllers.RecycleViewPhotoAdapter;
import com.assignment.androidphoto91.components.Album;
import com.assignment.androidphoto91.components.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class PhotoGalleryActivity extends AppCompatActivity implements RecycleViewPhotoAdapter.OnItemClickListener {
    private boolean isNewClick = true;
    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_FOLDER_REQUEST = 2;
    private static final String TAG = "PhotoGalleryActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter pAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Button btn_addPhoto;
    private Button btn_removePhoto;
    private Button btn_movePhoto;
    private Button btn_displayPhoto;

    private ArrayList<Photo> photoList;
    private ArrayList<Album> albumList;
    static public Album currentAlbum;
    private int albumSelected;
    private int currentItemSelected;

    private Uri uriToLoad;
    private ImageView imageView;
    private int thumbnail;


    Intent fileChooserIntent;
    Intent folderChooserIntent;

    public static final String SAVEDPREFERENCES_ALBUMLIST = "com.assignment.androidphoto91.albumList";
    public static final String SAVEDPREFERENCES_LOCATIONLIST = "com.assignment.androidphoto91.locationList";
    public static final String SAVEDPREFERENCES_PERSONLIST = "com.assignment.androidphoto91.personList";
    public static final String SAVEDPREFERENCES = "com.assignment.androidphoto91.sharedPreferences";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        // Add top navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the recycler item click position
        currentItemSelected = -1;

        // Get the value passed by the Main Event
        // to open the photos for the selected album
        Intent intent = getIntent();
        albumSelected = intent.getIntExtra(MainActivity.EXTRA_ALBUM_SELECTED, 0);
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
        currentAlbum = albumList.get(albumSelected);
        if(currentAlbum != null)
        {
            photoList = currentAlbum.getPhotoList();
        }


        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView_photoList);
        recyclerView.setHasFixedSize(true);


        // Setup RecyclerView / Linear Layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // Setup RecyclerView / Adapter
        pAdapter = new RecycleViewPhotoAdapter(photoList, PhotoGalleryActivity.this, PhotoGalleryActivity.this);
        recyclerView.setAdapter(pAdapter);


        // Setup button references
        btn_addPhoto = findViewById(R.id.btn_addPhoto);
        btn_removePhoto = findViewById(R.id.btn_removePhoto);
        btn_movePhoto = findViewById(R.id.btn_movePhoto);
        btn_displayPhoto = findViewById(R.id.btn_displayPhoto);


        // Listeners // =======================
        // ====================================


        // ADD: Setup the listener action
        btn_addPhoto.setOnClickListener(v -> addPhoto());

        // REMOVE: Setup the listener action
        btn_removePhoto.setOnClickListener(v -> removePhoto());

        // MOVE: Setup the listener action
        btn_movePhoto.setOnClickListener(v -> movePhoto());

        // DISPLAY: Setup the listener action
        btn_displayPhoto.setOnClickListener(v -> displayPhoto());
    }

    public void saveAlbumData() {
        Log.d("Main Gallery ", "here0:" + SAVEDPREFERENCES);
        if(albumList != null)
        {
            // Setup reference to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(SAVEDPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

            // Convert albumList object to JSON
            Gson gson = new Gson();
            String json = gson.toJson(albumList);

        //    Log.d("Main Gallery ", "here1");

            // Save JSON file to SharedPreferences
            sharedPreferencesEditor.putString(SAVEDPREFERENCES_ALBUMLIST, json);
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
    protected void onPause() {
        saveAlbumData();
        super.onPause();
    }

    public void addPhoto() {
        fileChooserIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        fileChooserIntent.setType("image/*");
        startActivityForResult(fileChooserIntent, PICK_IMAGE_REQUEST);

        // WORKING FROM @SuppressLint("WrongConstant")
        // Don't think necessary.
        //folderChooserIntent  = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when it loads.
        //folderChooserIntent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);
        //startActivityForResult(folderChooserIntent, PICK_FOLDER_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String path = data.getData().getPath();
            Uri uri = data.getData();

            Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = returnCursor.getString(nameIndex);
            returnCursor.close();

            Bitmap bitmap = null;
            String encodedImage = "";

            try {
                bitmap = getBitmapFromUri(uri);
                encodedImage = getStringFromBitmap(bitmap);

                Photo newPhoto = new Photo(name, path,encodedImage,currentAlbum.getName());

                if (!photoList.contains(newPhoto)) {
                    currentAlbum.addPhoto(newPhoto);
                    Collections.sort(photoList);
                    saveAlbumData();
                    pAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), "Photo already exists in album", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.d("DANGER","DANGEROUS TERRITORY");
            }
        }
    }

    /* WORKING . But I don't think we need the extra. Keeping just in case.
    ***********************************************************************
    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
     {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == PICK_FOLDER_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
             fileChooserIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);


             fileChooserIntent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);
             fileChooserIntent.setType("image/*");
             startActivityForResult(fileChooserIntent, PICK_IMAGE_REQUEST);
         }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Uri uri = data.getData();


            final int takeFlags = fileChooserIntent.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // Check for the freshest data.
            getContentResolver().takePersistableUriPermission(uri, takeFlags);

            String path = uri.getPath();

            Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = returnCursor.getString(nameIndex);
            returnCursor.close();

            Bitmap bitmap = null;
            String encodedImage = "";
            try {
                bitmap = getBitmapFromUri(uri);
                encodedImage = getStringFromBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Photo newPhoto = new Photo(name, path,encodedImage);
            //newPhoto.setPath(path);
            //Log.d("Here", path);

            currentAlbum.addPhoto(newPhoto);
            Collections.sort(photoList);
            pAdapter.notifyDataSetChanged();
        }
    }

     */


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public void removePhoto() {
        if (currentItemSelected > -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete photo?")
                    .setCancelable(true)
                    .setNegativeButton("No", (dialog, which) -> {
                        // do nothing
                        dialog.cancel();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Photo photo = photoList.get(currentItemSelected);

                        if (photo != null) {
                            // Remove selection and resort
                            photoList.remove(photo);
                            Collections.sort(photoList);
                            saveAlbumData();
                            // Notify the adapter the data has changed
                            pAdapter.notifyItemRemoved(currentItemSelected);

                            // Clear selection
                            currentItemSelected = -1;
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void movePhoto() {
        if (currentItemSelected > -1) {
            Intent intent = new Intent(PhotoGalleryActivity.this, MovePhotoActivity.class);
            intent.putExtra(MainActivity.EXTRA_PHOTO_SELECTED, currentItemSelected);
            intent.putExtra(MainActivity.EXTRA_ALBUM_SELECTED, albumSelected);
            startActivity(intent);
        }
    }

    public void displayPhoto() {
        if (currentItemSelected > -1) {
            Intent intent = new Intent(PhotoGalleryActivity.this, PhotoDisplayActivity.class);
            intent.putExtra(MainActivity.EXTRA_PHOTO_SELECTED, currentItemSelected);
            intent.putExtra(MainActivity.EXTRA_ALBUM_SELECTED, albumSelected);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(int position) {

        // Set the position of the currently selected item in RecyclerList
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



    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(PhotoGalleryActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(PhotoGalleryActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

}
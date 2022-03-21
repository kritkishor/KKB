package com.assignment.androidphoto91.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.androidphoto91.R;
import com.assignment.androidphoto91.app.controllers.RecycleViewPhotoAdapter;
import com.assignment.androidphoto91.components.Album;
import com.assignment.androidphoto91.components.Photo;
import com.assignment.androidphoto91.components.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity implements RecycleViewPhotoAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter pAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isNewClick = true;
    private int currentItemSelected;
    private Button btn_searchDisplay;
    private String person = "";
    private String location = "";
    private Tag tag1,tag2;
    private ArrayList<Album> albumList;
    static public ArrayList<Photo> searchResults;
    String type1,type2,category;


    public void loadSavedAlbumData() {
        // Setup reference to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SAVEDPREFERENCES, MODE_PRIVATE);

        // Convert JSON to albumList object
        Gson gson = new Gson();
        String json = sharedPreferences.getString(MainActivity.SAVEDPREFERENCES_ALBUMLIST, null);
        Type type = (new TypeToken<ArrayList<Album>>(){}).getType();
        albumList = gson.fromJson(json, type);
    }
    public void display()
    {
        if (currentItemSelected > -1) {
            Intent intent = new Intent(SearchResultsActivity.this,PhotoDisplayActivity.class);
            intent.putExtra("search","true");
            intent.putExtra(MainActivity.EXTRA_PHOTO_SELECTED, currentItemSelected);

            if(tag1 != null)
            {
                intent.putExtra(type1,tag1.getValue());
            }
            if(tag2 != null)
            {
                intent.putExtra(type2,tag2.getValue());
            }
            if(!category.isEmpty())
            {
                intent.putExtra("category",category);
            }
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_searchDisplay = findViewById(R.id.btn_startSearch);
        btn_searchDisplay.setOnClickListener(v -> display());
        // Initialize the recycler item click position
        currentItemSelected = -1;

        // Get the tag values to look for, PASSED BY SEARCH ACTIVITY

        Intent intent = getIntent();


        if(intent.getStringExtra("person1") != null ){
            person = intent.getStringExtra("person1");
            if(!person.isEmpty())
            {
                type1 = "person1";
                tag1 = new Tag("Person",person);
            }
     //       Log.d("Search1",person);
        }
        if(intent.getStringExtra("person2") != null ){
            person = intent.getStringExtra("person2");
            Log.d("HERE person2: ", person);
           if(!person.isEmpty())
           {
               type2 = "person2";
               tag2 = new Tag("Person",person);
           }
            //       Log.d("Search1",person);
        }
        if(intent.getStringExtra("location1") != null ){
            location = intent.getStringExtra("location1");
            Log.d("HERE location1: ", location);
            if(!location.isEmpty())
            {
                type1 = "location1";
                tag1 = new Tag("Location",location);
            }
      //      Log.d("Search2",location);
        }
        if(intent.getStringExtra("location2") != null ){
            location = intent.getStringExtra("location2");
            Log.d("HERE location2: ", location);
           if(!location.isEmpty())
           {
               type2 = "location2";
               tag2 = new Tag("Location",location);
           }
            //      Log.d("Search2",location);
        }

        if (intent.getStringExtra("category") != null)
        {

            category = intent.getStringExtra("category");
            Log.d("HERE category: ", category);
        }
        //Get current albumList
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
        searchResults = new ArrayList<Photo>();





        //Search for Photos
        for(Album album: albumList)
        {
            for(Photo photo: album.getPhotoList())
            {
                if(searchResults.contains(photo))
                {
                    continue;
                }
                for (Tag tag: photo.getTagList())
                {
              //      Log.d("Search",tag.getName() + ":" + tag.getValue());

                   if(category == null || category.isEmpty() || category.compareToIgnoreCase("or") == 0)
                   {
                       if(tag1 != null && tag2 == null && tag.contains(tag1) && !searchResults.contains(photo))
                       {
                           searchResults.add(photo);
                       }
                       else if(tag1 != null && tag2 != null)
                       {
                           if ((tag.contains(tag1) || tag.contains(tag2)) && !searchResults.contains(photo))
                           {
                               searchResults.add(photo);
                           }
                       }
                   }
                   else if(category != null && !category.isEmpty() && category.compareToIgnoreCase("and") == 0)
                   {
                       if(tag1 != null && tag2 != null && !searchResults.contains(photo))
                       {
                           Tag extra = null;
                           if(tag.contains(tag1))
                           {
                               extra = tag2;
                           }
                           if(tag.contains(tag2))
                           {
                               extra = tag1;
                           }
                           if(extra != null)
                           {
                               for (Tag loopyloopylooptag : photo.getTagList())
                               {
                                   if(loopyloopylooptag.contains(extra))
                                   {
                                       searchResults.add(photo);
                                   }
                               }
                           }
                       }
                   }
                   else
                   {
                       Log.d("DANGER","EXTREMELY DANGEROUS TERRITORY");
                   }

                }
            }
        }


        //Setup Recycler View
        recyclerView = findViewById(R.id.recyclerView_searchResults);
        recyclerView.setHasFixedSize(true);

        // Setup RecyclerView / Linear Layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if(searchResults.isEmpty())
        {
            intent = new Intent(SearchResultsActivity.this,SearchActivity.class);
            intent.putExtra("error","404");
            startActivity(intent);
        }
        else
        {
            // Setup RecyclerView / Adapter
            pAdapter = new RecycleViewPhotoAdapter(searchResults, SearchResultsActivity.this, SearchResultsActivity.this);
        }
        recyclerView.setAdapter(pAdapter);

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

}

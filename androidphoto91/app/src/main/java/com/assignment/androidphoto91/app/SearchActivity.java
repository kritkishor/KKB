package com.assignment.androidphoto91.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;

import com.assignment.androidphoto91.R;
import com.assignment.androidphoto91.components.Album;
import com.assignment.androidphoto91.components.Photo;
import com.assignment.androidphoto91.components.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView_value1;
    private AutoCompleteTextView autoCompleteTextView_value2;

    private Button btn_startSearch;
    private RadioButton radio_person1;
    private RadioButton radio_person2;
    private RadioButton radio_location1;
    private RadioButton radio_location2;
    private RadioButton radio_and;
    private RadioButton radio_or;

    private RecyclerView recycleView_searchImages;

    private ArrayAdapter<String> arrayLocationAdapter;
    private ArrayAdapter<String> arrayPersonAdapter;

    private ArrayList<String> autoCompletePersonList;
    private ArrayList<String> autoCompleteLocationList;

    private boolean isperson1 = false, isperson2 = false, islocation1 = false, islocation2 = false, isand = false, isor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Intent intent = getIntent();
        if(intent.getStringExtra("error") != null)
        {
            AlertDialog dialog = new AlertDialog.Builder(SearchActivity.this).create();
            dialog.setTitle("404");
            dialog.setMessage("No Results found with that input!");
            dialog.show();
        }
        // Add top navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadAutoComplete();
        // Create layout elements
        autoCompleteTextView_value1 = findViewById(R.id.autoComplete_value1);
        autoCompleteTextView_value2 = findViewById(R.id.autoComplete_value2);

        radio_and = findViewById(R.id.radio_and);
        radio_or = findViewById(R.id.radio_or);
        radio_person1 = findViewById(R.id.radio_person1);
        radio_person2 = findViewById(R.id.radio_person2);
        radio_location1 = findViewById(R.id.radio_location1);
        radio_location2 = findViewById(R.id.radio_location2);

        // Setup autocomplete entries
        arrayPersonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompletePersonList);
        arrayLocationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteLocationList);

        radio_person1.setOnClickListener( v-> {
            autoCompleteTextView_value1.setAdapter(arrayPersonAdapter);
            isperson1 = true;
            islocation1 = false;
        });


        radio_location1.setOnClickListener(v -> {
            autoCompleteTextView_value1.setAdapter(arrayLocationAdapter);
            islocation1 = true;
            isperson1 = false;
        });

        radio_person2.setOnClickListener( v-> {
            autoCompleteTextView_value2.setAdapter(arrayPersonAdapter);
            isperson2 = true;
            islocation2 = false;
        });
        radio_location2.setOnClickListener( v-> {
            autoCompleteTextView_value2.setAdapter(arrayLocationAdapter);
            islocation2 = true;
            isperson2 = false;
        });
        radio_and.setOnClickListener(v -> {
            isand = true;
            isor = false;
        });
        radio_or.setOnClickListener(v -> {
            isor = true;
            isand = false;
        });


        btn_startSearch = findViewById(R.id.btn_startSearch);
       // recycleView_searchImages = findViewById(R.id.recycleView_searchImages);

        // Listeners
        btn_startSearch.setOnClickListener(v -> searchPhotos());






        // ADD THE RECYCLER / GRIDVIEW, if you want
        //
    }

    public void searchPhotos() {
        String value1 = autoCompleteTextView_value1.getText().toString();
        String value2 = autoCompleteTextView_value2.getText().toString();
        AlertDialog dialog = new AlertDialog.Builder(SearchActivity.this).create();
        dialog.setTitle("Error");
/*
        if(((isand || isor) && (value1.isEmpty() || value2.isEmpty())) || ((!isand && !isor) && (!value1.isEmpty() && !value2.isEmpty())))
        {
            dialog.setMessage("AND/OR <=> 2 VALUES!");
            dialog.show();
            return;
        }
        else if (value1.isEmpty() && value2.isEmpty())
        {
            dialog.setMessage("Please provide a search category!");
            dialog.show();
            return;
        }
        else if((!isperson1 && !islocation1) || value1.isEmpty())
        {
            dialog.setMessage("Input at least tag-value pair for value1,\n value2 is secondary!");
            dialog.show();
            return;
        }
*/

        Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);

        String type1="", type2="",category = "";
        if(!value1.isEmpty()){
            Log.d("value1",value1);
            if(isperson1)
            {
                type1 = "person1";
            }
            if(islocation1)
            {
                type1 = "location1";
            }
            Log.d("type",type1);
           intent.putExtra(type1,value1);
        }
        if(!value2.isEmpty()){
            Log.d("value2",value2);
            if(isperson2)
            {
                type2 = "person2";
            }
            if(islocation2)
            {
                type2 = "location2";
            }
            Log.d("type2",type2);
            intent.putExtra(type2,value2);
        }
        if(isand)
        {

            category = "and";
        }
        if(isor)
        {
            category = "or";
        }
        Log.d("category",category);
        intent.putExtra("category",category);
        startActivity(intent);
    }

    public void loadAutoComplete()
    {
        ArrayList<Album>  albumList = MainActivity.albumList;
        autoCompletePersonList = new ArrayList<String>();
        autoCompleteLocationList = new ArrayList<String>();
        if(albumList == null)
        {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SAVEDPREFERENCES, MODE_PRIVATE);
            // Convert JSON to albumList object
            Gson gson = new Gson();
            String json = sharedPreferences.getString(MainActivity.SAVEDPREFERENCES_ALBUMLIST, null);
            Type type = (new TypeToken<ArrayList<Album>>(){}).getType();
            albumList = gson.fromJson(json, type);
        }
        if (albumList != null)
        {
            for (Album album: albumList)
            {
                for (Photo photo : album.getPhotoList())
                {
                    for (Tag tag : photo.getTagList())
                    {
                        if(tag.getName().compareToIgnoreCase("Person") == 0   && !autoCompletePersonList.contains(tag.getValue()))
                        {
                            autoCompletePersonList.add(tag.getValue());
                        }
                        if(tag.getName().compareToIgnoreCase("Location") == 0 && !autoCompleteLocationList.contains(tag.getValue()))
                        {
                            autoCompleteLocationList.add(tag.getValue());
                        }
                    }
                }
            }
        }
    }
}
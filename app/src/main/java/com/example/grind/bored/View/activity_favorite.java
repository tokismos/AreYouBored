package com.example.grind.bored.View;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.grind.bored.Contoller.DatabaseHelper;
import com.example.grind.bored.R;

import java.util.ArrayList;

public class activity_favorite extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mListView = (ListView)findViewById(R.id.listview);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    //Recuperer les données de la BD et les inserer dans une liste
    public void populateListView()
    {
        Log.d(TAG, "populateListView: Displaying data in the ListView");
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext())
        {
            listData.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);
    }
}

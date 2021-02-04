package com.example.afinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class ViewUser extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        getSupportActionBar().setTitle("Prices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ListView lvMain = (ListView) findViewById(R.id.listView);
        Cursor cursor = mDb.rawQuery("SELECT * FROM services;", null);
        ArrayList<String> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String s = cursor.getString(1)+"| Price: "+cursor.getInt(2)+"| Duration: "+cursor.getInt(3)+" minutes";
            arrayList.add(s);
            cursor.moveToNext();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        lvMain.setAdapter(adapter);
        cursor.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
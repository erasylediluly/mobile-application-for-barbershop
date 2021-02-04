package com.example.afinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Menu extends AppCompatActivity {
    private int id;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setTitle("Menu");
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
        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);
    }
    public void onClickProfile(View view){
        Intent intent = new Intent(this,Profile.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
    public void onClickSignOut(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void onClickView(View view){
        Cursor cursor = mDb.rawQuery("SELECT users.type_id FROM users WHERE users.id = " + id + ";", null);
        cursor.moveToFirst();
        int role = cursor.getInt(0);
        Intent intent;
        switch (role){
            case 1:
                intent = new Intent(this,MainActivity2.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this,ViewManager.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this,ViewUser.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
        }
    }
}
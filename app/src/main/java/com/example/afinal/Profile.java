package com.example.afinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.IOException;

public class Profile extends AppCompatActivity {
    private int id;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");
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
        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView loginTextView = findViewById(R.id.textView12);
        TextView nameTextView = findViewById(R.id.textView23);
        TextView surnameTextView = findViewById(R.id.textView25);
        TextView emailTextView = findViewById(R.id.textView27);
        TextView roleTextView = findViewById(R.id.textView13);
        TextView vipTextView = findViewById(R.id.textView14);
        TextView blockedTextView = findViewById(R.id.textView15);
        Cursor cursor = mDb.rawQuery("SELECT users.login,users.name,users.surname,users.email,types.type,users.vip,users.blocked" +
                " FROM users INNER JOIN types ON users.type_id = types.id WHERE users.id = " + id + ";", null);
        cursor.moveToFirst();
        loginTextView.setText(cursor.getString(0));
        nameTextView.setText(cursor.getString(1));
        surnameTextView.setText(cursor.getString(2));
        emailTextView.setText(cursor.getString(3));
        roleTextView.setText(cursor.getString(4));
        vipTextView.setText(cursor.getInt(5) == 0?"false":"true");
        blockedTextView.setText(cursor.getInt(6) == 0?"false":"true");
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
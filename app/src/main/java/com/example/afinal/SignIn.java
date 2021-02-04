package com.example.afinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class SignIn extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setTitle("Sign In");
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
    public void onClickSignIn(View view){
        try {
            EditText loginEditText = (EditText) findViewById(R.id.editTextTextPersonName);
            String login = loginEditText.getText().toString();
            EditText passwordEditText = (EditText) findViewById(R.id.editTextTextPassword);
            String password = passwordEditText.getText().toString();
            if(login.equals("") || password.equals("")){
                throw new NullPointerException();
            }
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            String role = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString().toLowerCase();
            Cursor cursor = mDb.rawQuery("SELECT users.id,users.blocked FROM users INNER JOIN types ON users.type_id = types.id WHERE users.login = '" + login + "' AND users.password = '" + password + "' AND types.type = '" + role + "';", null);
            if(cursor.isAfterLast()){
                TextView textView = findViewById(R.id.textView17);
                textView.setText("The entered data is not correct");
                return;
            }
            cursor.moveToFirst();
            if(cursor.getInt(1) == 1){
                TextView textView = findViewById(R.id.textView17);
                textView.setText("User is blocked");
                return;
            }
            if (!cursor.isAfterLast()) {
                Intent intent = new Intent(this, Menu.class);
                intent.putExtra("id", cursor.getInt(0));
                startActivity(intent);
            }
            cursor.close();
        }
        catch(NullPointerException e) {
            TextView textView = findViewById(R.id.textView17);
            textView.setText("Fill in all the fields");
        }
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
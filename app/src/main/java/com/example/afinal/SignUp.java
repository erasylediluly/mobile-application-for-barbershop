package com.example.afinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class SignUp extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Sign Up");
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
    public void onClickSignUp(View view){
        EditText nameEditText = (EditText) findViewById(R.id.editTextTextPersonName2);
        String name = nameEditText.getText().toString();
        EditText loginEditText = (EditText) findViewById(R.id.editTextTextPersonName7);
        String login = loginEditText.getText().toString();
        EditText repeatPasswordEditText = (EditText) findViewById(R.id.editTextTextPassword2);
        String repeatPassword = repeatPasswordEditText.getText().toString();
        EditText numberEditText = (EditText) findViewById(R.id.editTextTextPersonName6);
        String number = numberEditText.getText().toString();
        EditText surnameEditText = (EditText) findViewById(R.id.editTextTextPersonName4);
        String surname = surnameEditText.getText().toString();
        EditText emailEditText = (EditText) findViewById(R.id.editTextTextPersonName5);
        String email = emailEditText.getText().toString();
        EditText passwordEditText = (EditText) findViewById(R.id.editTextTextPassword3);
        String password = passwordEditText.getText().toString();
        if(login.equals("") || password.equals("") || number.equals("") || surname.equals("")
                || email.equals("") || repeatPassword.equals("")){
            TextView textView = findViewById(R.id.textView18);
            textView.setText("Fill in all the fields");
            return;
        }
        Cursor cursor = mDb.rawQuery("SELECT * FROM users WHERE login = '" + login + "';", null);
        if(!cursor.isAfterLast()){
            TextView textView = findViewById(R.id.textView18);
            textView.setText("Enter another login");
            return;
        }
        cursor = mDb.rawQuery("SELECT * FROM users WHERE email = '" + email + "';", null);
        if(!cursor.isAfterLast()){
            TextView textView = findViewById(R.id.textView18);
            textView.setText("Enter another email");
            return;
        }
        cursor = mDb.rawQuery("SELECT * FROM users WHERE mobile_number = '" + number + "';", null);
        if(!cursor.isAfterLast()){
            TextView textView = findViewById(R.id.textView18);
            textView.setText("Enter another phone number");
            return;
        }
        cursor.close();
        if(!password.equals(repeatPassword)) {
            TextView textView = findViewById(R.id.textView18);
            textView.setText("Enter the passwords correctly");
            return;
        }
        ContentValues values = new ContentValues();
        values.put("login", login);
        values.put("name", name);
        values.put("surname", surname);
        values.put("email", email);
        values.put("mobile_number", number);
        values.put("password", password);
        values.put("type_id", 3);
        long s = mDb.insert("users", null, values);
        if (s != -1) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
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
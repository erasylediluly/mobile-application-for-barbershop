package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Barbershop");
    }
    public void onClickSignIn(View view){
        Intent intent = new Intent(this,SignIn.class);
        startActivity(intent);
    }
    public void onClickSignUp(View view){
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }
}
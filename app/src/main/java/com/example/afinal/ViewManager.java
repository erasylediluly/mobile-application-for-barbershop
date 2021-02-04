package com.example.afinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class ViewManager extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_manager);
        getSupportActionBar().setTitle("Manager Panel");
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
        final ListView lvMain = (ListView) findViewById(R.id.listView_2);
        Cursor cursor = mDb.rawQuery("SELECT users.id,users.login,users.name,users.surname,users.email,users.mobile_number,types.type,users.vip,users.blocked" +
                " FROM users INNER JOIN types ON users.type_id = types.id;", null);
        ArrayList<String> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String s = "ID: " + cursor.getInt(0) + "| Login: " + cursor.getString(1)+"| Name: "+cursor.getString(2)+"| Surname: "+cursor.getString(3)+"| Email: " +
                    cursor.getString(4)+ "| Mobile number: " + cursor.getString(5) + "| Role: " + cursor.getString(6)+"| VIP: " +
                    (cursor.getInt(7) == 0 ? "false":"true") + "| Blocked: " + (cursor.getInt(8) == 0 ? "false":"true");
            arrayList.add(s);
            cursor.moveToNext();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                 TextView idtv = findViewById(R.id.textView4);
                 idtv.setText(lvMain.getItemAtPosition(position).toString().split("\\|")[0].substring(3));
             }
        });
        cursor.close();
    }
    public void onClickBlock(View view){
        ContentValues values = new ContentValues();
        values.put("blocked", 1);
        TextView idtv = findViewById(R.id.textView4);
        mDb.update("users", values,"id=?",new String[] {idtv.getText().toString()});
        final ListView lvMain = (ListView) findViewById(R.id.listView_2);
        Cursor cursor = mDb.rawQuery("SELECT users.id,users.login,users.name,users.surname,users.email,users.mobile_number,types.type,users.vip,users.blocked" +
                " FROM users INNER JOIN types ON users.type_id = types.id;", null);
        ArrayList<String> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String s = "ID: " + cursor.getInt(0) + "| Login: " + cursor.getString(1)+"| Name: "+cursor.getString(2)+"| Surname: "+cursor.getString(3)+"| Email: " +
                    cursor.getString(4)+ "| Mobile number: " + cursor.getString(5) + "| Role: " + cursor.getString(6)+"| VIP: " +
                    (cursor.getInt(7) == 0 ? "false":"true") + "| Blocked: " + (cursor.getInt(8) == 0 ? "false":"true");
            arrayList.add(s);
            cursor.moveToNext();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        lvMain.setAdapter(adapter);
    }
    public void onClickUnblock(View view){
        ContentValues values = new ContentValues();
        values.put("blocked", 0);
        TextView idtv = findViewById(R.id.textView4);
        mDb.update("users", values,"id=?",new String[] {idtv.getText().toString()});
        final ListView lvMain = (ListView) findViewById(R.id.listView_2);
        Cursor cursor = mDb.rawQuery("SELECT users.id,users.login,users.name,users.surname,users.email,users.mobile_number,types.type,users.vip,users.blocked" +
                " FROM users INNER JOIN types ON users.type_id = types.id;", null);
        ArrayList<String> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String s = "ID: " + cursor.getInt(0) + "| Login: " + cursor.getString(1)+"| Name: "+cursor.getString(2)+"| Surname: "+cursor.getString(3)+"| Email: " +
                    cursor.getString(4)+ "| Mobile number: " + cursor.getString(5) + "| Role: " + cursor.getString(6)+"| VIP: " +
                    (cursor.getInt(7) == 0 ? "false":"true") + "| Blocked: " + (cursor.getInt(8) == 0 ? "false":"true");
            arrayList.add(s);
            cursor.moveToNext();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        lvMain.setAdapter(adapter);
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
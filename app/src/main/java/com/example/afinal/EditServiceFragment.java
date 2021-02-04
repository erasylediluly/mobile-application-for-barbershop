package com.example.afinal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.ArrayList;

public class EditServiceFragment extends Fragment {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Button editUser;
    private Button deleteUser;
    private ListView listView;
    private int id;
    public EditServiceFragment(ListView listView,int id){
        this.listView = listView;
        this.id=id;
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_service, container, false);
        return root;
    }
    public void onStart() {
        super.onStart();
        mDBHelper = new DatabaseHelper(getContext());
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
        EditText nameEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName3);
        EditText priceEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName6);
        EditText minutesEditText = (EditText)getView().findViewById(R.id.editTextTextPersonName5);
        Cursor cursor = mDb.rawQuery("SELECT id,name,price,minutes" +
                " FROM services WHERE id = " + this.id + ";", null);
        cursor.moveToFirst();
        nameEditText.setText(cursor.getString(1));
        priceEditText.setText(cursor.getInt(2)+"");
        minutesEditText.setText(cursor.getInt(3)+"");
        cursor.close();
        editUser = (Button) getView().findViewById(R.id.button2);
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName3);
                EditText priceEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName6);
                EditText minutesEditText = (EditText)getView().findViewById(R.id.editTextTextPersonName5);
                String name = nameEditText.getText().toString();
                String price= priceEditText.getText().toString();
                String minutes = minutesEditText.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put("name",name);
                cv.put("price",price);
                cv.put("minutes",minutes);
                mDb.update("services",cv,"id="+id,null);
                Cursor cursor = mDb.rawQuery("SELECT * FROM services;", null);
                ArrayList<String> arrayList = new ArrayList<>();
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    String s = "ID: " + cursor.getInt(0) + "| " + cursor.getString(1)+"| Price: "+cursor.getInt(2)+"| Duration: "+cursor.getInt(3)+" minutes";
                    arrayList.add(s);
                    cursor.moveToNext();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);
                FragmentManager fm = getFragmentManager();
                for (Fragment fragment : fm.getFragments()) {
                    if((fragment.getTag() != null) && (fragment.getTag()).equals("tag")) {
                        fm.beginTransaction().remove(fragment).commit();
                    }
                }
            }
        });
        deleteUser = (Button) getView().findViewById(R.id.button5);
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long a = mDb.delete("services","id="+id+"",null);
                Cursor cursor = mDb.rawQuery("SELECT * FROM services;", null);
                ArrayList<String> arrayList = new ArrayList<>();
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    String s = "ID: " + cursor.getInt(0) + "| " + cursor.getString(1)+"| Price: "+cursor.getInt(2)+"| Duration: "+cursor.getInt(3)+" minutes";
                    arrayList.add(s);
                    cursor.moveToNext();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);
                FragmentManager fm = getFragmentManager();
                for (Fragment fragment : fm.getFragments()) {
                    if((fragment.getTag() != null) && (fragment.getTag()).equals("tag")) {
                        fm.beginTransaction().remove(fragment).commit();
                    }
                }
            }
        });
    }
}
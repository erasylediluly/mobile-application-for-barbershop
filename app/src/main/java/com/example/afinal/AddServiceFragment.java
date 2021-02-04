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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.ArrayList;

public class AddServiceFragment extends Fragment {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Button addButton;
    private ListView listView;
    public AddServiceFragment(ListView listView){this.listView=listView;}
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_service, container, false);
        return root;
    }

    @Override
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
        addButton = (Button) getView().findViewById(R.id.button2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                EditText nameEditText = getView().findViewById(R.id.editTextTextPersonName2);
                String name = nameEditText.getText().toString();
                EditText priceEditText = getView().findViewById(R.id.editTextTextPersonName6);
                String price = priceEditText.getText().toString();
                EditText minutesEditText = getView().findViewById(R.id.editTextTextPersonName5);
                String minutes = minutesEditText.getText().toString();
                if(name.equals("") || price.equals("") || minutes.equals("")){
                    TextView textView = getView().findViewById(R.id.textView29);
                    textView.setText("Fill in all the fields");
                    return;
                }
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("price", price);
                values.put("minutes", minutes);
                mDb.insert("services", null, values);
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
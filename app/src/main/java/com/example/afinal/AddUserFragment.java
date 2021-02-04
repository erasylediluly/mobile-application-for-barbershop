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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddUserFragment extends Fragment {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Button addButton;
    private ListView listView;
    public AddUserFragment(ListView listView){this.listView=listView;}
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_user, container, false);
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
                EditText surnameEditText = getView().findViewById(R.id.editTextTextPersonName4);
                String surname = surnameEditText.getText().toString();
                EditText loginEditText = getView().findViewById(R.id.editTextTextPersonName7);
                String login = loginEditText.getText().toString();
                EditText numberEditText = getView().findViewById(R.id.editTextTextPersonName6);
                String mobile_number = numberEditText.getText().toString();
                EditText emailEditText = getView().findViewById(R.id.editTextTextPersonName5);
                String email = emailEditText.getText().toString();
                EditText passwordEditText = getView().findViewById(R.id.editTextTextPassword3);
                String password = passwordEditText.getText().toString();
                Spinner roleSpinner = getView().findViewById(R.id.spinner);
                String role = roleSpinner.getSelectedItem().toString();
                Spinner vipSpinner = getView().findViewById(R.id.spinner_3);
                String vip = vipSpinner.getSelectedItem().toString();
                Spinner blockedSpinner = getView().findViewById(R.id.spinner_2);
                String blocked = blockedSpinner.getSelectedItem().toString();
                if(login.equals("") || password.equals("") || mobile_number.equals("") || surname.equals("")
                        || email.equals("") || name.equals("") || role.equals("") || vip.equals("") || blocked.equals("")){
                    TextView textView = getView().findViewById(R.id.textView19);
                    textView.setText("Fill in all the fields");
                    return;
                }
                Cursor cursor = mDb.rawQuery("SELECT * FROM users WHERE login = '" + login + "';", null);
                if(!cursor.isAfterLast()){
                    TextView textView = getView().findViewById(R.id.textView19);
                    textView.setText("Enter another login");
                    return;
                }
                cursor = mDb.rawQuery("SELECT * FROM users WHERE email = '" + email + "';", null);
                if(!cursor.isAfterLast()){
                    TextView textView = getView().findViewById(R.id.textView19);
                    textView.setText("Enter another email");
                    return;
                }
                cursor = mDb.rawQuery("SELECT * FROM users WHERE mobile_number = '" + mobile_number + "';", null);
                if(!cursor.isAfterLast()){
                    TextView textView = getView().findViewById(R.id.textView19);
                    textView.setText("Enter another phone number");
                    return;
                }
                cursor.close();
                ContentValues values = new ContentValues();
                values.put("login", login);
                values.put("name", name);
                values.put("surname", surname);
                values.put("email", email);
                values.put("mobile_number", mobile_number);
                values.put("password", password);
                values.put("blocked", blocked);
                values.put("vip", vip);
                if(role.equals("admin")) {
                    values.put("type_id", 1);
                }
                if(role.equals("manager")) {
                    values.put("type_id", 2);
                }
                if(role.equals("user")) {
                    values.put("type_id", 3);
                }
                long s = mDb.insert("users", null, values);
                cursor = mDb.rawQuery("SELECT users.id,users.login,users.name,users.surname,users.email,users.mobile_number,types.type,users.vip,users.blocked" +
                        " FROM users INNER JOIN types ON users.type_id = types.id;", null);
                ArrayList<String> arrayList = new ArrayList<>();
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    String st = "ID: " + cursor.getInt(0) + "| Login: " + cursor.getString(1)+"| Name: "+cursor.getString(2)+"| Surname: "+cursor.getString(3)+"| Email: " +
                            cursor.getString(4)+ "| Mobile number: " + cursor.getString(5) + "| Role: " + cursor.getString(6)+"| VIP: " +
                            (cursor.getInt(7) == 0 ? "false":"true") + "| Blocked: " + (cursor.getInt(8) == 0 ? "false":"true");
                    arrayList.add(st);
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
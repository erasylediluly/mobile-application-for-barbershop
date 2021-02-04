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

public class EditUserFragment extends Fragment {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Button editUser;
    private Button deleteUser;
    private ListView listView;
    private String login;
    private String email;
    private int id;
    public EditUserFragment(ListView listView,int id){
        this.listView = listView;
        this.id=id;
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_user, container, false);
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
        EditText loginEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName7);
        EditText nameEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName3);
        EditText surnameEditText = (EditText)getView().findViewById(R.id.editTextTextPersonName4);
        EditText phoneEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName6);
        EditText emailEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName5);
        EditText passwordEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName8);
        Spinner roleSpinner = (Spinner) getView().findViewById(R.id.spinner_4);
        Spinner vipSpinner = (Spinner) getView().findViewById(R.id.spinner_6);
        Spinner blockedSpinner = (Spinner) getView().findViewById(R.id.spinner_5);
        Cursor cursor = mDb.rawQuery("SELECT users.id,users.login,users.name,users.surname,users.email,users.type_id,users.vip,users.blocked,users.mobile_number,users.password" +
                " FROM users WHERE users.id = " + this.id + ";", null);
        cursor.moveToFirst();
        loginEditText.setText(cursor.getString(1));
        nameEditText.setText(cursor.getString(2));
        surnameEditText.setText(cursor.getString(3));
        emailEditText.setText(cursor.getString(4));
        passwordEditText.setText(cursor.getString(9));
        roleSpinner.setSelection(cursor.getInt(5)-1);
        vipSpinner.setSelection(cursor.getInt(6));
        blockedSpinner.setSelection(cursor.getInt(7));
        phoneEditText.setText(cursor.getString(8));
        final String login2 = loginEditText.getText().toString();
        final String email2 = emailEditText.getText().toString();
        cursor.close();
        editUser = (Button) getView().findViewById(R.id.button2);
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText loginEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName7);
                EditText nameEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName3);
                EditText surnameEditText = (EditText)getView().findViewById(R.id.editTextTextPersonName4);
                EditText phoneEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName6);
                EditText emailEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName5);
                EditText passwordEditText = (EditText) getView().findViewById(R.id.editTextTextPersonName8);
                Spinner roleSpinner = (Spinner) getView().findViewById(R.id.spinner_4);
                Spinner vipSpinner = (Spinner) getView().findViewById(R.id.spinner_6);
                Spinner blockedSpinner = (Spinner) getView().findViewById(R.id.spinner_5);
                String password = passwordEditText.getText().toString();
                String login = loginEditText.getText().toString();
                String name= nameEditText.getText().toString();
                String surname = surnameEditText.getText().toString();
                String mobile_phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                int role = roleSpinner.getSelectedItemPosition()+1;
                int vip = Integer.parseInt(vipSpinner.getSelectedItem().toString());
                int blocked = Integer.parseInt(blockedSpinner.getSelectedItem().toString());
                Cursor cursor = mDb.rawQuery("SELECT * FROM users WHERE login = '" + login + "';", null);
                System.out.println("-----------"+cursor.getCount());
                if(cursor.getCount()>0 && !login.equals(login2)){
                    TextView textView = getView().findViewById(R.id.textView20);
                    textView.setText("There is user with login:" + login);
                    return;
                }
                cursor = mDb.rawQuery("SELECT * FROM users WHERE email = '" + email + "';", null);
                cursor.move(1);
                if(cursor.getCount()>0 && !email.equals(email2)){
                    TextView textView = getView().findViewById(R.id.textView20);
                    textView.setText("There is user with email:" + email);
                    return;
                }
                ContentValues cv = new ContentValues();
                cv.put("name",name);
                cv.put("login",login);
                cv.put("surname",surname);
                cv.put("email",email);
                cv.put("password",password);
                cv.put("mobile_number",mobile_phone);
                cv.put("type_id",role);
                cv.put("blocked",blocked);
                cv.put("vip",vip);
                mDb.update("users",cv,"id="+id,null);
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
        deleteUser = (Button) getView().findViewById(R.id.button5);
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText loginEditText = getView().findViewById(R.id.editTextTextPersonName7);
                String login = loginEditText.getText().toString();
                Cursor cursor = mDb.rawQuery("SELECT * FROM users WHERE login = '" + login + "';", null);
                if(cursor.isAfterLast()){
                    TextView textView = getView().findViewById(R.id.textView20);
                    textView.setText("There is no user with login:" + login);
                    return;
                }
                long a = mDb.delete("users","login='"+login+"'",null);
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
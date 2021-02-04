package com.example.afinal;

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

import com.example.afinal.ui.home.HomeFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SearchUserFragment extends Fragment {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Button searchUser;
    private ListView listView;
    private int id;
    public SearchUserFragment(ListView listView){
        this.listView = listView;
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_user, container, false);
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

        searchUser = (Button) getView().findViewById(R.id.button2);
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                EditText nameEditText = getView().findViewById(R.id.editTextTextPersonName2);
                String name = nameEditText.getText().toString();
                EditText surnameEditText = getView().findViewById(R.id.editTextTextPersonName4);
                String surname = surnameEditText.getText().toString();
                EditText numberEditText = getView().findViewById(R.id.editTextTextPersonName6);
                String mobile_number = numberEditText.getText().toString();
                EditText emailEditText = getView().findViewById(R.id.editTextTextPersonName5);
                String email = emailEditText.getText().toString();
                HashMap<String, String> hashMap = new HashMap<>();
                if(name.length()!=0){
                    hashMap.put("name",name);
                }
                if(email.length()!=0){
                    hashMap.put("email",email);
                }
                if(surname.length()!=0){
                    hashMap.put("surname",surname);
                }
                if(mobile_number.length()!=0){
                    hashMap.put("mobile_number",mobile_number);
                }
                String query = "";
                for(String key: hashMap.keySet()){
                    query+="users."+key+"='"+hashMap.get(key)+"' AND";
                }
                if(query.length()!=0) {
                    query = "WHERE " + query.substring(0, query.length() - 4) + ";";
                }
                //ListView lvMain =
                Cursor cursor = mDb.rawQuery("SELECT users.id,users.login,users.name,users.surname,users.email," +
                        "users.mobile_number,types.type,users.vip,users.blocked" +
                        " FROM users INNER JOIN types ON users.type_id = types.id "+query, null);
                ArrayList<String> arrayList = new ArrayList<>();
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    String s = "ID: " + cursor.getInt(0) + "| Login: " + cursor.getString(1)+"| Name: "+cursor.getString(2)+"| Surname: "+cursor.getString(3)+"| Email: " +
                            cursor.getString(4)+ "| Mobile number: " + cursor.getString(5) + "| Role: " + cursor.getString(6)+"| VIP: " +
                            (cursor.getInt(7) == 0 ? "false":"true") + "| Blocked: " + (cursor.getInt(8) == 0 ? "false":"true");
                    arrayList.add(s);
                    cursor.moveToNext();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, arrayList);
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
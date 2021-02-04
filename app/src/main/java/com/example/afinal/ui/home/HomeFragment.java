package com.example.afinal.ui.home;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.afinal.AddUserFragment;
import com.example.afinal.DatabaseHelper;
import com.example.afinal.EditUserFragment;
import com.example.afinal.R;
import com.example.afinal.SearchUserFragment;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button addUser;
    private Button searchUser;
    private Button editUser;
    private Button close;
    private LinearLayout linearLayout;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private ListView listView;
    @Override
    public void onStart() {
        super.onStart();
        System.out.println(getView().findViewById(R.id.button2));
        addUser = (Button) getView().findViewById(R.id.button21);
        searchUser = (Button) getView().findViewById(R.id.button19);
        editUser = (Button) getView().findViewById(R.id.button20);
        close = (Button) getView().findViewById(R.id.button24);
        listView = getView().findViewById(R.id.listView_users);
        linearLayout = (LinearLayout) getView().findViewById(R.id.main_frag_container_2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView idtv = getView().findViewById(R.id.textView21);
                idtv.setText(listView.getItemAtPosition(position).toString().split("\\|")[0].substring(3));
            }
        });
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if(linearLayout.getChildCount() == 0){
                    AddUserFragment fragOne = new AddUserFragment((ListView)getView().findViewById(R.id.listView_users));
                    ft.add(R.id.main_frag_container_2, fragOne,"tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                else if(linearLayout.getChildCount() != 0){
                    for (Fragment fragment : fm.getFragments()) {
                        if((fragment.getTag() != null) && (fragment.getTag()).equals("tag")) {
                            fm.beginTransaction().remove(fragment).commit();
                        }
                    }
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    AddUserFragment fragOne = new AddUserFragment((ListView)getView().findViewById(R.id.listView_users));
                    ft.add(R.id.main_frag_container_2, fragOne,"tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                ft.commit();
            }});
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                for (Fragment fragment : fm.getFragments()) {
                    if((fragment.getTag() != null) && (fragment.getTag()).equals("tag")) {
                        fm.beginTransaction().remove(fragment).commit();
                    }
                }
            }
        });
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                if(linearLayout.getChildCount() == 0){
                    SearchUserFragment fragOne = new SearchUserFragment((ListView)getView().findViewById(R.id.listView_users));
                    ft.add(R.id.main_frag_container_2, fragOne,"tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                else if(linearLayout.getChildCount() != 0){
                    for (Fragment fragment : fm.getFragments()) {
                        if((fragment.getTag() != null) && (fragment.getTag()).equals("tag")) {
                            fm.beginTransaction().remove(fragment).commit();
                        }
                    }
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    SearchUserFragment fragOne = new SearchUserFragment((ListView)getView().findViewById(R.id.listView_users));
                    ft.add(R.id.main_frag_container_2, fragOne,"tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                ft.commit();
            }});
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                TextView chose = getView().findViewById(R.id.textView21);
                TextView choose = getView().findViewById(R.id.textView28);
                Cursor cursor = mDb.rawQuery("SELECT * FROM users WHERE id="+chose.getText().toString(),null);
                if(chose.getText().toString().equals("-1") || cursor.isAfterLast()){
                    choose.setText(R.string.chooseId);
                    return;
                }
                int id = Integer.parseInt(chose.getText().toString().trim());
                if(linearLayout.getChildCount() == 0){
                    EditUserFragment fragOne = new EditUserFragment((ListView)getView().findViewById(R.id.listView_users),id);
                    ft.add(R.id.main_frag_container_2, fragOne,"tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                else if(linearLayout.getChildCount() != 0){
                    for (Fragment fragment : fm.getFragments()) {
                        if((fragment.getTag() != null) && (fragment.getTag()).equals("tag")) {
                            fm.beginTransaction().remove(fragment).commit();
                        }
                    }
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    EditUserFragment fragOne = new EditUserFragment((ListView)getView().findViewById(R.id.listView_users),id);
                    ft.add(R.id.main_frag_container_2, fragOne,"tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                ft.commit();
            }});
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
        final ListView lvMain = (ListView) getView().findViewById(R.id.listView_users);
        Cursor cursor = mDb.rawQuery("SELECT users.id,users.login,users.name,users.surname,users.email,users.mobile_number,users.type_id,users.vip,users.blocked" +
                " FROM users;", null);
        ArrayList<String> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String s = "ID: " + cursor.getInt(0) + "| Login: " + cursor.getString(1)+"| Name: "+cursor.getString(2)+"| Surname: "+cursor.getString(3)+"| Email: " +
                    cursor.getString(4)+ "| Mobile number: " + cursor.getString(5) + "| Role: " + cursor.getInt(6)+"| VIP: " +
                    (cursor.getInt(7) == 0 ? "false":"true") + "| Blocked: " + (cursor.getInt(8) == 0 ? "false":"true");
            arrayList.add(s);
            cursor.moveToNext();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        lvMain.setAdapter(adapter);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }
}
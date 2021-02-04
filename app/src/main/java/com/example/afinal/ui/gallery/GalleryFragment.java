package com.example.afinal.ui.gallery;

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

import com.example.afinal.AddServiceFragment;
import com.example.afinal.AddUserFragment;
import com.example.afinal.DatabaseHelper;
import com.example.afinal.EditServiceFragment;
import com.example.afinal.R;

import java.io.IOException;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private Button addService;
    private Button editService;
    private Button close;
    private LinearLayout linearLayout;
    private GalleryViewModel galleryViewModel;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    public void onStart() {
        super.onStart();
        addService = (Button) getView().findViewById(R.id.button15);
        editService = (Button) getView().findViewById(R.id.button22);
        close = (Button) getView().findViewById(R.id.button23);
        final ListView listView = getView().findViewById(R.id.listView_services);
        linearLayout = (LinearLayout) getView().findViewById(R.id.main_frag_container);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView idtv = getView().findViewById(R.id.textView31);
                idtv.setText(listView.getItemAtPosition(position).toString().split("\\|")[0].substring(3));
            }
        });
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if(linearLayout.getChildCount() == 0){
                    AddServiceFragment fragOne = new AddServiceFragment(listView);
                    ft.add(R.id.main_frag_container, fragOne,"tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                else if(linearLayout.getChildCount() != 0){
                    for (Fragment fragment : fm.getFragments()) {
                        if((fragment.getTag() != null) && (fragment.getTag()).equals("tag")) {
                            fm.beginTransaction().remove(fragment).commit();
                        }
                    }
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    AddServiceFragment fragOne = new AddServiceFragment(listView);
                    ft.add(R.id.main_frag_container, fragOne,"tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                ft.commit();
            }});
        editService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                TextView chose = getView().findViewById(R.id.textView31);
                TextView choose = getView().findViewById(R.id.textView32);
                Cursor cursor = mDb.rawQuery("SELECT * FROM services WHERE id="+chose.getText().toString(),null);
                if(chose.getText().toString().equals("-1") || cursor.isAfterLast()){
                    choose.setText(R.string.chooseId2);
                    return;
                }
                int id = Integer.parseInt(chose.getText().toString().trim());
                if(linearLayout.getChildCount() == 0){
                    EditServiceFragment fragOne = new EditServiceFragment(listView,id);
                    ft.add(R.id.main_frag_container, fragOne,"tag");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                else if(linearLayout.getChildCount() != 0){
                    for (Fragment fragment : fm.getFragments()) {
                        if((fragment.getTag() != null) && (fragment.getTag()).equals("tag")) {
                            fm.beginTransaction().remove(fragment).commit();
                        }
                    }
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    EditServiceFragment fragOne = new EditServiceFragment(listView,id);
                    ft.add(R.id.main_frag_container, fragOne,"tag");
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
        ListView lvMain = (ListView) getView().findViewById(R.id.listView_services);
        Cursor cursor = mDb.rawQuery("SELECT * FROM services;", null);
        ArrayList<String> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String s = "ID: " + cursor.getInt(0) + "| " + cursor.getString(1)+"| Price: "+cursor.getInt(2)+"| Duration: "+cursor.getInt(3)+" minutes";
            arrayList.add(s);
            cursor.moveToNext();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        lvMain.setAdapter(adapter);
        cursor.close();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        return root;
    }
}
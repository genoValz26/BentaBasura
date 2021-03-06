package com.android.bentabasura.benta_basura.Fragments;

/**
 * Created by ccs on 10/18/17.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Trash;
import com.android.bentabasura.benta_basura.Pages.Login;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_trashlist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabFragmentTrash extends Fragment {

    ListView lstMyTrash;
    ActiveUser activeUser;
    String oldestPostId = "";
    ProgressDialog mProgressDialog;
    DatabaseReference databaseReferenceTrash;
    custom_trashlist customTrashAdapter;
    ArrayList<Trash> trashArray = new ArrayList<>();
    List<String> trashCategory = Arrays.asList("Plastic", "Paper", "Metal", "Wood");
    TextView txtEmpty;
    SearchView searchTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_items_trash, container, false);

        //set persist to true
        Login.setPersist(true);

        lstMyTrash = (ListView) view.findViewById(R.id.lstMyTrash);
        mProgressDialog = new ProgressDialog(container.getContext());
        txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);
        searchTxt = (SearchView) view.findViewById(R.id.searchTxt);

        databaseReferenceTrash  = FirebaseDatabase.getInstance().getReference("Trash");

        activeUser = ActiveUser.getInstance();

        customTrashAdapter = new custom_trashlist(container.getContext(), trashArray);

        searchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                customTrashAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customTrashAdapter.getFilter().filter(s);
                return false;
            }
        });

        lstMyTrash.setAdapter(customTrashAdapter);

        //LoadTrash
        getTrashDataFromFirebase();

        return view;
    }

    private void getTrashDataFromFirebase()
    {
        for(final String trashCat: trashCategory)
        {
            databaseReferenceTrash.child(trashCat.toString()).orderByChild("reverseDate").addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren())
                        {
                            boolean found = false;

                            oldestPostId = postSnapShot.getKey();

                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            for (Trash itemTrash : trashArray)
                            {
                                if (itemTrash.getTrashId().equals(oldestPostId)) {
                                    found = true;
                                    break;
                                }
                            }

                            if (!found)
                            {
                                Trash trash = postSnapShot.getValue(Trash.class);

                                if (trash.getTrashCategory().equals(trashCat.toString()))
                                {
                                    if (trash.getUploadedBy().equals(activeUser.getUserId()))
                                    {
                                        trash.setTrashId(postSnapShot.getKey().toString());
                                        trashArray.add(trash);
                                        customTrashAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                        mProgressDialog.dismiss();
                    }

                    if(trashArray.size() == 0){
                        lstMyTrash.setVisibility(View.INVISIBLE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        lstMyTrash.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
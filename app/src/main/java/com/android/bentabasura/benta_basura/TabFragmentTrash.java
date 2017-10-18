package com.android.bentabasura.benta_basura;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_items_trash, container, false);

        lstMyTrash = (ListView) view.findViewById(R.id.lstMyTrash);
        mProgressDialog = new ProgressDialog(container.getContext());


        databaseReferenceTrash  = FirebaseDatabase.getInstance().getReference("Trash");

        activeUser = ActiveUser.getInstance();

        customTrashAdapter = new custom_trashlist(container.getContext(), trashArray);

        lstMyTrash.setAdapter(customTrashAdapter);

        //LoadTrash
        getTrashDataFromFirebase();

        return view;

    }

    private void getTrashDataFromFirebase()
    {
        for(final String trashCat: trashCategory)
        {
            databaseReferenceTrash.child(trashCat.toString()).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren())
                        {
                            oldestPostId = postSnapShot.getKey();

                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            Trash trash = postSnapShot.getValue(Trash.class);

                            if (trash.getTrashCategory().equals(trashCat.toString()))
                            {
                                if (trash.getSold().equals("0"))
                                {
                                    for (Trash itemTrash : trashArray)
                                    {
                                        if (itemTrash.getTrashId().equals(oldestPostId)) {
                                            continue;
                                        }
                                    }

                                    trash.setTrashId(postSnapShot.getKey().toString());
                                    trashArray.add(trash);
                                    customTrashAdapter.notifyDataSetChanged();

                                }
                            }
                        }
                        mProgressDialog.dismiss();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
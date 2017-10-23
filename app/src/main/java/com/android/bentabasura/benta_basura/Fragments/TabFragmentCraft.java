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

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Craft;
import com.android.bentabasura.benta_basura.Pages.Login;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_craftlist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabFragmentCraft extends Fragment {

    ListView lstMyTrash;
    ActiveUser activeUser;
    String oldestPostId = "";
    ProgressDialog mProgressDialog;
    DatabaseReference databaseReferenceCraft;
    custom_craftlist customCraftAdapter;
    ArrayList<Craft> craftArray = new ArrayList<>();
    List<String> craftCategory = Arrays.asList("Decoration", "Furniture", "Projects", "Accessories");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_items_trash, container, false);

        //set persist to true
        Login.setPersist(true);

        lstMyTrash = (ListView) view.findViewById(R.id.lstMyTrash);
        mProgressDialog = new ProgressDialog(container.getContext());

        databaseReferenceCraft  = FirebaseDatabase.getInstance().getReference("Craft");

        activeUser = ActiveUser.getInstance();

        customCraftAdapter = new custom_craftlist(container.getContext(), craftArray);

        lstMyTrash.setAdapter(customCraftAdapter);

        //LoadTrash
        getCraftDataFromFirebase();

        return view;
    }

    private void getCraftDataFromFirebase()
    {
        for(final String trashCat: craftCategory)
        {
            databaseReferenceCraft.child(trashCat.toString()).addValueEventListener(new ValueEventListener()
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


                            for (Craft itemCraft : craftArray)
                            {
                                if (itemCraft.getCraftID().equals(oldestPostId)) {
                                    found = true;
                                    break;
                                }
                            }

                            if (!found)
                            {
                                Craft craft = postSnapShot.getValue(Craft.class);

                                if (craft.getCraftCategory().equals(trashCat.toString()))
                                {
                                    if (craft.getSold().equals("0") && craft.getUploadedBy().equals(activeUser.getUserId())) {

                                        craft.setCraftID(postSnapShot.getKey().toString());
                                        craftArray.add(craft);
                                        customCraftAdapter.notifyDataSetChanged();
                                    }
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
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
import android.widget.TextView;

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


public class BoughtItemsCraftTab extends Fragment {


    ListView lstMyCraft;
    ActiveUser activeUser;
    String oldestPostId = "";
    ProgressDialog mProgressDialog;
    DatabaseReference databaseReferenceCraft,databaseReferenceTransaction;
    custom_craftlist customCraftAdapter;
    ArrayList<Craft> craftArray = new ArrayList<>();
    List<String> craftCategory = Arrays.asList("Decoration", "Furniture", "Projects", "Accessories");
    TextView txtEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bought_craft, container, false);

        //set persist to true
        Login.setPersist(true);

        lstMyCraft = (ListView) view.findViewById(R.id.lstBoughtCraft);
        mProgressDialog = new ProgressDialog(container.getContext());
        txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);

        databaseReferenceCraft  = FirebaseDatabase.getInstance().getReference("Craft");
        databaseReferenceTransaction = FirebaseDatabase.getInstance().getReference("Transaction").child("Craft");

        activeUser = ActiveUser.getInstance();

        customCraftAdapter = new custom_craftlist(container.getContext(), craftArray);

        lstMyCraft.setAdapter(customCraftAdapter);

        //LoadTrash
        getCraftDataFromFirebase();


        return view;

    }

    private void getCraftDataFromFirebase()
    {
        for (final String trashCat : craftCategory) {
                databaseReferenceCraft.child(trashCat.toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                boolean found = false;

                                oldestPostId = postSnapShot.getKey();

                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();


                                for (Craft itemCraft : craftArray) {
                                    if (itemCraft.getCraftID().equals(oldestPostId)) {
                                        found = true;
                                        break;
                                    }
                                }

                                if (!found) {
                                    final Craft craft = postSnapShot.getValue(Craft.class);
                                    if (craft.getflag().equals("1") && craft.getflagTo().equals(activeUser.getUserId())) {
                                        craft.setCraftID(oldestPostId);

                                        craftArray.add(craft);
                                        customCraftAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                            mProgressDialog.dismiss();
                        }
                        if (craftArray.size() == 0) {
                            lstMyCraft.setVisibility(View.INVISIBLE);
                            txtEmpty.setVisibility(View.VISIBLE);
                        } else {
                            lstMyCraft.setVisibility(View.VISIBLE);
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
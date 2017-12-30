package com.android.bentabasura.benta_basura.Pages;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.android.bentabasura.benta_basura.Models.Trash;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_admin_trash_list;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_ManageTrash extends Admin_Navigation {
    protected DrawerLayout mDrawer;
    private ListView listView1;
    String oldestPostId = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog mProgressDialog;
    ArrayList<Trash> trashArray = new ArrayList<>();
    private custom_admin_trash_list customAdapter;
    Bundle receivedBundle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_manage_trash, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addView(contentView, 0);

        receivedBundle = getIntent().getExtras();

        listView1 = (ListView) findViewById(R.id.listView1);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Trash").child(receivedBundle.get("Category").toString());
        mProgressDialog = new ProgressDialog(this);

        getCraftDataFromFirebase();

        customAdapter = new custom_admin_trash_list(this, trashArray);
        listView1.setAdapter(customAdapter);
    }

    public void getCraftDataFromFirebase() {


        databaseReference.orderByChild("reverseDate").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        boolean found = false;
                        oldestPostId = postSnapShot.getKey();

                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();

                        for (Trash trashItem : trashArray) {
                            if (!TextUtils.isEmpty(trashItem.getTrashId()) && !TextUtils.isEmpty(oldestPostId)) {
                                if (trashItem.getTrashId().equals(oldestPostId)) {
                                    found = true;
                                }
                            }
                        }

                        if (!found) {

                            Trash trash = postSnapShot.getValue(Trash.class);

                            trash.setTrashId(postSnapShot.getKey().toString());
                            trashArray.add(trash);
                            customAdapter.notifyDataSetChanged();

                        }
                    }
                    mProgressDialog.dismiss();
                }
                if (trashArray.size() == 0) {
                    listView1.setVisibility(View.INVISIBLE);
                    listView1.setVisibility(View.VISIBLE);
                } else {
                    listView1.setVisibility(View.VISIBLE);
                    //txtEmpty.setVisibility(View.INVISIBLE);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
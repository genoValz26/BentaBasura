package com.android.bentabasura.benta_basura.Pages;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.android.bentabasura.benta_basura.Models.Craft;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_admin_craft_list;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_ManageCraft extends Admin_Navigation
{
    protected DrawerLayout mDrawer;
    private ListView listView1;
    String oldestPostId = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog mProgressDialog;
    ArrayList<Craft> craftArray =new ArrayList<>();
    private custom_admin_craft_list customAdapter;
    Bundle receivedBundle;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_manage_craft, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addView(contentView, 0);

        receivedBundle = getIntent().getExtras();

        listView1 = (ListView) findViewById(R.id.listView1);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Craft").child(receivedBundle.get("Category").toString());
        mProgressDialog = new ProgressDialog(this);

        getCraftDataFromFirebase();

        customAdapter = new custom_admin_craft_list(this, craftArray);
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

                        for (Craft craftItem : craftArray) {
                            if (!TextUtils.isEmpty(craftItem.getCraftID()) && !TextUtils.isEmpty(oldestPostId)) {
                                if (craftItem.getCraftID().equals(oldestPostId)) {
                                    found = true;
                                }
                            }
                        }

                        if (!found) {

                           Craft craft = postSnapShot.getValue(Craft.class);

                            craft.setCraftID(postSnapShot.getKey().toString());
                            craftArray.add(craft);
                            customAdapter.notifyDataSetChanged();

                        }
                    }
                    mProgressDialog.dismiss();
                }
                if(craftArray.size() == 0){
                    listView1.setVisibility(View.INVISIBLE);
                    listView1.setVisibility(View.VISIBLE);
                }
                else
                {
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
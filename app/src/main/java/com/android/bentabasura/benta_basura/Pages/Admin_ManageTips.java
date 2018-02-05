package com.android.bentabasura.benta_basura.Pages;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.bentabasura.benta_basura.Models.Tips;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_tipslist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_ManageTips extends Admin_Navigation
{
    protected DrawerLayout mDrawer;
    private Intent addTipsPage;
    private Button addTipsBtn;
    private ListView listView1;
    String oldestPostId = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog mProgressDialog;
    ArrayList<Tips> tipsArray =new ArrayList<>();
    private custom_tipslist customAdapter;
    SearchView searchTxt;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_manage_tips, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addView(contentView, 0);

        addTipsPage = new Intent(Admin_ManageTips.this,Admin_AddTips.class);

        addTipsBtn = (Button) findViewById(R.id.btnAddTips);

        listView1 = (ListView) findViewById(R.id.listView1);

        searchTxt = (SearchView) findViewById(R.id.searchTxt);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tips");
        mProgressDialog = new ProgressDialog(this);

        getCraftDataFromFirebase();

        customAdapter = new custom_tipslist(this, tipsArray);
        listView1.setAdapter(customAdapter);

        addTipsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addTipsPage);
            }
        });
        searchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }
        });

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

                            for (Tips tipsItem : tipsArray) {
                                if (!TextUtils.isEmpty(tipsItem.getTipsID()) && !TextUtils.isEmpty(oldestPostId)) {
                                    if (tipsItem.getTipsID().equals(oldestPostId)) {
                                        found = true;
                                    }
                                }
                            }

                            if (!found) {

                               Tips tips = postSnapShot.getValue(Tips.class);

                                tips.setTipsID(postSnapShot.getKey().toString());
                                tipsArray.add(tips);
                                customAdapter.notifyDataSetChanged();

                            }
                        }
                        mProgressDialog.dismiss();
                    }
                    if(tipsArray.size() == 0){
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
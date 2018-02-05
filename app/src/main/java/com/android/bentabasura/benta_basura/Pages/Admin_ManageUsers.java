package com.android.bentabasura.benta_basura.Pages;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.bentabasura.benta_basura.Models.Users;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_userslist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_ManageUsers extends Admin_Navigation
{
  protected DrawerLayout mDrawer;
  private ListView listView1;
  String oldestPostId = "";
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReference;
  ProgressDialog mProgressDialog;
  ArrayList<Users> usersArray =new ArrayList<>();
  private custom_userslist customAdapter;
  SearchView searchTxt;

  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = (LayoutInflater) this
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View contentView = inflater.inflate(R.layout.activity_manage_users, null, false);
    mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawer.addView(contentView, 0);

    listView1 = (ListView) findViewById(R.id.listView1);

    searchTxt = (SearchView) findViewById(R.id.searchTxt);

    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference("Users");
    mProgressDialog = new ProgressDialog(this);

    getUsersDataFromFirebase();

    customAdapter = new custom_userslist(this, usersArray);
    listView1.setAdapter(customAdapter);

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
  public void getUsersDataFromFirebase() {


    databaseReference.orderByChild("reverseDate").addValueEventListener(new ValueEventListener() {

      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {

        if (dataSnapshot.exists()) {
          for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
            boolean found = false;
            oldestPostId = postSnapShot.getKey();

            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            for (Users usersItem : usersArray) {
              if (!TextUtils.isEmpty(usersItem.getUserid()) && !TextUtils.isEmpty(oldestPostId)) {
                if (usersItem.getUserid().equals(oldestPostId)) {
                  found = true;
                }
              }
            }

            if (!found) {

              Users users = postSnapShot.getValue(Users.class);

              users.setUserid(postSnapShot.getKey().toString());
              usersArray.add(users);
              customAdapter.notifyDataSetChanged();

            }
          }
          mProgressDialog.dismiss();
        }
        if(usersArray.size() == 0){
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
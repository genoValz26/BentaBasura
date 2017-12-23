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

import com.android.bentabasura.benta_basura.Models.News;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_newslist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_ManageNews extends Admin_Navigation
{
    protected DrawerLayout mDrawer;
    private Intent addNewsPage;
    private Button addNewsBtn;
    private ListView listView1;
    String oldestPostId = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog mProgressDialog;
    ArrayList<News> newsArray =new ArrayList<>();
    private custom_newslist customAdapter;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_manage_news, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addView(contentView, 0);

        addNewsPage = new Intent(Admin_ManageNews.this,Admin_AddNews.class);

        addNewsBtn = (Button) findViewById(R.id.btnAddNews);
        listView1 = (ListView) findViewById(R.id.listView1);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("News");
        mProgressDialog = new ProgressDialog(this);

        getCraftDataFromFirebase();

        customAdapter = new custom_newslist(this, newsArray);
        listView1.setAdapter(customAdapter);

        addNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addNewsPage);
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

                        for (News newsItem : newsArray) {
                            if (!TextUtils.isEmpty(newsItem.getNewsID()) && !TextUtils.isEmpty(oldestPostId)) {
                                if (newsItem.getNewsID().equals(oldestPostId)) {
                                    found = true;
                                }
                            }
                        }

                        if (!found) {

                           News news = postSnapShot.getValue(News.class);
                            news.setNewsID(postSnapShot.getKey().toString());
                            newsArray.add(news);
                            customAdapter.notifyDataSetChanged();

                        }
                    }
                    mProgressDialog.dismiss();
                }
                if(newsArray.size() == 0){
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
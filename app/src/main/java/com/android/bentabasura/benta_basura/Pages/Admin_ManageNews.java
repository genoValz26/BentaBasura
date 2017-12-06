package com.android.bentabasura.benta_basura.Pages;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.android.bentabasura.benta_basura.R;

public class Admin_ManageNews extends Admin_Navigation
{
    protected DrawerLayout mDrawer;
    private Intent addNewsPage;
    private Button addNewsBtn;
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

        addNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addNewsPage);
            }
        });

    }
}
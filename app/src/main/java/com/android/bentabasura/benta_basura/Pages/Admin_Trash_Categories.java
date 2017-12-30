package com.android.bentabasura.benta_basura.Pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.android.bentabasura.benta_basura.R;

/**
 * Created by Geno on 12/30/2017.
 */

public class Admin_Trash_Categories extends Admin_Navigation {
    protected DrawerLayout mDrawer;
    Button btnPaper, btnPlastic, btnWood, btnMetal;
    Intent trashIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_categories_trash, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addView(contentView, 0);

        btnPaper = (Button) findViewById(R.id.btnPapers);
        btnPlastic = (Button) findViewById(R.id.btnPlastic);
        btnWood = (Button) findViewById(R.id.btnWood);
        btnMetal = (Button) findViewById(R.id.btnMetal);


        trashIntent = new Intent(Admin_Trash_Categories.this, Admin_ManageTrash.class);

        btnPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trashIntent.putExtra("Category", "Paper");
                startActivity(trashIntent);
            }
        });
        btnPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trashIntent.putExtra("Category", "Plastic");
                startActivity(trashIntent);
            }
        });
        btnWood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trashIntent.putExtra("Category", "Wood");
                startActivity(trashIntent);
            }
        });
        btnMetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trashIntent.putExtra("Category", "Metal");
                startActivity(trashIntent);
            }
        });
    }

}

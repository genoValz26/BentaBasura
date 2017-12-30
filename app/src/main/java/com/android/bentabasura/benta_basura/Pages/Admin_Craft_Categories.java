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
 * Created by Geno on 12/29/2017.
 */

public class Admin_Craft_Categories extends Admin_Navigation {

    protected DrawerLayout mDrawer;
    Button btnDecorations, btnFurniture, btnProjects, btnAccessories;
    Intent craftIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_craft_categories_main, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addView(contentView, 0);

        btnDecorations = (Button) findViewById(R.id.btnDecorations);
        btnFurniture = (Button) findViewById(R.id.btnFurniture);
        btnProjects = (Button) findViewById(R.id.btnProjects);
        btnAccessories = (Button) findViewById(R.id.btnAccessories);

        craftIntent = new Intent(Admin_Craft_Categories.this, Admin_ManageCraft.class);

        btnDecorations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                craftIntent.putExtra("Category", "Decoration");
                startActivity(craftIntent);
            }
        });
        btnFurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                craftIntent.putExtra("Category", "Furniture");
                startActivity(craftIntent);
            }
        });
        btnProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                craftIntent.putExtra("Category", "Projects");
                startActivity(craftIntent);
            }
        });
        btnAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                craftIntent.putExtra("Category", "Accessories");
                startActivity(craftIntent);
            }
        });
    }

}

package com.android.bentabasura.benta_basura;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by reymond on 13/10/2017.
 */

public class Craft_Categories  extends AppCompatActivity implements View.OnClickListener{
    Button btnDecoratons,btnFurniture,btnProjects;
    Intent craftIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craft_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnDecoratons = (Button) findViewById(R.id.btnDecorations);
        btnFurniture = (Button) findViewById(R.id.btnFurniture);
        btnProjects = (Button) findViewById(R.id.btnProjects);


        btnDecoratons.setOnClickListener(this);
        btnFurniture.setOnClickListener(this);
        btnProjects.setOnClickListener(this);


       craftIntent = new Intent(Craft_Categories.this, BuyCrafted.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnDecorations:
            {
                craftIntent.putExtra("Category", "Decorations");
                startActivity( craftIntent);
                break;
            }
            case R.id.btnFurniture:
            {
                craftIntent.putExtra("Category", "Furniture");
                startActivity( craftIntent);
                break;
            }
            case R.id.btnProjects:
            {
                craftIntent.putExtra("Category", "Projects");
                startActivity( craftIntent);
                break;
            }

        }
    }
}

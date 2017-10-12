package com.android.bentabasura.benta_basura;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by gd185082 on 10/12/2017.
 */

public class Categories extends AppCompatActivity implements View.OnClickListener{

    Button btnPaper, btnPlastic, btnWood, btnMetal;
    Intent rawIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnPaper = (Button) findViewById(R.id.btnPapers);
        btnPlastic = (Button) findViewById(R.id.btnPlastic);
        btnWood = (Button) findViewById(R.id.btnWood);
        btnMetal = (Button) findViewById(R.id.btnMetal);

        btnPaper.setOnClickListener(this);
        btnPlastic.setOnClickListener(this);
        btnWood.setOnClickListener(this);
        btnMetal.setOnClickListener(this);

        rawIntent = new Intent(Categories.this, BuyRaw.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnPapers:
            {
                rawIntent.putExtra("Category", "Paper");
                startActivity(rawIntent);
                break;
            }
            case R.id.btnPlastic:
            {
                rawIntent.putExtra("Category", "Plastic");
                startActivity(rawIntent);
                break;
            }
            case R.id.btnWood:
            {
                rawIntent.putExtra("Category", "Wood");
                startActivity(rawIntent);
                break;
            }
            case R.id.btnMetal:
            {
                rawIntent.putExtra("Category", "Metal");
                startActivity(rawIntent);
                break;
            }
        }
    }
}

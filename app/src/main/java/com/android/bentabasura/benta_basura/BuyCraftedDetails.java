package com.android.bentabasura.benta_basura;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by reymond on 18/10/2017.
 */

public class BuyCraftedDetails extends AppCompatActivity {
    TextView txtCraftName, txtCraftDescription, txtCraftQuantity, txtCraftCategory, txtCraftPrice, txtSellerInfo,txtUploadedBy;
    Button addToCartBtn;
    ImageView imgThumbTrash;
    Intent receiveIntent;
    Bundle receivedBundle;
    ActiveUser activeUser;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_crafted_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiveIntent = getIntent();
        receivedBundle = receiveIntent.getExtras();

        txtCraftName = (TextView) findViewById(R.id.txtCraftName);
        imgThumbTrash = (ImageView) findViewById(R.id.imgThumbTrash);
        txtCraftDescription = (TextView) findViewById(R.id.txtCraftDescription);
        txtCraftQuantity = (TextView) findViewById(R.id.txtCraftQuantity);
        txtCraftPrice = (TextView) findViewById(R.id.txtCraftPrice);
        txtSellerInfo = (TextView) findViewById(R.id.txtSellerInfo);
        txtUploadedBy = (TextView) findViewById(R.id.txtUploadedBy);

        txtCraftName.setText(receivedBundle.get("CraftName").toString());
        Picasso.with(getApplicationContext()).load(receivedBundle.get("CraftPic").toString()).fit().into(imgThumbTrash);
        txtCraftDescription.setText(receivedBundle.get("CraftDescription").toString());
        txtCraftQuantity.setText(receivedBundle.get("CraftQuantity").toString());
        txtCraftPrice.setText(receivedBundle.get("CraftPrice").toString());
        txtSellerInfo.setText(receivedBundle.get("CraftSeller").toString());
        txtUploadedBy.setText(receivedBundle.get("UploadedBy").toString());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        activeUser = ActiveUser.getInstance();


    }
}

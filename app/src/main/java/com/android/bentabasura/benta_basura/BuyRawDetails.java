package com.android.bentabasura.benta_basura;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by gd185082 on 10/11/2017.
 */

public class BuyRawDetails extends AppCompatActivity implements View.OnClickListener {

    TextView txtTrashName, txtTrashDescription, txtTrashQuantity, txtTrashCategory, txtTrashPrice, txtSellerInfo;
    Button addToCartBtn;
    ImageView imgThumbTrash;
    Intent receiveIntent;
    Bundle receivedBundle;
    ActiveUser activeUser;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_raw_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiveIntent = getIntent();
        receivedBundle = receiveIntent.getExtras();

        txtTrashName = (TextView) findViewById(R.id.txtTrashName);
        imgThumbTrash = (ImageView) findViewById(R.id.imgThumbTrash);
        txtTrashDescription = (TextView) findViewById(R.id.txtTrashDescription);
        txtTrashQuantity = (TextView) findViewById(R.id.txtTrashQuantity);
        txtTrashCategory = (TextView) findViewById(R.id.txtTrashCategory);
        txtTrashPrice = (TextView) findViewById(R.id.txtTrashPrice);
        txtSellerInfo = (TextView) findViewById(R.id.txtSellerInfo);

        addToCartBtn = (Button) findViewById(R.id.addToCartBtn);

        txtTrashName.setText(receivedBundle.get("TrashName").toString());
        Picasso.with(getApplicationContext()).load(receivedBundle.get("TrashPic").toString()).fit().into(imgThumbTrash);
        txtTrashDescription.setText(receivedBundle.get("TrashDescription").toString());
        txtTrashQuantity.setText(receivedBundle.get("TrashQuantity").toString());
        txtTrashCategory.setText(receivedBundle.get("TrashCategory").toString());
        txtTrashPrice.setText(receivedBundle.get("TrashPrice").toString());
        txtSellerInfo.setText(receivedBundle.get("TrashSeller").toString());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        activeUser = ActiveUser.getInstance();

        addToCartBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String itemId = databaseReference.push().getKey();
        databaseReference.child("Cart").child(activeUser.getUserId()).child(receivedBundle.get("TrashId").toString()).setValue(true);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("sold", "1");

        databaseReference.child("Trash").child(receivedBundle.get("TrashCategory").toString()).child(receivedBundle.get("TrashId").toString()).updateChildren(hashMap);

    }
}

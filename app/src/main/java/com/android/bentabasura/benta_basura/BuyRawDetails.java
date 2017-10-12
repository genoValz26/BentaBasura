package com.android.bentabasura.benta_basura;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by gd185082 on 10/11/2017.
 */

public class BuyRawDetails extends AppCompatActivity {

    TextView txtTrashName, txtTrashDescription, txtTrashQuantity, txtTrashCategory, txtTrashPrice, txtSellerInfo;
    ImageView imgThumbTrash;
    Intent receiveIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_raw_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiveIntent = getIntent();
        Bundle receivedBundle = receiveIntent.getExtras();

        txtTrashName = (TextView) findViewById(R.id.txtTrashName);
        imgThumbTrash = (ImageView) findViewById(R.id.imgThumbTrash);
        txtTrashDescription = (TextView) findViewById(R.id.txtTrashDescription);
        txtTrashQuantity = (TextView) findViewById(R.id.txtTrashQuantity);
        txtTrashCategory = (TextView) findViewById(R.id.txtTrashCategory);
        txtTrashPrice = (TextView) findViewById(R.id.txtTrashPrice);
        txtSellerInfo = (TextView) findViewById(R.id.txtSellerInfo);


        txtTrashName.setText(receivedBundle.get("TrashName").toString());
        Picasso.with(getApplicationContext()).load(receivedBundle.get("TrashPic").toString()).fit().into(imgThumbTrash);
        txtTrashDescription.setText(receivedBundle.get("TrashDescription").toString());
        txtTrashQuantity.setText(receivedBundle.get("TrashQuantity").toString());
        txtTrashCategory.setText(receivedBundle.get("TrashCategory").toString());
        txtTrashPrice.setText(receivedBundle.get("TrashPrice").toString());
        txtSellerInfo.setText(receivedBundle.get("TrashSeller").toString());

    }

}

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
 * Created by gd185082 on 10/11/2017.
 */

public class BuyRawDetails extends AppCompatActivity {

    TextView txtTrashName, txtTrashDescription, txtTrashQuantity, txtTrashCategory, txtTrashPrice, txtSellerInfo,txtUploadedBy;
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

        txtTrashName = (TextView) findViewById(R.id.txtCraftName);
        imgThumbTrash = (ImageView) findViewById(R.id.imgThumbTrash);
        txtTrashDescription = (TextView) findViewById(R.id.txtCraftDescription);
        txtTrashQuantity = (TextView) findViewById(R.id.txtCraftQuantity);
        txtTrashPrice = (TextView) findViewById(R.id.txtCraftPrice);
        txtSellerInfo = (TextView) findViewById(R.id.txtSellerInfo);
        txtUploadedBy = (TextView) findViewById(R.id.txtUploadedBy);

        txtTrashName.setText(receivedBundle.get("TrashName").toString());
        Picasso.with(getApplicationContext()).load(receivedBundle.get("TrashPic").toString()).fit().into(imgThumbTrash);
        txtTrashDescription.setText(receivedBundle.get("TrashDescription").toString());
        txtTrashQuantity.setText(receivedBundle.get("TrashQuantity").toString());
        txtTrashPrice.setText(receivedBundle.get("TrashPrice").toString());
        txtSellerInfo.setText(receivedBundle.get("TrashSeller").toString());
        txtUploadedBy.setText(receivedBundle.get("UploadedBy").toString());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        activeUser = ActiveUser.getInstance();


    }

   /*
    public void onClick(View view) {
        String itemId = databaseReference.push().getKey();
        databaseReference.child("Cart").child(activeUser.getUserId()).child(receivedBundle.get("TrashId").toString()).setValue(true);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("sold", "1");

        databaseReference.child("Trash").child(receivedBundle.get("TrashCategory").toString()).child(receivedBundle.get("TrashId").toString()).updateChildren(hashMap);

    }*/
}

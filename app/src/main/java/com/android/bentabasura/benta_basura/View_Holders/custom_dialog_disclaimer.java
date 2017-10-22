package com.android.bentabasura.benta_basura.View_Holders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by reymond on 22/10/2017.
 */

public class custom_dialog_disclaimer extends AppCompatActivity implements View.OnClickListener {

    Button btnOK;
    Bundle receivedBundle;
    Intent receiveIntent,sellerdetailsIntent;

    public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
       setContentView(R.layout.dialog_disclaimer);
        receiveIntent = custom_dialog_disclaimer.this.getIntent();
        receivedBundle = receiveIntent.getExtras();
        TextView tv1 = (TextView) findViewById(R.id.tv1);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);

    }
    public void onClick(View view) {

        sellerdetailsIntent =  new Intent(custom_dialog_disclaimer.this,custom_dialog_contact_seller.class);
        sellerdetailsIntent.putExtra("UserId",receivedBundle.get("UserId").toString());
        startActivity(sellerdetailsIntent);
        this.finishAndRemoveTask();
    }

}

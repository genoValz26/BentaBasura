package com.android.bentabasura.benta_basura.View_Holders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by reymond on 22/10/2017.
 */

public class custom_dialog_contact_seller extends AppCompatActivity implements View.OnClickListener {

    Button btnCall, btnSMS;
    ImageView sellerImage;
    TextView sellerName, sellerContact, sellerEmail, sellerAddress;
    Bundle receivedBundle;
    Intent receiveIntent;
    DatabaseReference databaseReference;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contact_seller);

        receiveIntent = custom_dialog_contact_seller.this.getIntent();
        receivedBundle = receiveIntent.getExtras();

        sellerName = (TextView) findViewById(R.id.sellerName);
        sellerContact = (TextView) findViewById(R.id.sellerContact);
        sellerAddress = (TextView) findViewById(R.id.sellerAddress);
        sellerEmail = (TextView) findViewById(R.id.sellerEmail);
        btnCall = (Button) findViewById(R.id.btnInterested);
        btnSMS = (Button) findViewById(R.id.btnSMS);
        btnCall.setOnClickListener(this);
        btnSMS.setOnClickListener(this);

        sellerImage = (ImageView) findViewById(R.id.sellerImage);

        sellerName.setVisibility(View.INVISIBLE);
        sellerAddress.setVisibility(View.INVISIBLE);
        sellerContact.setVisibility(View.INVISIBLE);
        sellerEmail.setVisibility(View.INVISIBLE);
        sellerImage.setVisibility(View.INVISIBLE);
        btnSMS.setVisibility(View.INVISIBLE);
        btnCall.setVisibility(View.INVISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(receivedBundle.get("UserId").toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sellerName.setText(dataSnapshot.child("username").getValue().toString());
                sellerEmail.setText(dataSnapshot.child("email").getValue().toString());
                sellerAddress.setText(dataSnapshot.child("address").getValue().toString());
                sellerContact.setText(dataSnapshot.child("contact_number").getValue().toString());
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("profile_picture").getValue().toString()).placeholder(R.drawable.progress_animation).fit().into(sellerImage);

                sellerName.setVisibility(View.VISIBLE);
                sellerAddress.setVisibility(View.VISIBLE);
                sellerContact.setVisibility(View.VISIBLE);
                sellerEmail.setVisibility(View.VISIBLE);
                sellerImage.setVisibility(View.VISIBLE);
                btnSMS.setVisibility(View.VISIBLE);
                btnCall.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnInterested:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + sellerContact.getText().toString()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
                break;
            case R.id.btnSMS:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", sellerContact.getText().toString(), null));
                startActivity(smsIntent);
                break;
        }
    }
}

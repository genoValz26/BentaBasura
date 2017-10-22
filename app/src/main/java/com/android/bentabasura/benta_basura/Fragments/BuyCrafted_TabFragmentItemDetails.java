package com.android.bentabasura.benta_basura.Fragments;

/**
 * Created by ccs on 10/18/17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Craft;
import com.android.bentabasura.benta_basura.Pages.MyItems_Edit_Craft;
import com.android.bentabasura.benta_basura.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BuyCrafted_TabFragmentItemDetails extends Fragment implements View.OnClickListener{


    ActiveUser activeUser;
    ProgressDialog mProgressDialog;
    TextView txtCraftName, txtCraftDescription, txtCraftQuantity, txtCraftPrice, txtSellerInfo, txtUploadedBy;
    Button btnEdit,btnCall,btnSMS;
    ImageView imgThumbCraft;
    Bundle receivedBundle;
    Intent receiveIntent,editCraftpage,detailsIntent;
    DatabaseReference databaseReference;
    String userid;
    private ArrayList<Craft> craft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_buy_crafted, container, false);

        mProgressDialog = new ProgressDialog(container.getContext());

        receiveIntent = getActivity().getIntent();
        receivedBundle = receiveIntent.getExtras();

        txtCraftName = (TextView) view.findViewById(R.id.txtCraftName);
        imgThumbCraft = (ImageView) view.findViewById(R.id.imgThumbCraft);
        txtCraftDescription = (TextView) view.findViewById(R.id.txtCraftDescription);
        txtCraftQuantity = (TextView) view.findViewById(R.id.txtCraftQuantity);
        txtCraftPrice = (TextView) view.findViewById(R.id.txtCraftPrice);
        txtSellerInfo = (TextView) view.findViewById(R.id.tv14);
        txtUploadedBy = (TextView) view.findViewById(R.id.txtSellerInfoCraft);

        editCraftpage = new Intent(getActivity().getApplicationContext(),MyItems_Edit_Craft.class);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnCall = (Button) view.findViewById(R.id.btnCall);
        btnSMS = (Button) view.findViewById(R.id.btnSMS);
        btnEdit.setOnClickListener(this);
        btnSMS.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        //btnEdit.setVisibility(View.GONE)
        
        databaseReference = FirebaseDatabase.getInstance().getReference();
        activeUser = ActiveUser.getInstance();

        txtCraftName.setText(receivedBundle.get("CraftName").toString());
        Picasso.with(getActivity().getApplicationContext()).load(receivedBundle.get("CraftPic").toString()).placeholder(R.drawable.progress_animation).fit().into(imgThumbCraft);
        txtCraftDescription.setText(receivedBundle.get("CraftDescription").toString());
        txtCraftQuantity.setText(receivedBundle.get("CraftQuantity").toString());
        txtCraftPrice.setText("Php " + receivedBundle.get("CraftPrice").toString() + ".00");


        databaseReference.child("Users").child(receivedBundle.get("UploadedBy").toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtSellerInfo.setText(dataSnapshot.child("firstname").getValue().toString() + " " + dataSnapshot.child("lastname").getValue().toString());
                txtUploadedBy.setText(dataSnapshot.child("contact_number").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // editCraftpage = new Intent(BuyCrafted_TabFragmentItemDetails.this,MyItems_Edit_Craft.class);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnEdit:
                String craftid = receivedBundle.get("CraftId").toString();
                String craftCategory = receivedBundle.get("CraftCategory").toString();
                detailsIntent = new Intent(getActivity().getApplicationContext(), MyItems_Edit_Craft.class);
                // showMessage(craftid);
                detailsIntent.putExtra("CraftID", craftid);
                detailsIntent.putExtra("CraftCategory", craftCategory);

                startActivity(detailsIntent);
                break;
            case R.id.btnCall:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+txtUploadedBy.getText().toString()));
                startActivity(callIntent);
                break;
            case R.id.btnSMS:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",txtUploadedBy.getText().toString(),null));
                startActivity(smsIntent);
                break;
        }
    }
    public void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
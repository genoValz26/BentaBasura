package com.android.bentabasura.benta_basura;

/**
 * Created by ccs on 10/18/17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BuyCrafted_TabFragmentItemDetails extends Fragment implements View.OnClickListener{


    ActiveUser activeUser;
    ProgressDialog mProgressDialog;
    TextView txtCraftName, txtCraftDescription, txtCraftQuantity, txtCraftPrice, txtSellerInfo, txtUploadedBy;
    Button btnEdit;
    ImageView imgThumbCraft;
    Intent receiveIntent;
    Bundle receivedBundle;
    Intent editCraftpage;
    DatabaseReference databaseReference;
    String userid;
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
        txtSellerInfo = (TextView) view.findViewById(R.id.txtSellerInfoCraft);
        //txtUploadedBy = (TextView) findViewById(R.id.txtUploadedBy);

        editCraftpage = new Intent(getActivity().getApplicationContext(),MyItems_Edit_Craft.class);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);
        //btnEdit.setVisibility(View.GONE);

        txtCraftName.setText(receivedBundle.get("CraftName").toString());
        Picasso.with(getActivity().getApplicationContext()).load(receivedBundle.get("CraftPic").toString()).placeholder(R.drawable.progress_animation).fit().into(imgThumbCraft);
        txtCraftDescription.setText(receivedBundle.get("CraftDescription").toString());
        txtCraftQuantity.setText(receivedBundle.get("CraftQuantity").toString());
        txtCraftPrice.setText("Php " + receivedBundle.get("CraftPrice").toString() + ".00");
        txtSellerInfo.setText(receivedBundle.get("CraftSeller").toString());
        //txtUploadedBy.setText(receivedBundle.get("UploadedBy").toString());
       // editCraftpage = new Intent(BuyCrafted_TabFragmentItemDetails.this,MyItems_Edit_Craft.class);

        
        databaseReference = FirebaseDatabase.getInstance().getReference();
        activeUser = ActiveUser.getInstance();

        return view;
    }


    @Override
    public void onClick(View view) {

        startActivity(editCraftpage);
        //showMessage(databaseReference.child("Craft").().getKey().toString());

    }
    public void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
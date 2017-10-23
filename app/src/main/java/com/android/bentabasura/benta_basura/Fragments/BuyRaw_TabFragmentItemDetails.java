package com.android.bentabasura.benta_basura.Fragments;

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

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Pages.Login;
import com.android.bentabasura.benta_basura.Pages.MyItems_Edit_Trash;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_dialog_disclaimer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BuyRaw_TabFragmentItemDetails extends Fragment implements View.OnClickListener {


    ActiveUser activeUser;
    ProgressDialog mProgressDialog;
    TextView txtTrashName, txtTrashDescription, txtTrashQuantity, txtTrashPrice, txtSellerInfo, txtUploadedBy;
    Button btnEdit,btnInterested;
    ImageView imgThumbRaw;
    Intent receiveIntent,editTrashPage,detailsIntent,sellerdetailsIntent;
    Bundle receivedBundle;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_buy_raw, container, false);

        //set persist to true
        Login.setPersist(true);

        mProgressDialog = new ProgressDialog(container.getContext());

        receiveIntent = getActivity().getIntent();
        receivedBundle = receiveIntent.getExtras();

        editTrashPage = new Intent(getActivity().getApplicationContext(),MyItems_Edit_Trash.class);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnInterested = (Button) view.findViewById(R.id.btnInterested);

        btnEdit.setOnClickListener(this);
        btnInterested.setOnClickListener(this);

        txtTrashName = (TextView) view.findViewById(R.id.txtTrashName);
        imgThumbRaw = (ImageView) view.findViewById(R.id.imgThumbRaw);
        txtTrashDescription = (TextView) view.findViewById(R.id.txtRawDescription);
        txtTrashQuantity = (TextView) view.findViewById(R.id.txtRawQuantity);
        txtTrashPrice = (TextView) view.findViewById(R.id.txtRawPrice);
        txtSellerInfo = (TextView) view.findViewById(R.id.textView14);
        txtUploadedBy = (TextView) view.findViewById(R.id.txtSellerInfo);

        txtTrashName.setText(receivedBundle.get("TrashName").toString());
        Picasso.with(getActivity().getApplicationContext()).load(receivedBundle.get("TrashPic").toString()).placeholder(R.drawable.progress_animation).fit().into(imgThumbRaw);
        txtTrashDescription.setText(receivedBundle.get("TrashDescription").toString());
        txtTrashQuantity.setText(receivedBundle.get("TrashQuantity").toString() + " Kg/s" );
        txtTrashPrice.setText("Php " + receivedBundle.get("TrashPrice").toString() + ".00");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        activeUser = ActiveUser.getInstance();

        databaseReference.child("Users").child(receivedBundle.get("UploadedBy").toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtSellerInfo.setText(dataSnapshot.child("firstname").getValue().toString() + " " + dataSnapshot.child("lastname").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnEdit:
                detailsIntent = new Intent(getActivity().getApplicationContext(), MyItems_Edit_Trash.class);
                detailsIntent.putExtra("TrashID", receivedBundle.get("TrashId").toString());
                detailsIntent.putExtra("TrashCategory", receivedBundle.get("TrashCategory").toString());
                startActivity(detailsIntent);
                break;
            case R.id.btnInterested:
                sellerdetailsIntent = new Intent(getActivity().getApplicationContext(),custom_dialog_disclaimer.class);
                sellerdetailsIntent.putExtra("UserId",receivedBundle.get("UploadedBy").toString());
                startActivity(sellerdetailsIntent);
                break;

        }
    }
    public void showMessage(String message){
        Toast.makeText(getActivity().getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
}
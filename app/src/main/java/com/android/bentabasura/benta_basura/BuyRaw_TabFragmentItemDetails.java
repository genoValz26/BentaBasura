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
    Button btnEdit;
    ImageView imgThumbRaw;
    Intent receiveIntent,editTrashPage;
    Bundle receivedBundle;

    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_buy_raw, container, false);

        mProgressDialog = new ProgressDialog(container.getContext());

        receiveIntent = getActivity().getIntent();
        receivedBundle = receiveIntent.getExtras();

        editTrashPage = new Intent(getActivity().getApplicationContext(),MyItems_Edit_Craft.class);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);

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
        //txtSellerInfo.setText(receivedBundle.get("TrashSeller").toString());
        //txtUploadedBy.setText(receivedBundle.get("UploadedBy").toString());

        databaseReference = FirebaseDatabase.getInstance().getReference();
        activeUser = ActiveUser.getInstance();

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

        return view;
    }

    @Override
    public void onClick(View view) {
        startActivity(editTrashPage);
    }
}
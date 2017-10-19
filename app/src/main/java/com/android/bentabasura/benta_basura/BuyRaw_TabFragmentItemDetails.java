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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BuyRaw_TabFragmentItemDetails extends Fragment {


    ActiveUser activeUser;
    ProgressDialog mProgressDialog;
    TextView txtTrashName, txtTrashDescription, txtTrashQuantity, txtTrashPrice, txtSellerInfo, txtUploadedBy;
    Button addToCartBtn;
    ImageView imgThumbRaw;
    Intent receiveIntent;
    Bundle receivedBundle;

    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_buy_raw, container, false);

        mProgressDialog = new ProgressDialog(container.getContext());

        receiveIntent = getActivity().getIntent();
        receivedBundle = receiveIntent.getExtras();

        txtTrashName = (TextView) view.findViewById(R.id.txtTrashName);
        imgThumbRaw = (ImageView) view.findViewById(R.id.imgThumbRaw);
        txtTrashDescription = (TextView) view.findViewById(R.id.txtRawDescription);
        txtTrashQuantity = (TextView) view.findViewById(R.id.txtRawQuantity);
        txtTrashPrice = (TextView) view.findViewById(R.id.txtRawPrice);
        txtSellerInfo = (TextView) view.findViewById(R.id.txtSellerInfo);
        //txtUploadedBy = (TextView) view.findViewById(R.id.txtUploadedBy);

        txtTrashName.setText(receivedBundle.get("TrashName").toString());
        Picasso.with(getActivity().getApplicationContext()).load(receivedBundle.get("TrashPic").toString()).fit().into(imgThumbRaw);
        txtTrashDescription.setText(receivedBundle.get("TrashDescription").toString());
        txtTrashQuantity.setText(receivedBundle.get("TrashQuantity").toString());
        txtTrashPrice.setText(receivedBundle.get("TrashPrice").toString());
        txtSellerInfo.setText(receivedBundle.get("TrashSeller").toString());
        //txtUploadedBy.setText(receivedBundle.get("UploadedBy").toString());



        databaseReference = FirebaseDatabase.getInstance().getReference();
        activeUser = ActiveUser.getInstance();

        return view;
    }
}
package com.android.bentabasura.benta_basura.Fragments;

/**
 * Created by ccs on 10/18/17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Craft;
import com.android.bentabasura.benta_basura.Models.Notification;
import com.android.bentabasura.benta_basura.Pages.Login;
import com.android.bentabasura.benta_basura.Pages.MyItems_Edit_Craft;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_dialog_contact_seller;
import com.android.bentabasura.benta_basura.View_Holders.custom_dialog_disclaimer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BuyCrafted_TabFragmentItemDetails extends Fragment implements View.OnClickListener{


    ActiveUser activeUser;
    ProgressDialog mProgressDialog;
    TextView txtCraftName, txtCraftDescription, txtCraftQuantity, txtCraftPrice, txtSellerInfo, txtUploadedBy;
    Button btnEdit,btnInterested;
    ImageView imgThumbCraft;
    Bundle receivedBundle;
    Intent receiveIntent,editCraftpage,detailsIntent,sellerdetailsIntent;
    DatabaseReference databaseReference, databaseReferenceNotif;
    Boolean found = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_buy_crafted, container, false);

        //set persist to true
        Login.setPersist(true);

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
        btnInterested = (Button) view.findViewById(R.id.btnInterested);

        btnEdit.setOnClickListener(this);
        btnInterested.setOnClickListener(this);

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
                txtSellerInfo.setText(dataSnapshot.child("fullname").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Craft").child(receivedBundle.get("CraftCategory").toString()).child(receivedBundle.get("CraftId").toString()).child("Interested").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                found = false;
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    if (postSnapShot.getValue().toString().equals(activeUser.getUserId()))
                    {
                        btnInterested.setText("View Seller Contact");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        txtUploadedBy.setVisibility(View.GONE);


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
                detailsIntent.putExtra("CraftID", craftid);
                detailsIntent.putExtra("CraftCategory", craftCategory);
                startActivity(detailsIntent);
                break;

            case R.id.btnInterested:

                if (btnInterested.getText().toString().equals("View Seller Contact"))
                {
                    sellerdetailsIntent =  new Intent(getActivity(),custom_dialog_contact_seller.class);
                    sellerdetailsIntent.putExtra("UserId",receivedBundle.get("UploadedBy").toString());
                    startActivity(sellerdetailsIntent);
                }
                else
                {
                    sellerdetailsIntent = new Intent(getActivity().getApplicationContext(), custom_dialog_disclaimer.class);
                    sellerdetailsIntent.putExtra("UserId", receivedBundle.get("UploadedBy").toString());
                    addNotification();
                    startActivity(sellerdetailsIntent);
                }
                break;
        }
    }

    public void showMessage(String message){
        Toast.makeText(getActivity().getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }

    public void addNotification()
    {
        final String profileImage = activeUser.getProfilePicture();
        final String profileName = activeUser.getFullname();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy hh:mm a");
        final String UploadedDate = sdf.format(currentTime);

        //Notification
        final String notifId = databaseReference.child("Notification").push().getKey();
        final String location = "Craft" + ":" + receivedBundle.get("CraftCategory").toString() + ":" + receivedBundle.get("CraftId").toString();
        final String message = activeUser.getFullname() + " is interested in your Craft " + receivedBundle.get("CraftName").toString() + ". Expect a call from him/her";
        final String ownerId = receivedBundle.get("UploadedBy").toString();
        final String profileId = activeUser.getUserId();

        if (!TextUtils.isEmpty(location) && !TextUtils.isEmpty(message) && !TextUtils.isEmpty(ownerId) && !TextUtils.isEmpty(profileId) && !TextUtils.isEmpty(profileImage) && !TextUtils.isEmpty(UploadedDate) )
        {
            final Notification newNotif = new Notification();

            newNotif.setNotifDbLink(location);
            newNotif.setNotifMessage(message);
            newNotif.setNotifOwnerId(ownerId);
            newNotif.setNotifBy(profileId);
            newNotif.setNotifRead("0");
            newNotif.setNotifNotify("0");
            newNotif.setNotifByPic(profileImage);
            newNotif.setNotifDate(UploadedDate);


            databaseReference.child("Craft").child(receivedBundle.get("CraftCategory").toString()).child(receivedBundle.get("CraftId").toString()).child("Interested").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    found = false;
                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        if (postSnapShot.getValue().toString().equals(profileId))
                        {
                            found = true;
                            break;
                        }

                    }

                    if(!found)
                    {
                        databaseReference.child("Notification").child(notifId).setValue(newNotif);
                        databaseReference.child("Craft").child(receivedBundle.get("CraftCategory").toString()).child(receivedBundle.get("CraftId").toString()).child("Interested").push().setValue(profileId);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
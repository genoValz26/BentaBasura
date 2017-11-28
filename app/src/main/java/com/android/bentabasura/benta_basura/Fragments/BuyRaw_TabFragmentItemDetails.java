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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Notification;
import com.android.bentabasura.benta_basura.Pages.Login;
import com.android.bentabasura.benta_basura.Pages.MyItems_Edit_Trash;
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
import java.util.Calendar;
import java.util.Date;

public class BuyRaw_TabFragmentItemDetails extends Fragment implements View.OnClickListener {


    ActiveUser activeUser;
    ProgressDialog mProgressDialog;
    TextView txtTrashName, txtTrashDescription, txtTrashQuantity, txtTrashPrice, txtSellerInfo, txtUploadedBy;
    Button btnEdit,btnInterested;
    ImageView imgThumbRaw;
    Intent receiveIntent,editTrashPage,detailsIntent,sellerdetailsIntent;
    Bundle receivedBundle;
    DatabaseReference databaseReference;
    Boolean found = false;
    RatingBar ratingBar;
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
        
        activeUser = ActiveUser.getInstance();

        if (!receivedBundle.get("UploadedBy").toString().equals(activeUser.getUserId()))
        {
            btnEdit.setVisibility(View.INVISIBLE);
        }
        else
        {
            btnInterested.setVisibility(View.INVISIBLE);
        }

        txtTrashName = (TextView) view.findViewById(R.id.txtTrashName);
        imgThumbRaw = (ImageView) view.findViewById(R.id.imgThumbRaw);
        txtTrashDescription = (TextView) view.findViewById(R.id.txtRawDescription);
        txtTrashQuantity = (TextView) view.findViewById(R.id.txtRawQuantity);
        txtTrashPrice = (TextView) view.findViewById(R.id.txtRawPrice);
        txtSellerInfo = (TextView) view.findViewById(R.id.textView14);
        txtUploadedBy = (TextView) view.findViewById(R.id.txtSellerInfo);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        txtTrashName.setText(receivedBundle.get("TrashName").toString());
        Picasso.with(getActivity().getApplicationContext()).load(receivedBundle.get("TrashPic").toString()).placeholder(R.drawable.progress_animation).fit().into(imgThumbRaw);
        txtTrashDescription.setText(receivedBundle.get("TrashDescription").toString());
        txtTrashQuantity.setText(receivedBundle.get("TrashQuantity").toString());
        txtTrashPrice.setText("Php " + receivedBundle.get("TrashPrice").toString());

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child(receivedBundle.get("UploadedBy").toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtSellerInfo.setText(dataSnapshot.child("fullname").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateButtonText();

        txtUploadedBy.setVisibility(View.GONE);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                showMessage(String.valueOf(rating));
                databaseReference.child("Trash").child(receivedBundle.get("TrashCategory").toString()).child(receivedBundle.get("TrashId").toString()).child("Ratings").child(activeUser.getUserId()).child("Rate").setValue(String.valueOf(rating));
                startActivity(getActivity().getIntent());
            }
        });


        return view;

}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEdit:
                detailsIntent = new Intent(getActivity().getApplicationContext(), MyItems_Edit_Trash.class);
                detailsIntent.putExtra("TrashID", receivedBundle.get("TrashId").toString());
                detailsIntent.putExtra("TrashCategory", receivedBundle.get("TrashCategory").toString());
                startActivity(detailsIntent);

                //showMessage(receivedBundle.get("TrashId").toString());
                break;
            case R.id.btnInterested:
                if (btnInterested.getText().toString().equals("View Seller Contact")) {
                    sellerdetailsIntent = new Intent(getActivity(), custom_dialog_contact_seller.class);
                    sellerdetailsIntent.putExtra("UserId", receivedBundle.get("UploadedBy").toString());
                    startActivity(sellerdetailsIntent);
                }
                else{
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
        final String location = "Trash" + ":" + receivedBundle.get("TrashCategory").toString() + ":" + receivedBundle.get("TrashId").toString();
        final String message = activeUser.getFullname() + " is interested in your Trash " + receivedBundle.get("TrashName").toString() + ". Expect a call from him/her";
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


            databaseReference.child("Trash").child(receivedBundle.get("TrashCategory").toString()).child(receivedBundle.get("TrashId").toString()).child("Interested").addListenerForSingleValueEvent(new ValueEventListener() {
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
                        databaseReference.child("Trash").child(receivedBundle.get("TrashCategory").toString()).child(receivedBundle.get("TrashId").toString()).child("Interested").push().setValue(profileId);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        updateButtonText();
    }

    public void updateButtonText()
    {
        databaseReference.child("Trash").child(receivedBundle.get("TrashCategory").toString()).child(receivedBundle.get("TrashId").toString()).child("Interested").addListenerForSingleValueEvent(new ValueEventListener() {
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
    }
}
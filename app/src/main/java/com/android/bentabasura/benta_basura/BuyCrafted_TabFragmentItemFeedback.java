package com.android.bentabasura.benta_basura;

/**
 * Created by ccs on 10/18/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BuyCrafted_TabFragmentItemFeedback extends Fragment {

    DatabaseReference databaseReference, databaseReferenceNotif;
    ActiveUser activeUser;
    Intent receiveIntent;
    Bundle receivedBundle;
    EditText txtComment;
    ListView lstComments;
    custom_commentlist_crafted commentAdapter;
    ArrayList<Comment>  commentArray = new ArrayList<>();
    String oldestCommentId;

    int currentVisibleItemCount;
    int currentScrollState;
    int currentFirstVisibleItem;
    int totalItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_crafted_comment, container, false);



        activeUser =  ActiveUser.getInstance();

        receiveIntent = getActivity().getIntent();
        receivedBundle = receiveIntent.getExtras();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Craft").child(receivedBundle.get("CraftCategory").toString()).child(receivedBundle.get("CraftId").toString()).child("Comment");
        databaseReferenceNotif = FirebaseDatabase.getInstance().getReference().child("Notification");

        Button btnSend = (Button) view.findViewById(R.id.btn_send);
        txtComment = (EditText) view.findViewById(R.id.text_content);
        lstComments = (ListView) view.findViewById(R.id.lstComments);

        commentAdapter = new custom_commentlist_crafted(container.getContext(), commentArray);
        lstComments.setAdapter(commentAdapter);
        lstComments.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                currentScrollState = scrollState;
                isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;
                totalItem = totalItemCount;
            }
        });

        getCommentFromDatabase();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String profileImage = activeUser.getProfilePicture();
                String profileName = activeUser.getFullname();

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy hh:mm a");
                String UploadedDate = sdf.format(currentTime);

                String commentDate = UploadedDate;
                String comment = txtComment.getText().toString();

                if(!comment.equals(""))
                {
                    String commentId = databaseReference.push().getKey();
                    Comment newComment = new Comment();
                    newComment.setProfileImage(profileImage);
                    newComment.setProfileName(profileName);
                    newComment.setCommentDate(commentDate);
                    newComment.setComment(comment);

                    databaseReference.child(commentId).setValue(newComment);

                    txtComment.setText("");

                    //Notification
                    String notifId = databaseReferenceNotif.push().getKey();
                    String location = "Craft" + ":" + receivedBundle.get("CraftCategory").toString() + ":" + receivedBundle.get("CraftId").toString();
                    String message = activeUser.getFullname() + " added a comment on Craft " + receivedBundle.get("CraftName").toString();
                    String ownerId = receivedBundle.get("UploadedBy").toString();

                    Notification newNotif = new Notification();
                    newNotif.setNotifDbLink(location);
                    newNotif.setNotifMessage(message);
                    newNotif.setNotifOwnerId(ownerId);
                    newNotif.setNotifBy(activeUser.getUserId());
                    newNotif.setNotifRead("0");
                    newNotif.setNotifNotify("0");
                    newNotif.setNotifByPic(activeUser.getProfilePicture());
                    newNotif.setNotifDate(commentDate);

                    databaseReferenceNotif.child(notifId).setValue(newNotif);
                }

            }
        });

        return view;

    }

    public void getCommentFromDatabase() {

        databaseReference.limitToFirst(6).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        Boolean found = false;
                        oldestCommentId = postSnapShot.getKey();

                        for(Comment com : commentArray)
                        {
                            if(com.getCommendID().equals(oldestCommentId))
                            {
                                found = true;
                            }
                        }

                        if(!found)
                        {
                            Comment comment = postSnapShot.getValue(Comment.class);

                            comment.setCommendID(postSnapShot.getKey().toString());
                            commentArray.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isScrollCompleted() {
        if (totalItem - currentFirstVisibleItem == currentVisibleItemCount && this.currentScrollState == 0) {
            databaseReference.orderByKey().startAt(oldestCommentId).limitToFirst(3).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                        if (!oldestCommentId.equals(postSnapShot.getKey())) {
                            oldestCommentId = postSnapShot.getKey();

                            Comment comment = postSnapShot.getValue(Comment.class);

                            comment.setCommendID(postSnapShot.getKey().toString());
                            commentArray.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
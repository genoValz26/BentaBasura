package com.android.bentabasura.benta_basura;

/**
 * Created by ccs on 10/19/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import static com.android.bentabasura.benta_basura.R.drawable.user;


public class FirebaseNotificationService extends Service {

    public FirebaseDatabase mDatabase;
    FirebaseAuth firebaseAuth;
    Context context;
    static String TAG = "FirebaseService";
    static int counterNot = 0;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;
        mDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        setupNotificationListener();
    }


    private void setupNotificationListener()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            mDatabase.getReference().child("Notification")
                    .addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            for (DataSnapshot postSnapShot : dataSnapshot.getChildren())
                            {
                                if (!user.getUid().equals(postSnapShot.child("notifBy").getValue().toString()) && user.getUid().equals(postSnapShot.child("notifOwnerId").getValue().toString()) )
                                {
                                    if (postSnapShot.child("notifNotify").getValue().toString().equals("0"))
                                    {
                                        final String message = postSnapShot.child("notifMessage").getValue().toString();
                                        final String key = postSnapShot.getKey();

                                        String[] dbLink = postSnapShot.child("notifDbLink").getValue().toString().split(":");

                                        if (dbLink[0].equals("Trash"))
                                        {

                                            mDatabase.getReference(dbLink[0]).child(dbLink[1]).child(dbLink[2]).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    int counter = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

                                                    Intent trashIntent = new Intent(context, BuyRawDetails.class);
                                                    trashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                    trashIntent.putExtra("TrashName", dataSnapshot.child("trashName").getValue().toString());
                                                    trashIntent.putExtra("TrashPic", dataSnapshot.child("imageUrl").getValue().toString());
                                                    trashIntent.putExtra("TrashDescription", dataSnapshot.child("trashDescription").getValue().toString());
                                                    trashIntent.putExtra("TrashQuantity", dataSnapshot.child("trashQuantity").getValue().toString());
                                                    trashIntent.putExtra("TrashCategory", dataSnapshot.child("trashCategory").getValue().toString());
                                                    trashIntent.putExtra("TrashPrice", dataSnapshot.child("trashPrice").getValue().toString());
                                                    trashIntent.putExtra("TrashSeller", dataSnapshot.child("sellerContact").getValue().toString());
                                                    trashIntent.putExtra("TrashId", dataSnapshot.getKey());
                                                    trashIntent.putExtra("UploadedBy", dataSnapshot.child("uploadedBy").getValue().toString());

                                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, counter, trashIntent, PendingIntent.FLAG_ONE_SHOT);

                                                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                                    NotificationCompat.Builder mBuilder =
                                                            new NotificationCompat.Builder(context)
                                                                    .setSmallIcon(R.drawable.ic_bentabasura_logo)
                                                                    .setContentTitle("Benta Basura")
                                                                    .setContentText(message)
                                                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                                                    .setSound(soundUri)
                                                                    .setContentIntent(pendingIntent)
                                                                    .setAutoCancel(true);

                                                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                                    mNotificationManager.notify(counter, mBuilder.build());

                                                    flagNotificationAsSent(key);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                        else if (dbLink[0].equals("Craft"))
                                        {
                                            mDatabase.getReference(dbLink[0]).child(dbLink[1]).child(dbLink[2]).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    int counter = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

                                                    Intent craftIntent = new Intent(context, BuyCraftedDetails.class);
                                                    craftIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                    craftIntent.putExtra("CraftName", dataSnapshot.child("craftName").getValue().toString());
                                                    craftIntent.putExtra("CraftPic", dataSnapshot.child("imageUrl").getValue().toString());
                                                    craftIntent.putExtra("CraftDescription", dataSnapshot.child("craftDescription").getValue().toString());
                                                    craftIntent.putExtra("CraftQuantity", dataSnapshot.child("craftQuantity").getValue().toString());
                                                    craftIntent.putExtra("CraftCategory", dataSnapshot.child("craftCategory").getValue().toString());
                                                    craftIntent.putExtra("CraftPrice", dataSnapshot.child("craftPrice").getValue().toString());
                                                    craftIntent.putExtra("CraftSeller", dataSnapshot.child("sellerContact").getValue().toString());
                                                    craftIntent.putExtra("CraftId", dataSnapshot.getKey());
                                                    craftIntent.putExtra("UploadedBy", dataSnapshot.child("uploadedBy").getValue().toString());

                                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, counter, craftIntent, PendingIntent.FLAG_ONE_SHOT);

                                                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                                    NotificationCompat.Builder mBuilder =
                                                            new NotificationCompat.Builder(context)
                                                                    .setSmallIcon(R.drawable.ic_bentabasura_logo)
                                                                    .setContentTitle("Benta Basura")
                                                                    .setContentText(message)
                                                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                                                    .setSound(soundUri)
                                                                    .setContentIntent(pendingIntent)
                                                                    .setAutoCancel(true);


                                                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                                    mNotificationManager.notify(counter, mBuilder.build());

                                                    flagNotificationAsSent(key);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }


                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            // No user is signed in
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(getApplicationContext(), FirebaseNotificationService.class));
    }


    private void showNotification(Context context,  String notification_key){

    }

    private void flagNotificationAsSent(String notification_key) {
        mDatabase.getReference().child("Notification")
                .child(notification_key)
                .child("notifNotify")
                .setValue("1");
    }


}
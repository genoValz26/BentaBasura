package com.android.bentabasura.benta_basura.Pages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Notification;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.RoundedTransformation;
import com.android.bentabasura.benta_basura.View_Holders.custom_notiflist;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class Notifications extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private Intent reservedItems,profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems,loginpage;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    ActiveUser activeUser;
    FirebaseDatabase mDatabase;
    TextView navFullName, navEmail;
    ImageView navImage;
    ListView lstNotif;
    custom_notiflist notifAdapter;
    ArrayList<Notification> notifArray = new ArrayList<>();
    DatabaseReference databaseReference, databaseReferenceInt;
    String oldestNotifId;
    Button btnClear;
    private GoogleApiClient mGoogleApiClient;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        //set persist to true
        Login.setPersist(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePage = new Intent(Notifications.this, MyProfile.class);
        buyCrafted = new Intent(Notifications.this, Craft_Categories.class);
        buyRaw = new Intent(Notifications.this, Categories.class);
        sellCrafted = new Intent(Notifications.this, SellCrafted.class);
        sellRaw = new Intent(Notifications.this, SellRaw.class);
        notificationsPage = new Intent(Notifications.this, Notifications.class);
        homePage = new Intent(Notifications.this,Home.class);
        cartPage = new Intent(Notifications.this,Cart.class);
        historyPage = new Intent(Notifications.this,BoughtItems.class);
        myItems = new Intent(Notifications.this,MyItems.class);
        loginpage = new Intent(Notifications.this,Login.class);
        reservedItems = new Intent(Notifications.this,ReservedItems.class);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        activeUser = ActiveUser.getInstance();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navImage = (ImageView) headerView.findViewById(R.id.imageView);
        navFullName = (TextView) headerView.findViewById(R.id.txtFullNameMenu);
        navEmail = (TextView) headerView.findViewById(R.id.txtEmailMenu);

        navFullName.setText(activeUser.getFullname());
        navEmail.setText(activeUser.getEmail());
        Picasso.with(this).load(activeUser.getProfilePicture()).transform(new RoundedTransformation(50, 0)).fit().into(navImage);

        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        lstNotif = (ListView) findViewById(R.id.lstNotif);
        notifAdapter = new custom_notiflist(this, notifArray);
        lstNotif.setAdapter(notifAdapter);
        lstNotif.setOnItemClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        databaseReferenceInt = FirebaseDatabase.getInstance().getReference();
        getNotifFromDatabase();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        showMessage("Something went wrong. Please try again");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    public void showMessage(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuNotification:
                startActivity(notificationsPage);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_account:
                startActivity(profilePage);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_my_items:
                startActivity(myItems);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_reserved_items:
                startActivity(reservedItems);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.buy:
                if(navMenu.findItem(R.id.buy).getTitle().equals("Buy                                        +")) {
                    navMenu.findItem(R.id.buy_crafted).setVisible(true);
                    navMenu.findItem(R.id.buy_raw).setVisible(true);
                    navMenu.findItem(R.id.buy).setTitle("Buy                                        -");
                }
                else{
                    navMenu.findItem(R.id.buy_crafted).setVisible(false);
                    navMenu.findItem(R.id.buy_raw).setVisible(false);
                    navMenu.findItem(R.id.buy).setTitle("Buy                                        +");
                }
                break;
            case R.id.buy_crafted:
                startActivity(buyCrafted);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.buy_raw:
                startActivity(buyRaw);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.sell:
                if(navMenu.findItem(R.id.sell).getTitle().equals("Sell                                        +")) {
                    navMenu.findItem(R.id.sell_crafted).setVisible(true);
                    navMenu.findItem(R.id.sell_raw).setVisible(true);
                    navMenu.findItem(R.id.sell).setTitle("Sell                                        -");
                }
                else{
                    navMenu.findItem(R.id.sell_crafted).setVisible(false);
                    navMenu.findItem(R.id.sell_raw).setVisible(false);
                    navMenu.findItem(R.id.sell).setTitle("Sell                                        +");
                }
                break;
            case R.id.sell_crafted:
                startActivity(sellCrafted);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.sell_raw:
                startActivity(sellRaw);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.home:
                startActivity(homePage);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.history:
                startActivity(historyPage);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                logout();
                break;
        }
        return true;
    }
    public void logout() {
        buildDialog(this).show();
        return;
    }
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("BentaBasura");
        builder.setMessage("Are you sure you want to logout?");

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                startActivity(loginpage);
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder;
    }
    public void getNotifFromDatabase() {

        databaseReference.limitToFirst(30).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        Boolean found = false;
                        oldestNotifId = postSnapShot.getKey();

                        for(Notification notif : notifArray)
                        {
                            if(notif.getNotifId().equals(oldestNotifId))
                            {
                                found = true;
                            }
                        }

                        if(!found)
                        {
                            if( !postSnapShot.child("notifBy").getValue().toString().equals(activeUser.getUserId()) ) {
                                if(postSnapShot.child("notifOwnerId").getValue().toString().equals(activeUser.getUserId())) {
                                    if(postSnapShot.child("notifRead").getValue().toString().equals("0")) {
                                        Notification notifs = postSnapShot.getValue(Notification.class);

                                        notifs.setNotifId(postSnapShot.getKey().toString());
                                        notifArray.add(notifs);
                                        notifAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view)
    {
        for(Notification notif:notifArray)
        {
            databaseReference.child(notif.getNotifId()).child("notifRead").setValue("1");
        }

        notifArray.clear();
        notifAdapter.notifyDataSetChanged();

        lstNotif.setVisibility(View.INVISIBLE);
        startActivity(homePage);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int pos = i;
        final Notification getNotif = notifArray.get(i);

        String[] dbLink = getNotif.getNotifDbLink().split(":");

        if (dbLink[0].equals("Trash"))
        {
            mDatabase.getReference().child(dbLink[0]).child(dbLink[1]).child(dbLink[2]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Intent trashIntent = new Intent(getApplicationContext(), BuyRawDetails.class);
                    trashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    trashIntent.putExtra("TrashName", dataSnapshot.child("trashName").getValue().toString());
                    trashIntent.putExtra("TrashPic", dataSnapshot.child("imageUrl").getValue().toString());
                    trashIntent.putExtra("TrashDescription", dataSnapshot.child("trashDescription").getValue().toString());
                    trashIntent.putExtra("TrashQuantity", dataSnapshot.child("trashQuantity").getValue().toString());
                    trashIntent.putExtra("TrashCategory", dataSnapshot.child("trashCategory").getValue().toString());
                    trashIntent.putExtra("TrashPrice", dataSnapshot.child("trashPrice").getValue().toString());
                    trashIntent.putExtra("TrashSeller", dataSnapshot.child("meetupLocation").getValue().toString());
                    trashIntent.putExtra("TrashId", dataSnapshot.getKey());
                    trashIntent.putExtra("UploadedBy", dataSnapshot.child("uploadedBy").getValue().toString());

                    startActivity(trashIntent);

                    databaseReference.child(getNotif.getNotifId()).child("notifRead").setValue("1");

                    notifArray.remove(pos);
                    notifAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if (dbLink[0].equals("Craft"))
        {
            mDatabase.getReference().child(dbLink[0]).child(dbLink[1]).child(dbLink[2]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Intent craftIntent = new Intent(getApplicationContext(), BuyCraftedDetails.class);
                    craftIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    craftIntent.putExtra("CraftName", dataSnapshot.child("craftName").getValue().toString());
                    craftIntent.putExtra("CraftPic", dataSnapshot.child("imageUrl").getValue().toString());
                    craftIntent.putExtra("CraftDescription", dataSnapshot.child("craftDescription").getValue().toString());
                    craftIntent.putExtra("CraftQuantity", dataSnapshot.child("craftQuantity").getValue().toString());
                    craftIntent.putExtra("CraftCategory", dataSnapshot.child("craftCategory").getValue().toString());
                    craftIntent.putExtra("CraftPrice", dataSnapshot.child("craftPrice").getValue().toString());
                    craftIntent.putExtra("CraftSeller", dataSnapshot.child("meetupLocation").getValue().toString());
                    craftIntent.putExtra("CraftId", dataSnapshot.getKey());
                    craftIntent.putExtra("CraftResource", dataSnapshot.child("resourcesFrom").getValue().toString());
                    craftIntent.putExtra("UploadedBy", dataSnapshot.child("uploadedBy").getValue().toString());

                    startActivity(craftIntent);

                    databaseReference.child(getNotif.getNotifId()).child("notifRead").setValue("1");

                    notifArray.remove(pos);
                    notifAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}

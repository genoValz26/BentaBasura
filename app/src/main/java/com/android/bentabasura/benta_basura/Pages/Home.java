package com.android.bentabasura.benta_basura.Pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.News;
import com.android.bentabasura.benta_basura.Models.Tips;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.BadgeDrawable;
import com.android.bentabasura.benta_basura.Utils.RoundedTransformation;
import com.android.bentabasura.benta_basura.View_Holders.home_news_list;
import com.android.bentabasura.benta_basura.View_Holders.home_tips_list;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Intent reservedItems,profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,loginpage,myItems;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    String userid;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReferenceNot;
    StorageReference storageReference;
    public static final String TAG = "Home";
    public static final String DATABASE_PATH="image";
    public static final String STORAGE_PATH="image/";
    static Menu mn;

    TextView navFullName, navEmail,tvTop;
    ImageView navImage;
    ActiveUser activeUser;
    ScrollView scrollView;
    private GoogleApiClient mGoogleApiClient;

    private ListView newslist,tipslist;
    String oldestPostId = "";
    ProgressDialog mProgressDialog;
    ArrayList<News> newsArray =new ArrayList<>();
    ArrayList<Tips> tipsArray =new ArrayList<>();
    private home_news_list customnewsAdapter;
    private home_tips_list customtipsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //set persist to true
        Login.setPersist(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTop = (TextView) findViewById(R.id.textViewA);
        scrollView = (ScrollView) findViewById(R.id.sv);
        scrollView.smoothScrollTo(0,tvTop.getTop());
        progressDialog = new ProgressDialog(this);
        profilePage = new Intent(Home.this, MyProfile.class);
        buyCrafted = new Intent(Home.this, Craft_Categories.class);
        buyRaw = new Intent(Home.this, Categories.class);
        sellCrafted = new Intent(Home.this, SellCrafted.class);
        sellRaw = new Intent(Home.this, SellRaw.class);
        notificationsPage = new Intent(Home.this, Notifications.class);
        homePage = new Intent(Home.this,Home.class);
        cartPage = new Intent(Home.this,Cart.class);
        historyPage = new Intent(Home.this,BoughtItems.class);
        loginpage = new Intent(Home.this,Login.class);
        myItems = new Intent(Home.this,MyItems.class);
        reservedItems = new Intent(Home.this,ReservedItems.class);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //----------------------------------------------------------------
        activeUser = ActiveUser.getInstance();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navFullName = (TextView) headerView.findViewById(R.id.txtFullNameMenu);
        navEmail = (TextView) headerView.findViewById(R.id.txtEmailMenu);
        navImage = (ImageView) headerView.findViewById(R.id.imageView);


        navFullName.setText(activeUser.getFullname());
        navEmail.setText(activeUser.getEmail());
        Picasso.with(this).load(activeUser.getProfilePicture()).transform(new RoundedTransformation(280, 0)).placeholder(R.drawable.progress_animation).fit().into(navImage);

        //Set Fullname and Email

        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        //----------------------------------------------------------------
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);
        databaseReferenceNot = FirebaseDatabase.getInstance().getReference();

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

        mProgressDialog = new ProgressDialog(this);
        newslist = (ListView) findViewById(R.id.newslist);
        tipslist = (ListView) findViewById(R.id.tipslist);

        getNewsDataFromFirebase();

        customnewsAdapter = new home_news_list(this, newsArray);
        newslist.setAdapter(customnewsAdapter);

        getTipsDataFromFirebase();
        customtipsAdapter = new home_tips_list(this, tipsArray);
        tipslist.setAdapter(customtipsAdapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        mn = menu;

        getMenuInflater().inflate(R.menu.menu_action_bar, menu);


        databaseReferenceNot.child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int counter = 0;
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren())
                {
                    if(!activeUser.getUserId().equals(postSnapShot.child("notifBy").getValue().toString()) && activeUser.getUserId().equals(postSnapShot.child("notifOwnerId").getValue().toString()))
                    {
                        if(postSnapShot.child("notifRead").getValue().toString().equals("0"))
                        {
                            counter++;
                        }
                    }
                }

                 MenuItem itemCart = mn.findItem(R.id.menuNotification);
                LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
                setBadgeCount(getApplicationContext(), icon, String.valueOf(counter));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
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
    public void showMessage(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
    public void getNewsDataFromFirebase() {


        databaseReferenceNot.child("News").orderByChild("reverseDate").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        boolean found = false;
                        oldestPostId = postSnapShot.getKey();

                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();

                        for (News newsItem : newsArray) {
                            if (!TextUtils.isEmpty(newsItem.getNewsID()) && !TextUtils.isEmpty(oldestPostId)) {
                                if (newsItem.getNewsID().equals(oldestPostId)) {
                                    found = true;
                                }
                            }
                        }

                        if (!found) {

                            News news = postSnapShot.getValue(News.class);
                            news.setNewsID(postSnapShot.getKey().toString());
                            newsArray.add(news);
                            customnewsAdapter.notifyDataSetChanged();

                        }
                    }
                    mProgressDialog.dismiss();
                }
                if(newsArray.size() == 0){
                    newslist.setVisibility(View.INVISIBLE);
                    newslist.setVisibility(View.VISIBLE);
                }
                else
                {
                    newslist.setVisibility(View.VISIBLE);
                    //txtEmpty.setVisibility(View.INVISIBLE);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getTipsDataFromFirebase() {


        databaseReferenceNot.child("Tips").orderByChild("reverseDate").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        boolean found = false;
                        oldestPostId = postSnapShot.getKey();

                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();

                        for (Tips tipsItem : tipsArray) {
                            if (!TextUtils.isEmpty(tipsItem.getTipsID()) && !TextUtils.isEmpty(oldestPostId)) {
                                if (tipsItem.getTipsID().equals(oldestPostId)) {
                                    found = true;
                                }
                            }
                        }

                        if (!found) {

                            Tips tips = postSnapShot.getValue(Tips.class);

                            tips.setTipsID(postSnapShot.getKey().toString());
                            tipsArray.add(tips);
                            customtipsAdapter.notifyDataSetChanged();

                        }
                    }
                    mProgressDialog.dismiss();
                }
                if(tipsArray.size() == 0){
                    tipslist.setVisibility(View.INVISIBLE);
                    tipslist.setVisibility(View.VISIBLE);
                }
                else
                {
                    tipslist.setVisibility(View.VISIBLE);
                    //txtEmpty.setVisibility(View.INVISIBLE);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

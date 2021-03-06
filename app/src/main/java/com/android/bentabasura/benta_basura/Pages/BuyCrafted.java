package com.android.bentabasura.benta_basura.Pages;

import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Craft;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.RoundedTransformation;
import com.android.bentabasura.benta_basura.View_Holders.custom_craftlist;
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
import com.webianks.library.PopupBubble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class BuyCrafted extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnScrollListener {

    private Intent reservedItems,profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems,loginpage;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    private ListView lstCraft;
    ProgressDialog mProgressDialog;
    private custom_craftlist customAdapter;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<Craft> craftArray =new ArrayList<>();
    private int counter = 3;

    TextView navFullName, navEmail, txtEmpty;
    ImageView navImage;
    ActiveUser activeUser;
    SearchView searchTxt;

    private String oldestPostId;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private int currentFirstVisibleItem;
    private int totalItem;
    private LinearLayout lBelow;

    Bundle receivedBundle;
    private GoogleApiClient mGoogleApiClient;
    PopupBubble popupBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_crafted);

        //set persist to true
        Login.setPersist(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePage = new Intent(BuyCrafted.this, MyProfile.class);
        buyCrafted = new Intent(BuyCrafted.this, Craft_Categories.class);
        buyRaw = new Intent(BuyCrafted.this, Categories.class);
        sellCrafted = new Intent(BuyCrafted.this, SellCrafted.class);
        sellRaw = new Intent(BuyCrafted.this, SellRaw.class);
        notificationsPage = new Intent(BuyCrafted.this, Notifications.class);
        homePage = new Intent(BuyCrafted.this,Home.class);
        cartPage = new Intent(BuyCrafted.this,Cart.class);
        historyPage = new Intent(BuyCrafted.this,BoughtItems.class);
        myItems = new Intent(BuyCrafted.this,MyItems.class);
        loginpage = new Intent(BuyCrafted.this,Login.class);
        reservedItems = new Intent(BuyCrafted.this,ReservedItems.class);

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
        Picasso.with(this).load(activeUser.getProfilePicture()).transform(new RoundedTransformation(50, 0)).placeholder(R.drawable.progress_animation).fit().into(navImage);

        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

        //----------------------------------------------------------------
        lstCraft = (ListView) findViewById(R.id.lstCrafted);
        txtEmpty = (TextView) findViewById(R.id.txtEmpty);

        Intent receivedIntent = getIntent();
        receivedBundle = receivedIntent.getExtras();

        databaseReference= FirebaseDatabase.getInstance().getReference("Craft").child(receivedBundle.get("Category").toString());

        mProgressDialog = new ProgressDialog(this);

        getCraftDataFromFirebase();

        customAdapter = new custom_craftlist(this, craftArray);
        lstCraft.setAdapter(customAdapter);
        lstCraft.setOnScrollListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

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

        popupBubble = (PopupBubble) findViewById(R.id.popup_bubble);

        /*popupBubble.setPopupBubbleListener(new PopupBubble.PopupBubbleClickListener() {
            @Override
            public void bubbleClicked(Context context) {

                int duration = 500;  //miliseconds
                int offset = 0;      //fromListTop

                lstCraft.smoothScrollToPositionFromTop(0,offset,duration);

                craftArray.clear();
                customAdapter.notifyDataSetChanged();

                getCraftDataFromFirebase();
                //popup_bubble is clicked

                popupBubble.hide();
            }
        });*/

        popupBubble.hide();

        searchTxt = (SearchView) findViewById(R.id.searchTxt);
        searchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }
        });



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
    public void getCraftDataFromFirebase() {


        databaseReference.orderByChild("reverseDate").limitToFirst(3).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (craftArray.size() != 0) {
                    popupBubble.show();

                } else {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            boolean found = false;
                            oldestPostId = postSnapShot.getKey();

                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            for (Craft craftItem : craftArray) {
                                if (!TextUtils.isEmpty(craftItem.getCraftID()) && !TextUtils.isEmpty(oldestPostId)) {
                                    if (craftItem.getCraftID().equals(oldestPostId)) {
                                        found = true;
                                    }
                                }
                            }

                            if (!found) {

                                Craft craft = postSnapShot.getValue(Craft.class);

                                if (craft.getCraftCategory().equals(receivedBundle.get("Category"))) {
                                    if ( craft.getflag().equals("0") ) {
                                        craft.setCraftID(postSnapShot.getKey().toString());
                                        craftArray.add(craft);
                                        customAdapter.notifyDataSetChanged();

                                    }
                                }
                            }
                        }
                        mProgressDialog.dismiss();
                    }
                    if(craftArray.size() == 0){
                        lstCraft.setVisibility(View.INVISIBLE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        lstCraft.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.totalItem = totalItemCount;
    }

    private void isScrollCompleted() {
        if (totalItem - currentFirstVisibleItem == currentVisibleItemCount && this.currentScrollState == SCROLL_STATE_IDLE) {
            if (totalItem >= counter) {
                counter += 3;
                databaseReference.orderByChild("reverseDate").limitToFirst(counter).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        boolean found = false;

                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                            if (!oldestPostId.equals(postSnapShot.getKey())) {
                                oldestPostId = postSnapShot.getKey();

                                for (Craft craft : craftArray) {
                                    if (craft.getCraftID().equals(oldestPostId)) {
                                        found = true;
                                    }
                                }

                                if (found) {
                                    continue;
                                }

                                mProgressDialog.setMessage("Loading...");
                                mProgressDialog.show();

                                Craft craft = postSnapShot.getValue(Craft.class);

                                if (craft.getCraftCategory().equals(receivedBundle.get("Category"))) {
                                    if ( craft.getflag().equals("0") ) {
                                        craft.setCraftID(postSnapShot.getKey().toString());
                                        craftArray.add(craft);
                                        customAdapter.notifyDataSetChanged();

                                    }
                                }
                            }
                        }
                        mProgressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
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
}
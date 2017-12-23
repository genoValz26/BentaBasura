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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Trash;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.RoundedTransformation;
import com.android.bentabasura.benta_basura.View_Holders.custom_trashlist;
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


public class BuyRaw extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ListView.OnScrollListener, AdapterView.OnItemSelectedListener {

    private Intent reservedItems,profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems,loginpage;
    private DrawerLayout drawer;
    FirebaseAuth firebaseAuth;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    private ListView lstRecycle;
    ProgressDialog mProgressDialog;
    private custom_trashlist customAdapter;
    private int counter = 3;

    DatabaseReference databaseReference;
    ArrayList<Trash> trashArray =new ArrayList<>();

    TextView navFullName, navEmail, txtEmpty;
    ImageView navImage;
    ActiveUser activeUser;
    SearchView filterTxt;
    Button filterBtn;

    private String oldestPostId;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private int currentFirstVisibleItem;
    private int totalItem;
    private LinearLayout lBelow;

    Bundle receivedBundle;
    private GoogleApiClient mGoogleApiClient;
    private Spinner spnFilter;
    private String selectedFilter;
    PopupBubble popupBubble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_raw);

        //set persist to true
        Login.setPersist(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePage = new Intent(BuyRaw.this, MyProfile.class);
        buyCrafted = new Intent(BuyRaw.this, Craft_Categories.class);
        buyRaw = new Intent(BuyRaw.this, Categories.class);
        sellCrafted = new Intent(BuyRaw.this, SellCrafted.class);
        sellRaw = new Intent(BuyRaw.this, SellRaw.class);
        notificationsPage = new Intent(BuyRaw.this, Notifications.class);
        homePage = new Intent(BuyRaw.this,Home.class);
        cartPage = new Intent(BuyRaw.this,Cart.class);
        historyPage = new Intent(BuyRaw.this,BoughtItems.class);
        myItems = new Intent(BuyRaw.this,MyItems.class);
        loginpage = new Intent(BuyRaw.this,Login.class);
        reservedItems = new Intent(BuyRaw.this,ReservedItems.class);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //---------------------------------------------------------
        spnFilter = (Spinner) findViewById(R.id.spnr);

        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,R.array.filter_array,android.R.layout.simple_spinner_dropdown_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilter.setAdapter(adapterCategory);
        spnFilter.setOnItemSelectedListener(this);

        //----------------------------------------------------------
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
        lstRecycle = (ListView) findViewById(R.id.lstRecycle);
        txtEmpty = (TextView) findViewById(R.id.txtEmpty);

        Intent receivedIntent = getIntent();
        receivedBundle = receivedIntent.getExtras();

        databaseReference= FirebaseDatabase.getInstance().getReference("Trash").child(receivedBundle.get("Category").toString());

        mProgressDialog = new ProgressDialog(this);

        getTrashDataFromFirebase();

        customAdapter = new custom_trashlist(this, trashArray);
        lstRecycle.setAdapter(customAdapter);
        lstRecycle.setOnScrollListener(this);

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

        popupBubble.setPopupBubbleListener(new PopupBubble.PopupBubbleClickListener() {
            @Override
            public void bubbleClicked(Context context) {

                int duration = 500;  //miliseconds
                int offset = 0;      //fromListTop

                lstRecycle.smoothScrollToPositionFromTop(0,offset,duration);

                trashArray.clear();
                customAdapter.notifyDataSetChanged();

                getTrashDataFromFirebase();
                //popup_bubble is clicked

                popupBubble.hide();
            }
        });

        popupBubble.hide();

        filterTxt = (SearchView) findViewById(R.id.filterTxt);
        filterTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //customAdapter.getFilter().filter(s);
                return false;
            }
        });
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
    public void getTrashDataFromFirebase() {

        databaseReference.orderByChild("reverseDate").limitToFirst(3).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (trashArray.size() != 0) {
                    popupBubble.show();

                } else {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            boolean found = false;
                            oldestPostId = postSnapShot.getKey();

                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            for (Trash trashItem : trashArray) {
                                if (!TextUtils.isEmpty(trashItem.getTrashId()) && !TextUtils.isEmpty(oldestPostId)) {
                                    if (trashItem.getTrashId().equals(oldestPostId)) {
                                        found = true;
                                    }
                                }
                            }

                            if (!found) {

                              Trash trash = postSnapShot.getValue(Trash.class);

                                if (trash.getTrashCategory().equals(receivedBundle.get("Category"))) {
                                    if ( trash.getflag().equals("0") ) {
                                        trash.setTrashId(postSnapShot.getKey().toString());
                                        trashArray.add(trash);
                                        customAdapter.notifyDataSetChanged();

                                    }
                                }
                            }
                        }
                        mProgressDialog.dismiss();
                    }
                    if(trashArray.size() == 0){
                        lstRecycle.setVisibility(View.INVISIBLE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        lstRecycle.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    @Override
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
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            boolean found = false;

                            if (!oldestPostId.equals(postSnapShot.getKey())) {
                                oldestPostId = postSnapShot.getKey();

                                for (Trash trash : trashArray) {
                                    if (trash.getTrashId().equals(oldestPostId)) {
                                        found = true;
                                    }
                                }

                                if (found) {
                                    continue;
                                }
                                Log.i("Fetching data","Loading...");

                                Trash trash = postSnapShot.getValue(Trash.class);
                                if (trash.getTrashCategory().equals(receivedBundle.get("Category"))) {
                                    if ( trash.getflag().equals("0") ) {
                                        trash.setTrashId(postSnapShot.getKey().toString());
                                        trashArray.add(trash);
                                    }
                                }
                                customAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner theSpinner = (Spinner) adapterView;
        switch(theSpinner.getId()){
            case R.id.spnr:
                selectedFilter = theSpinner.getItemAtPosition(i).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
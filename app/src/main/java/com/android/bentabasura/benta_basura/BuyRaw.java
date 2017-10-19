package com.android.bentabasura.benta_basura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class BuyRaw extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ListView.OnScrollListener {

    private Intent profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems,loginpage;
    private DrawerLayout drawer;
	FirebaseAuth firebaseAuth;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    private ListView lstRecycle;
    ProgressDialog mProgressDialog;
    private custom_trashlist customAdapter;

    DatabaseReference databaseReference;
    ArrayList<Trash> trashArray =new ArrayList<>();

    TextView navFullName, navEmail;
    ImageView navImage;
    ActiveUser activeUser;

    private String oldestPostId;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private int currentFirstVisibleItem;
    private int totalItem;
    private LinearLayout lBelow;

    Bundle receivedBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_raw);
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
        historyPage = new Intent(BuyRaw.this,History.class);
        myItems = new Intent(BuyRaw.this,MyItems.class);
        loginpage = new Intent(BuyRaw.this,Login.class);

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
        Picasso.with(this).load(activeUser.getProfilePicture()).transform(new RoundedTransformation(150, 20)).into(navImage);

        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

        //----------------------------------------------------------------
        lstRecycle = (ListView) findViewById(R.id.lstRecycle);

        Intent receivedIntent = getIntent();
        receivedBundle = receivedIntent.getExtras();

        databaseReference= FirebaseDatabase.getInstance().getReference("Trash").child(receivedBundle.get("Category").toString());

        mProgressDialog = new ProgressDialog(this);

        getTrashDataFromFirebase();

        customAdapter = new custom_trashlist(this, trashArray);
        lstRecycle.setAdapter(customAdapter);
        lstRecycle.setOnScrollListener(this);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.notifications:
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
        firebaseAuth.signOut();
        startActivity(loginpage);

    }
    public void getTrashDataFromFirebase() {

        databaseReference.limitToFirst(3).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {

                        boolean found = true;
                        oldestPostId = postSnapShot.getKey();

                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();

                        Trash trash = postSnapShot.getValue(Trash.class);

                        if(trash.getTrashCategory().equals(receivedBundle.get("Category")))
                        {
                            if (trash.getSold().equals("0"))
                            {

                                for(Trash itemTrash : trashArray)
                                {
                                    if (itemTrash.getTrashId().equals(oldestPostId))
                                    {
                                        found = true;
                                    }
                                }

                                if(!found) {
                                    trash.setTrashId(postSnapShot.getKey().toString());
                                    trashArray.add(trash);
                                    customAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                    }
                    mProgressDialog.dismiss();
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
        if (totalItem - currentFirstVisibleItem == currentVisibleItemCount && this.currentScrollState == SCROLL_STATE_IDLE)
        {
            databaseReference.orderByKey().startAt(oldestPostId).limitToFirst(3).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                        if ( !oldestPostId.equals(postSnapShot.getKey()) )
                        {
                            oldestPostId = postSnapShot.getKey();

                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            Trash trash = postSnapShot.getValue(Trash.class);
                            if(trash.getTrashCategory().equals(receivedBundle.get("Category"))) {
                                if (trash.getSold().equals("0")) {
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

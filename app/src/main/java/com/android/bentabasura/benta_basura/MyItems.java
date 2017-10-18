package com.android.bentabasura.benta_basura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyItems extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private Intent profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    ListView lstMyTrash, lstMyCraft;
    ProgressDialog mProgressDialog;
    private String oldestPostId;

    ActiveUser activeUser;

    DatabaseReference databaseReferenceCraft, databaseReferenceTrash;

    private custom_trashlist customTrashAdapter;
    private custom_craftlist customCraftAdapter;

    ArrayList<Trash> trashArray = new ArrayList<>();
    ArrayList<Craft> craftArray = new ArrayList<>();


    List<String> trashCategory = Arrays.asList("Plastic", "Paper", "Metal", "Wood");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePage = new Intent(MyItems.this, MyProfile.class);
        buyCrafted = new Intent(MyItems.this, Craft_Categories.class);
        buyRaw = new Intent(MyItems.this, Categories.class);
        sellCrafted = new Intent(MyItems.this, SellCrafted.class);
        sellRaw = new Intent(MyItems.this, SellRaw.class);
        notificationsPage = new Intent(MyItems.this, Notifications.class);
        homePage = new Intent(MyItems.this,Home.class);
        cartPage = new Intent(MyItems.this,Cart.class);
        historyPage = new Intent(MyItems.this,History.class);
        myItems = new Intent(MyItems.this,MyItems.class);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

        mProgressDialog = new ProgressDialog(this);

        lstMyTrash = (ListView) findViewById(R.id.myTrash);
        lstMyCraft = (ListView) findViewById(R.id.myCraft);

        databaseReferenceTrash  = FirebaseDatabase.getInstance().getReference("Trash");
        databaseReferenceCraft  = FirebaseDatabase.getInstance().getReference("Craft");

        activeUser = ActiveUser.getInstance();

        customTrashAdapter = new custom_trashlist(this, trashArray);
        customCraftAdapter = new custom_craftlist(this, craftArray);

        lstMyTrash.setAdapter(customTrashAdapter);
        lstMyCraft.setAdapter(customCraftAdapter);

        //LoadCraft
        getCraftDataFromFirebase();

        //LoadTrash
        getTrashDataFromFirebase();
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
            case R.id.cart:
                startActivity(cartPage);
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
        }
        return true;
    }

    private void getCraftDataFromFirebase()
    {
        for(final String trashCat: trashCategory)
        {
            databaseReferenceTrash.child(trashCat.toString()).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren())
                        {
                            oldestPostId = postSnapShot.getKey();

                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();

                            Trash trash = postSnapShot.getValue(Trash.class);

                            if (trash.getTrashCategory().equals(trashCat.toString()))
                            {
                                if (trash.getSold().equals("0"))
                                {
                                    for (Trash itemTrash : trashArray)
                                    {
                                        if (itemTrash.getTrashId().equals(oldestPostId)) {
                                            continue;
                                        }
                                    }

                                    trash.setTrashId(postSnapShot.getKey().toString());
                                    trashArray.add(trash);
                                    customTrashAdapter.notifyDataSetChanged();

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

    }

    private void getTrashDataFromFirebase() {
    }
}
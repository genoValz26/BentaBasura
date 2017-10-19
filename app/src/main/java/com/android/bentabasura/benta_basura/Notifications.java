package com.android.bentabasura.benta_basura;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class Notifications extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Intent profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    ActiveUser activeUser;
    TextView navFullName, navEmail;
    ImageView navImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
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
        historyPage = new Intent(Notifications.this,History.class);
        myItems = new Intent(Notifications.this,MyItems.class);

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
        }
        return true;
    }
}

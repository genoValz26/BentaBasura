package com.android.bentabasura.benta_basura;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

/**
 * Created by gd185082 on 10/12/2017.
 */

public class Categories extends AppCompatActivity implements View.OnClickListener,
            NavigationView.OnNavigationItemSelectedListener{

    Button btnPaper, btnPlastic, btnWood, btnMetal;
    Intent rawIntent;
    private Intent profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw, notificationsPage, homePage, cartPage, historyPage, myItems, loginpage;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    TextView navFullName, navEmail;
    ImageView navImage;
    FirebaseAuth firebaseAuth;
    ActiveUser activeUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_trash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnPaper = (Button) findViewById(R.id.btnPapers);
        btnPlastic = (Button) findViewById(R.id.btnPlastic);
        btnWood = (Button) findViewById(R.id.btnWood);
        btnMetal = (Button) findViewById(R.id.btnMetal);

        btnPaper.setOnClickListener(this);
        btnPlastic.setOnClickListener(this);
        btnWood.setOnClickListener(this);
        btnMetal.setOnClickListener(this);

        rawIntent = new Intent(Categories.this, BuyRaw.class);
        profilePage = new Intent(Categories.this, MyProfile.class);
        buyCrafted = new Intent(Categories.this, Craft_Categories.class);
        buyRaw = new Intent(Categories.this, Categories.class);
        sellCrafted = new Intent(Categories.this, SellCrafted.class);
        sellRaw = new Intent(Categories.this, SellRaw.class);
        notificationsPage = new Intent(Categories.this, Notifications.class);
        homePage = new Intent(Categories.this, Home.class);
        cartPage = new Intent(Categories.this, Cart.class);
        historyPage = new Intent(Categories.this, BoughtItems.class);
        myItems = new Intent(Categories.this, MyItems.class);
        loginpage = new Intent(Categories.this, Login.class);

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
        Picasso.with(this).load(activeUser.getProfilePicture()).transform(new RoundedTransformation(50, 0)).fit().into(navImage);

        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPapers: {
                rawIntent.putExtra("Category", "Paper");
                startActivity(rawIntent);
                break;
            }
            case R.id.btnPlastic: {
                rawIntent.putExtra("Category", "Plastic");
                startActivity(rawIntent);
                break;
            }
            case R.id.btnWood: {
                rawIntent.putExtra("Category", "Wood");
                startActivity(rawIntent);
                break;
            }
            case R.id.btnMetal: {
                rawIntent.putExtra("Category", "Metal");
                startActivity(rawIntent);
                break;
            }
        }
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
            switch (item.getItemId()) {
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
                case R.id.buy:
                    if (navMenu.findItem(R.id.buy).getTitle().equals("Buy                                        +")) {
                        navMenu.findItem(R.id.buy_crafted).setVisible(true);
                        navMenu.findItem(R.id.buy_raw).setVisible(true);
                        navMenu.findItem(R.id.buy).setTitle("Buy                                        -");
                    } else {
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
                    if (navMenu.findItem(R.id.sell).getTitle().equals("Sell                                        +")) {
                        navMenu.findItem(R.id.sell_crafted).setVisible(true);
                        navMenu.findItem(R.id.sell_raw).setVisible(true);
                        navMenu.findItem(R.id.sell).setTitle("Sell                                        -");
                    } else {
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
        buildDialog(this).show();
        return;

    }
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("BentaBasura");
        builder.setMessage("Thank you for using BentaBasura!."+"\n"+" Press OK to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(loginpage);
            }
        });

        return builder;
    }
}

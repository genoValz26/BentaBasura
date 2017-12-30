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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.RoundedTransformation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

/**
 * Created by Lowe on 12/4/2017.
 */

public class Admin_Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    private Intent manageUsers,manageNews,manageCraft,manageTrash,manageTips;
    private GoogleApiClient mGoogleApiClient;
    ActiveUser activeUser;
    FirebaseAuth firebaseAuth;
    TextView navFullName, navEmail;
    ImageView navImage;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        activeUser = new ActiveUser().getInstance();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navMenu = navigationView.getMenu();

        View headerView = navigationView.getHeaderView(0);
        navFullName = (TextView) headerView.findViewById(R.id.txtFullNameMenu);
        navEmail = (TextView) headerView.findViewById(R.id.txtEmailMenu);
        navImage = (ImageView) headerView.findViewById(R.id.imageView);

        navFullName.setText(activeUser.getFullname());
        navEmail.setText(activeUser.getEmail());
        Picasso.with(this).load(activeUser.getProfilePicture()).transform(new RoundedTransformation(50, 0)).placeholder(R.drawable.progress_animation).fit().into(navImage);
        navMenu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(this);

        manageUsers = new Intent(this,Admin_ManageUsers.class);
        manageNews = new Intent(this,Admin_ManageNews.class);
        manageTips = new Intent(this,Admin_ManageTips.class);
        manageCraft = new Intent(this,Admin_Craft_Categories.class);
        manageTrash = new Intent(this,Admin_Trash_Categories.class);

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



    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void logout() {

        buildDialog(this).show();
        return;

    }
    public void showMessage(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
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
                startActivity(new Intent(Admin_Navigation.this,Login.class));
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.manage:
                if(navMenu.findItem(R.id.manage).getTitle().equals("Manage                                        +")) {
                    navMenu.findItem(R.id.manage_crafted).setVisible(true);
                    navMenu.findItem(R.id.manage_raw).setVisible(true);
                    navMenu.findItem(R.id.manage_users).setVisible(true);
                    navMenu.findItem(R.id.manage_news).setVisible(true);
                    navMenu.findItem(R.id.manage_tips).setVisible(true);
                    navMenu.findItem(R.id.manage).setTitle("Manage                                        -");
                }
                else{
                    navMenu.findItem(R.id.manage_crafted).setVisible(false);
                    navMenu.findItem(R.id.manage_raw).setVisible(false);
                    navMenu.findItem(R.id.manage_users).setVisible(false);
                    navMenu.findItem(R.id.manage_news).setVisible(false);
                    navMenu.findItem(R.id.manage_tips).setVisible(false);
                    navMenu.findItem(R.id.manage).setTitle("Manage                                        +");
                }
                break;
            case R.id.manage_users:
                startActivity(manageUsers);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.manage_crafted:
                startActivity(manageCraft);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.manage_raw:
                startActivity(manageTrash);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.manage_news:
                startActivity(manageNews);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.manage_tips:
                startActivity(manageTips);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                logout();
                break;
        }
        return true;
    }
}

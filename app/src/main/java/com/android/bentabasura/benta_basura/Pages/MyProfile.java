package com.android.bentabasura.benta_basura.Pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.BlurTransformation;
import com.android.bentabasura.benta_basura.Utils.RoundedTransformation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class MyProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    private Intent reservedItems,profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,loginpage,editprofile,myItems;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    String userid,fname,lname,email;
    public static final String TAG = "MyProfile";
    ActiveUser activeUser;
    private TextView txtFullname, txtEmail, txtUserType, txtGender;
    TextView navFullName, navEmail;
    ImageView navImage,n;
    ImageView bigProfile, smallProfile;
    Uri imageUri;
    FloatingActionButton editProfilebtn;
    ProgressDialog progressDialog;
    private static final int Gallery_Intent = 100;
    public static final String STORAGE_PATH="Profile/";

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //set persist to true
        Login.setPersist(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePage = new Intent(MyProfile.this, MyProfile.class);
        buyCrafted = new Intent(MyProfile.this, Craft_Categories.class);
        buyRaw = new Intent(MyProfile.this, Categories.class);
        sellCrafted = new Intent(MyProfile.this, SellCrafted.class);
        sellRaw = new Intent(MyProfile.this, SellRaw.class);
        notificationsPage = new Intent(MyProfile.this, Notifications.class);
        homePage = new Intent(MyProfile.this,Home.class);
        cartPage = new Intent(MyProfile.this,Cart.class);
        historyPage = new Intent(MyProfile.this,BoughtItems.class);
        loginpage = new Intent(MyProfile.this,Login.class);
        editprofile = new Intent(MyProfile.this,MyProfile_Edit.class);
        myItems = new Intent(MyProfile.this,MyItems.class);
        reservedItems = new Intent(MyProfile.this,ReservedItems.class);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
       //--------------------------------------------------
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();
        userid = user.getUid();
        activeUser = ActiveUser.getInstance();
      //--------------------------------------------------

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

        editProfilebtn = (FloatingActionButton) findViewById(R.id.fab);
        editProfilebtn.setOnClickListener(this);

        //-----------------------------------------------------------
        progressDialog = new ProgressDialog(this);

        txtFullname = (TextView) findViewById(R.id.txtFullname);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtUserType = (TextView) findViewById(R.id.txtUserType);
        txtGender = (TextView) findViewById(R.id.txtGender);
        bigProfile = (ImageView) findViewById(R.id.bigProfile);
        smallProfile = (ImageView) findViewById(R.id.smallProfile);

        txtFullname.setText(activeUser.getFullname());
        txtEmail.setText(activeUser.getEmail());
        txtGender.setText(activeUser.getContact_number());
        txtUserType.setText(activeUser.getAddress());
        Picasso.with(this).load(activeUser.getProfilePicture())
                .transform(new RoundedTransformation(150, 20)).placeholder(R.drawable.progress_animation)
                .fit()
                .centerCrop().into(smallProfile);

        Picasso.with(this).load(activeUser.getProfilePicture()).transform(new BlurTransformation(this)).fit().into(bigProfile);
        //-----------------------------------------------------------

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

    public void showMessage(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View view) {

        //showUpdateDialog(userid); //Using Custom Alert Dialog
        startActivity(editprofile); //Using Custom Layout Dialog

    }

    //Custom Alert Dialog
    public void showUpdateDialog(final String userid){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_edit_profile,null);
        dialogBuilder.setView(dialogView);

        final EditText editAddress = (EditText) dialogView.findViewById(R.id.editAddress);
        final EditText editContact = (EditText)  dialogView.findViewById(R.id.editContact);
        final EditText editUsername = (EditText)  dialogView.findViewById(R.id.editFullname);
        final ImageView profileImageView = (ImageView) dialogView.findViewById(R.id.profileImageView);
        final Button updatebtn = (Button)  dialogView.findViewById(R.id.updatebtn);
        dialogBuilder.setTitle("Edit Profile");

        final AlertDialog  alertDialog = dialogBuilder.create();
        alertDialog.show();
        activeUser = ActiveUser.getInstance();
        editAddress.setText(activeUser.getAddress().toString());
        editContact.setText(activeUser.getContact_number().toString());
        editUsername.setText(activeUser.getUserName().toString());
        Picasso.with(this).load(activeUser.getProfilePicture()).placeholder(R.drawable.progress_animation)
                .fit().into(profileImageView);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //updateProfile(userid, editUsername.getText().toString(), editContact.getText().toString(), editAddress.getText().toString());
            }
        });

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
}

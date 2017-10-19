package com.android.bentabasura.benta_basura;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by reymond on 15/10/2017.
 */

public class MyProfile_Edit extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private Intent profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;

    EditText editUsername, editAddress, editContact,editfname,editlname;
    Button updatebtn,galleybtn,takephotobtn,savebtn;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    String userid;
    ImageView profileImageView;

    Uri imageUri;
    StorageReference storageReference;
    private ImageView imageView;
    private static final int Gallery_Intent = 100;
    public static final String STORAGE_PATH="Profile/";

    TextView navFullName, navEmail;
    ImageView navImage;
    ActiveUser activeUser;

  ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePage = new Intent(MyProfile_Edit.this, MyProfile.class);
        buyCrafted = new Intent(MyProfile_Edit.this, Craft_Categories.class);
        buyRaw = new Intent(MyProfile_Edit.this, Categories.class);
        sellCrafted = new Intent(MyProfile_Edit.this, SellCrafted.class);
        sellRaw = new Intent(MyProfile_Edit.this, SellRaw.class);
        notificationsPage = new Intent(MyProfile_Edit.this, Notifications.class);
        homePage = new Intent(MyProfile_Edit.this,Home.class);
        cartPage = new Intent(MyProfile_Edit.this,Cart.class);
        historyPage = new Intent(MyProfile_Edit.this,History.class);
        myItems = new Intent(MyProfile_Edit.this,MyItems.class);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

        editUsername = (EditText) findViewById(R.id.editUsername);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editContact = (EditText) findViewById(R.id.editContact);
        editfname = (EditText) findViewById(R.id.editfname);
        editlname = (EditText) findViewById(R.id.editlname);
        updatebtn = (Button) findViewById(R.id.updatebtn);
        updatebtn.setOnClickListener(this);
        galleybtn = (Button) findViewById(R.id.gallerybtn);
        galleybtn.setOnClickListener(this);
        takephotobtn = (Button) findViewById(R.id.takephotobtn);
        takephotobtn.setOnClickListener(this);
        savebtn = (Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener(this);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userid = user.getUid();

        checkFilePermissions();

        progressDialog = new ProgressDialog(this);
        savebtn.setEnabled(false);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updatebtn:
            String strUsername = editUsername.getText().toString().trim();
            String strContact = editContact.getText().toString().trim();
            String strAddress = editAddress.getText().toString().trim();
            String strFname = editfname.getText().toString().trim();
            String strLname = editlname.getText().toString().trim();

                if(TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strAddress = activeUser.getAddress().toString();
                    strContact = activeUser.getContact_number().toString();
                    strFname  = activeUser.getFirstname().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(!TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strAddress = activeUser.getAddress().toString();
                    strContact = activeUser.getContact_number().toString();
                    strFname  = activeUser.getFirstname().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strContact = activeUser.getContact_number().toString();
                    strFname  = activeUser.getFirstname().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strAddress = activeUser.getAddress().toString();
                    strFname  = activeUser.getFirstname().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strAddress = activeUser.getAddress().toString();
                    strContact = activeUser.getContact_number().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strAddress = activeUser.getAddress().toString();
                    strContact = activeUser.getContact_number().toString();
                    strFname  = activeUser.getFirstname().toString();

                }
                else if(!TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strContact = activeUser.getContact_number().toString();
                    strFname  = activeUser.getFirstname().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(!TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strAddress = activeUser.getAddress().toString();
                    strFname  = activeUser.getFirstname().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(!TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strAddress = activeUser.getAddress().toString();
                    strContact = activeUser.getContact_number().toString();
                    strLname = activeUser.getLastname().toString();
                }
                else if(!TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strAddress = activeUser.getAddress().toString();
                    strContact = activeUser.getContact_number().toString();
                    strFname  = activeUser.getFirstname().toString();
                }
                else if(TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strFname  = activeUser.getFirstname().toString();
                    strLname = activeUser.getLastname().toString();
                }
                else if(TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strContact = activeUser.getContact_number().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strContact = activeUser.getContact_number().toString();
                    strFname  = activeUser.getFirstname().toString();
                }
                else if(TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strAddress = activeUser.getAddress().toString();
                    strLname = activeUser.getLastname().toString();
                }
                else if(TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strAddress = activeUser.getAddress().toString();
                    strFname  = activeUser.getFirstname().toString();
                }
                else if(TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strAddress = activeUser.getAddress().toString();
                    strContact = activeUser.getContact_number().toString();
                }
                else if(!TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strFname  = activeUser.getFirstname().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(!TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strAddress = activeUser.getAddress().toString();
                    strLname = activeUser.getLastname().toString();

                }
                else if(!TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strContact = activeUser.getContact_number().toString();;
                    strLname = activeUser.getLastname().toString();

                }
                else if(!TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strContact = activeUser.getContact_number().toString();
                    strFname  = activeUser.getFirstname().toString();
                }
                else if(!TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strAddress = activeUser.getAddress().toString();
                    strFname  = activeUser.getFirstname().toString();
                }
                else if(TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strAddress = activeUser.getAddress().toString();
                }
                else if(TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strContact = activeUser.getContact_number().toString();

                }
                else if(TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strLname = activeUser.getLastname().toString();
                }
                else if(TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                    strFname  = activeUser.getFirstname().toString();

                }
                else if(!TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strAddress = activeUser.getAddress().toString();
                    strContact = activeUser.getContact_number().toString();

                }
                else if(TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strUsername = activeUser.getUserName().toString();
                }
                else if(!TextUtils.isEmpty(strUsername) && TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strAddress = activeUser.getAddress().toString();
                }
                else  if(!TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strContact = activeUser.getContact_number().toString();
                }
                else  if(!TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && TextUtils.isEmpty(strFname ) && !TextUtils.isEmpty(strLname)){
                    strFname  = activeUser.getFirstname().toString();
                }
                else  if(TextUtils.isEmpty(strUsername) && !TextUtils.isEmpty(strAddress) && !TextUtils.isEmpty(strContact) && !TextUtils.isEmpty(strFname ) && TextUtils.isEmpty(strLname)){
                    strLname = activeUser.getLastname().toString();
                }
            updateProfile(userid, strUsername, strContact, strAddress, strFname, strLname);
                break;
            case R.id.gallerybtn:
                onGallery();
                savebtn.setEnabled(true);
                break;
            case R.id.takephotobtn:
                onCamera();
             savebtn.setEnabled(true);
                break;
            case R.id.savebtn:
                StorageReference path = storageReference.child(STORAGE_PATH).child("." + getImageExt(imageUri));
                String strprofile_picture = path.putFile(imageUri).toString();
                updateProfilePicture(userid,strprofile_picture);
                savebtn.setEnabled(false);
                break;
        }

    }
    public boolean updateProfile(String userid, String username, String contact_number, String address, String fname, String lname)
    {
        progressDialog.setMessage("Updating your Information...");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        Users updateUser = new Users(username,activeUser.getEmail().toString(),fname,lname,activeUser.getGender().toString(),activeUser.getProfilePicture(),activeUser.getUserType(),address,contact_number);
        databaseReference.setValue(updateUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Update Successful!");
                progressDialog.dismiss();
            }
        });

        return  true;

    }
    public boolean updateProfilePicture(String struserid,String profile_picture){

        progressDialog.setMessage("Uploading Profile Picture...");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(struserid);
                Users updateUserprofile = new Users(activeUser.getUserName(),activeUser.getEmail().toString(),activeUser.getFirstname(),activeUser.getLastname(),activeUser.getGender().toString(),profile_picture,activeUser.getUserType(),activeUser.getAddress(),activeUser.getContact_number());;
                databaseReference.setValue(updateUserprofile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Upload Success!");
                        progressDialog.dismiss();
                    }
                });


        return true;
    }
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        int CAMERA_REQUEST = 0;

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            if(requestCode == Gallery_Intent)
            {

                //imageView.setImageResource(imageUri);
                InputStream inputStream;
                try
                {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    profileImageView.setImageBitmap(image);

                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            if(requestCode == CAMERA_REQUEST)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profileImageView.setImageBitmap(photo);
            }
        }

    }
    private void onCamera()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,0);
    }
    private void onGallery()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, Gallery_Intent);

    }
    public void checkFilePermissions()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {
            int permissionCheck = MyProfile_Edit.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += MyProfile_Edit.this.checkSelfPermission("Manifest.permission.READ_WRITE_STORAGE");
            if(permissionCheck != 0)
            {
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},permissionCheck);
            }
        }
        else
        {
            Log.d("SellCrafted","checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
    public void showMessage(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
    public String getImageExt(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}

package com.android.bentabasura.benta_basura.Pages;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Craft;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.RoundedTransformation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SellCrafted extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Intent reservedItems,profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems,loginpage;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    private Button uploadbtn,takePhotobtn;
    private ImageButton imageView;

    Uri imageUri;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userid;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    EditText craftName,craftDesc,craftQty,craftPrice,sellerContact,resourcesFrom;
    Button SubmitCraft;
    private GoogleApiClient mGoogleApiClient;
    ProgressDialog progressDialog;

    TextView navFullName, navEmail;
    ImageView navImage, navBack;
    ActiveUser activeUser;

    public static final String STORAGE_PATH="Products/Crafts/";
    String selectedType,selectedCategory;
    private Spinner spnCraftCategory;

    //keep track of camera capture intent
    static final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 3;
    //keep track of gallery intent
    final int PICK_IMAGE_REQUEST = 2;
    //captured picture uri
    private Uri picUri;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_crafted);

        //set persist to true
        Login.setPersist(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePage = new Intent(SellCrafted.this, MyProfile.class);
        buyCrafted = new Intent(SellCrafted.this, Craft_Categories.class);
        buyRaw = new Intent(SellCrafted.this, Categories.class);
        sellCrafted = new Intent(SellCrafted.this, SellCrafted.class);
        sellRaw = new Intent(SellCrafted.this, SellRaw.class);
        notificationsPage = new Intent(SellCrafted.this, Notifications.class);
        homePage = new Intent(SellCrafted.this,Home.class);
        cartPage = new Intent(SellCrafted.this,Cart.class);
        historyPage = new Intent(SellCrafted.this,BoughtItems.class);
        myItems = new Intent(SellCrafted.this,MyItems.class);
        loginpage = new Intent(SellCrafted.this,Login.class);
        reservedItems = new Intent(SellCrafted.this,ReservedItems.class);

        //--------------------------------------------------------------
        spnCraftCategory = (Spinner) findViewById(R.id.spnCraftCategory);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,R.array.craft_category_array,android.R.layout.simple_spinner_dropdown_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCraftCategory.setAdapter(adapterCategory);
        spnCraftCategory.setOnItemSelectedListener(this);
        //--------------------------------------------------------------
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //----------------------------------------------------------------
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        activeUser = ActiveUser.getInstance();
        //----------------------------------------------------------------
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

        imageView = (ImageButton) findViewById(R.id.UploadImageView);
        imageView.setOnClickListener(this);

        navBack = (ImageView) findViewById(R.id.imageView);

        checkFilePermissions();

        //----------------------------------
        craftName = (EditText) findViewById(R.id.craftName);
        craftDesc = (EditText) findViewById(R.id.craftDesc);
        craftPrice = (EditText) findViewById(R.id.craftPrice);
        craftQty = (EditText) findViewById(R.id.craftQty);
        sellerContact = (EditText) findViewById(R.id.sellerContact);
        resourcesFrom = (EditText) findViewById(R.id.resourcesFrom);

        SubmitCraft = (Button) findViewById(R.id.SubmitCraft);
        SubmitCraft.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.UploadImageView:
                showPictureDialog();
                break;
            case R.id.SubmitCraft:
                onUpload();
                break;
        }
    }
    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){
                //get the Uri for the captured image
                Uri uri = picUri;
                //carry out the crop operation
                performCrop();
                //Log.d("picUri", uri.toString());

            }

            else if(requestCode == PICK_IMAGE_REQUEST){
                picUri = data.getData();
                Log.d("uriGallery", picUri.toString());
                performCrop();
            }

            //user is returning from cropping the image
            else if(requestCode == PIC_CROP){
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = (Bitmap) extras.get("data");
                //display the returned cropped image
                navBack.setImageBitmap(thePic);
            }

        }

    }
    private void onCamera()
    {
        try {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
        File imageFile = new File(imageFilePath);
        picUri = Uri.fromFile(imageFile); // convert path to Uri
        takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT,  picUri );
        startActivityForResult(takePictureIntent, CAMERA_CAPTURE);

        } catch(ActivityNotFoundException anfe){
        //display an error message
        String errorMessage = "Whoops - your device doesn't support capturing images!";
        Toast.makeText(SellCrafted.this, errorMessage, Toast.LENGTH_SHORT).show();
     }
    }
    private void onGallery()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);

    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                onGallery();
                                break;
                            case 1:
                                onCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void checkFilePermissions()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {
            int permissionCheck = SellCrafted.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += SellCrafted.this.checkSelfPermission("Manifest.permission.READ_WRITE_STORAGE");
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
    public void onUpload()
    {
        String craft_name = craftName.getText().toString().trim();
        String craft_desc = craftDesc.getText().toString().trim();
        String craft_qty = craftQty.getText().toString().trim();
        String craft_price = craftPrice.getText().toString().trim();
        String craft_seller = sellerContact.getText().toString();

        if(TextUtils.isEmpty(craft_name) && TextUtils.isEmpty(craft_desc) && TextUtils.isEmpty(craft_qty)&& TextUtils.isEmpty(craft_price) && TextUtils.isEmpty(craft_seller))
        {
            craftName.setError("Craft Name is empty!");
            craftDesc.setError("Description is empty!");
            craftPrice.setError("Price is empty!");
            craftQty.setError("Quantity is empty!");
            sellerContact.setError("Meetup Location or Contact is empty!");
            progressDialog.dismiss();
            return;
        }
        else if(TextUtils.isEmpty(craft_name) && !TextUtils.isEmpty(craft_desc) && !TextUtils.isEmpty(craft_qty)&& !TextUtils.isEmpty(craft_price) && !TextUtils.isEmpty(craft_seller))
        {
            craftName.setError("Craft Name is empty!");
            progressDialog.dismiss();
            return;
        }
        else if(!TextUtils.isEmpty(craft_name) && TextUtils.isEmpty(craft_desc) && !TextUtils.isEmpty(craft_qty)&& !TextUtils.isEmpty(craft_price) && !TextUtils.isEmpty(craft_seller))
        {
            craftDesc.setError("Description is empty!");
            progressDialog.dismiss();
            return;
        }
        else if(!TextUtils.isEmpty(craft_name) && !TextUtils.isEmpty(craft_desc) && TextUtils.isEmpty(craft_qty)&& !TextUtils.isEmpty(craft_price) && !TextUtils.isEmpty(craft_seller))
        {
            craftQty.setError("Quantity is empty!");
            progressDialog.dismiss();
            return;
        }
        else if(!TextUtils.isEmpty(craft_name) && !TextUtils.isEmpty(craft_desc) && !TextUtils.isEmpty(craft_qty)&& TextUtils.isEmpty(craft_price) && !TextUtils.isEmpty(craft_seller))
        {
            craftPrice.setError("Price is empty!");
            progressDialog.dismiss();
            return;
        }
        else if(!TextUtils.isEmpty(craft_name) && !TextUtils.isEmpty(craft_desc) && !TextUtils.isEmpty(craft_qty)&& !TextUtils.isEmpty(craft_price) && TextUtils.isEmpty(craft_seller))
        {
            sellerContact.setError("Meetup Location or Contact is empty!");
            progressDialog.dismiss();
            return;
        }

        progressDialog.setMessage("Uploading your Craft...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        user = firebaseAuth.getCurrentUser();
        userid = user.getUid();
        //Check if resourceFrom is null
        if(resourcesFrom.getText().toString().equals("")){
            resourcesFrom.setText("None");
        }

        //Uploading the image on firebase storage
        StorageReference path = storageReference.child(STORAGE_PATH).child(userid).child(craftName.getText().toString() + "." + getImageExt(imageUri));
        path.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Adding the additional information on the real-time db
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy hh:mm a");
                String UploadedDate = sdf.format(currentTime);
                Craft newCraft = new Craft(craftName.getText().toString(),craftQty.getText().toString(),craftPrice.getText().toString(),craftDesc.getText().toString(),selectedCategory,sellerContact.getText().toString(),userid, UploadedDate.toString(),resourcesFrom.getText().toString(),taskSnapshot.getDownloadUrl().toString(),"0", "", (0 - new Date().getTime()));
                String uploadid = databaseReference.push().getKey();
                databaseReference.child("Craft").child(selectedCategory).child(uploadid).setValue(newCraft);
                showMessage("Craft Uploaded Successfully");
                progressDialog.dismiss();
                startActivity(homePage);
            }
        });

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        Spinner theSpinner = (Spinner) parent;
        switch(theSpinner.getId()){
            case R.id.spnCraftCategory:
                selectedCategory = theSpinner.getItemAtPosition(pos).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    private static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            cursor.close();
            return -1;
        }

        cursor.moveToFirst();
        int orientation = cursor.getInt(0);
        cursor.close();
        cursor = null;
        return orientation;
    }

    public static Bitmap rotateBitmap(Context context, Uri photoUri, Bitmap bitmap) {
        int orientation = getOrientation(context, photoUri);
        if (orientation <= 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return bitmap;
    }

    public static boolean setOrientation(Context context, Uri fileUri, int orientation) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.ORIENTATION, orientation);
        int rowsUpdated = context.getContentResolver().update(fileUri, values, null, null);
        return rowsUpdated > 0;
    }
    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }



}

package com.android.bentabasura.benta_basura;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by reymond on 19/10/2017.
 */

public class MyItems_Edit_Craft extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Intent profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems,loginpage;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    private Button uploadbtn,takePhotobtn;
    private ImageView imageView;
    private static final int Gallery_Intent = 100;
    Uri imageUri;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userid;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String date;
    EditText craftName,craftDesc,craftQty,craftPrice,craftCategory,sellerContact,resourcesFrom;
    Button SubmitCraft,soldtbtn,deletebtn;

    ProgressDialog progressDialog;

    TextView navFullName, navEmail;
    ImageView navImage;
    ActiveUser activeUser;
    public static final String STORAGE_PATH="Products/Crafts/";
    String selectedType,selectedCategory;
    private Spinner spnCraftCategory;

    Intent receiveIntent;
    Bundle receivedBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items_edit_craft);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePage = new Intent(MyItems_Edit_Craft.this, MyProfile.class);
        buyCrafted = new Intent(MyItems_Edit_Craft.this, Craft_Categories.class);
        buyRaw = new Intent(MyItems_Edit_Craft.this, Categories.class);
        sellCrafted = new Intent(MyItems_Edit_Craft.this, SellCrafted.class);
        sellRaw = new Intent(MyItems_Edit_Craft.this,SellRaw.class);
        notificationsPage = new Intent(MyItems_Edit_Craft.this, Notifications.class);
        homePage = new Intent(MyItems_Edit_Craft.this,Home.class);
        cartPage = new Intent(MyItems_Edit_Craft.this,Cart.class);
        historyPage = new Intent(MyItems_Edit_Craft.this,BoughtItems.class);
        myItems = new Intent(MyItems_Edit_Craft.this,MyItems.class);
       loginpage = new Intent(MyItems_Edit_Craft.this,Login.class);

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
        Picasso.with(this).load(activeUser.getProfilePicture()).transform(new RoundedTransformation(50, 0)).fit().into(navImage);
        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        //----------------------------------------------------------------
        uploadbtn = (Button)findViewById(R.id.uploadbtn);
        uploadbtn.setOnClickListener(this);
        takePhotobtn = (Button) findViewById(R.id.takePhotobtn);
        takePhotobtn.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.UploadImageView);

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
        deletebtn = (Button) findViewById(R.id.deletebtn);
        deletebtn.setOnClickListener(this);
        soldtbtn = (Button) findViewById(R.id.soldbtn);
        soldtbtn.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        //--------------------------------------------------------------

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
        buildDialog(this).show();
        return;

    }

//Button Codes
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadbtn:
                onGallery();
                break;
            case R.id.takePhotobtn:
                onCamera();
                break;
            case R.id.SubmitCraft:
                onUpload();
                break;
            case R.id.soldbtn:
                break;
            case R.id.deletebtn:
                break;
        }
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
                    imageView.setImageBitmap(image);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            if(requestCode == CAMERA_REQUEST)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
            }
        }
        /*super.onActivityResult(requestCode,resultCode,data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);*/

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

        /*Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Gallery_Intent);*/
    }
    public void checkFilePermissions()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {
            int permissionCheck = MyItems_Edit_Craft.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += MyItems_Edit_Craft.this.checkSelfPermission("Manifest.permission.READ_WRITE_STORAGE");
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
                Craft newCraft = new Craft(craftName.getText().toString(),craftQty.getText().toString(),craftPrice.getText().toString(),craftDesc.getText().toString(),selectedCategory,sellerContact.getText().toString(),userid, UploadedDate.toString(),resourcesFrom.getText().toString(),taskSnapshot.getDownloadUrl().toString(),"0", "");
                String uploadid = databaseReference.getKey();
                databaseReference.child("Craft").child(selectedCategory).child(uploadid).setValue(newCraft);
                showMessage("Craft Updated Successfully");
                progressDialog.dismiss();
                startActivity(myItems);
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

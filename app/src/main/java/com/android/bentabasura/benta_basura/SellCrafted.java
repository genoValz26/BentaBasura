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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class SellCrafted extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Intent profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage;
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
    Button SubmitCraft;

    ProgressDialog progressDialog;

    TextView navFullName, navEmail;
    ActiveUser activeUser;
    public static final String STORAGE_PATH="Products/Crafts/";
    String selectedType,selectedCategory;
    private Spinner spnCraftCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_crafted);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profilePage = new Intent(SellCrafted.this, MyProfile.class);
        buyCrafted = new Intent(SellCrafted.this, BuyCrafted.class);
        buyRaw = new Intent(SellCrafted.this, BuyRaw.class);
        sellCrafted = new Intent(SellCrafted.this, SellCrafted.class);
        sellRaw = new Intent(SellCrafted.this, SellRaw.class);
        notificationsPage = new Intent(SellCrafted.this, Notifications.class);
        homePage = new Intent(SellCrafted.this,Home.class);
        cartPage = new Intent(SellCrafted.this,Cart.class);
        historyPage = new Intent(SellCrafted.this,History.class);

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

        navFullName.setText(activeUser.getFullname());
        navEmail.setText(activeUser.getEmail());
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

        progressDialog = new ProgressDialog(this);
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
                Craft newCraft = new Craft(craftName.getText().toString(),craftQty.getText().toString(),craftPrice.getText().toString(),craftDesc.getText().toString(),selectedCategory,sellerContact.getText().toString(),userid,currentTime.toString(),resourcesFrom.getText().toString(),taskSnapshot.getDownloadUrl().toString(),0);
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
}

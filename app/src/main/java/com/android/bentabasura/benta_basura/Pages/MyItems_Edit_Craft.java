package com.android.bentabasura.benta_basura.Pages;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.android.bentabasura.benta_basura.Models.Notification;
import com.android.bentabasura.benta_basura.Models.Transaction_Craft;
import com.android.bentabasura.benta_basura.Models.Users;
import com.android.bentabasura.benta_basura.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by reymond on 19/10/2017.
 */

public class MyItems_Edit_Craft extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Intent profilePage, buyCrafted, buyRaw, sellCrafted, sellRaw,notificationsPage,homePage,cartPage,historyPage,myItems,loginpage;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    private ImageButton uploadImageView;
    private ImageView imageView;
    private static final int Gallery_Intent = 100;
    Uri imageUri;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userid;
    DatabaseReference databaseReference, databaseReferenceNotif;
    StorageReference storageReference;
    String date;
    EditText craftName,craftDesc,craftQty,craftPrice,craftCategory,sellerContact,resourcesFrom;
    Button SubmitCraft,soldtbtn,deletebtn,btnEditQty;
    String strcraftID,strcraftCategory,strImageUrl,strUploadedDate, strUploadedBy;
    ProgressDialog progressDialog;

    TextView navFullName, navEmail;
    ImageView navImage;
    ActiveUser activeUser;
    public static final String STORAGE_PATH="Products/Crafts/";
    String selectedType,selectedCategory;
    private Spinner spnCraftCategory;
    private GoogleApiClient mGoogleApiClient;
    Intent receiveIntent;
    Bundle receivedBundle;
    Long reverseDate;
    ArrayList<String> arrInterested, arrInterestedNames;
    Map<String,String> mapUser;
    ArrayAdapter<String> dataAdapter;
    SearchableSpinner soldTo;
    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_craft);

        //set persist to true
        Login.setPersist(true);

        spnCraftCategory = (Spinner) findViewById(R.id.spnCraftCategory);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,R.array.craft_category_array,android.R.layout.simple_spinner_dropdown_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCraftCategory.setAdapter(adapterCategory);
        spnCraftCategory.setOnItemSelectedListener(this);

        //----------------------------------------------------------------
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        activeUser = ActiveUser.getInstance();
        //----------------------------------------------------------------

        uploadImageView = (ImageButton) findViewById(R.id.UploadImageView);
        uploadImageView.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);

        checkFilePermissions();

        //----------------------------------
        craftName = (EditText) findViewById(R.id.craftName);
        craftDesc = (EditText) findViewById(R.id.craftDesc);
        craftPrice = (EditText) findViewById(R.id.craftPrice);
        craftQty = (EditText) findViewById(R.id.craftQty);
        sellerContact = (EditText) findViewById(R.id.sellerContact);
        resourcesFrom = (EditText) findViewById(R.id.resourcesFrom);
        spnCraftCategory = (Spinner) findViewById(R.id.spnCraftCategory);

        SubmitCraft = (Button) findViewById(R.id.SubmitCraft);
        SubmitCraft.setOnClickListener(this);
        deletebtn = (Button) findViewById(R.id.deletebtn);
        deletebtn.setOnClickListener(this);
        soldtbtn = (Button) findViewById(R.id.soldbtn);
        soldtbtn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        //--------------------------------------------------------------
        databaseReferenceNotif = FirebaseDatabase.getInstance().getReference().child("Notification");

        Bundle receiveBundle = getIntent().getExtras();
        strcraftID = receiveBundle.get("CraftID").toString();
        strcraftCategory = receiveBundle.get("CraftCategory").toString();
         databaseReference.child("Craft").child(strcraftCategory).child(strcraftID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                craftName.setText(dataSnapshot.child("craftName").getValue().toString());
                strImageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("imageUrl").getValue().toString()).placeholder(R.drawable.progress_animation).fit().into(imageView);
                craftDesc.setText(dataSnapshot.child("craftDescription").getValue().toString());
                craftPrice.setText(dataSnapshot.child("craftPrice").getValue().toString());
                craftQty.setText(dataSnapshot.child("craftQuantity").getValue().toString());
                sellerContact.setText(dataSnapshot.child("sellerContact").getValue().toString());
                resourcesFrom.setText(dataSnapshot.child("resourcesFrom").getValue().toString());
                spnCraftCategory.setSelection(getIndex(spnCraftCategory, dataSnapshot.child("craftCategory").getValue().toString()));
                strUploadedDate = dataSnapshot.child("uploadedDate").getValue().toString();
                reverseDate = Long.parseLong(dataSnapshot.child("reverseDate").getValue().toString());
                strUploadedBy = dataSnapshot.child("uploadedBy").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//Button Codes
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.SubmitCraft:
                onUpload();
                break;
            case R.id.soldbtn:
                showSoldDialog();
                break;
            case R.id.deletebtn:
                buildDeleteDialog(MyItems_Edit_Craft.this).show();
                break;
            case R.id.UploadImageView:
                showPictureDialog();
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
                InputStream inputStream;
                try
                {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    int orientation = getOrientation(getApplicationContext(), imageUri);
                    Bitmap image = rotateBitmap(getApplicationContext(), imageUri, BitmapFactory.decodeStream(inputStream));
                    setOrientation(getApplicationContext(), imageUri, orientation);
                    imageView.setImageBitmap(image);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            if(requestCode == CAMERA_REQUEST)
            {
                Bitmap photo = rotateBitmap(getApplicationContext(), imageUri, ((Bitmap) data.getExtras().get("data")));
                int orientation = getOrientation(getApplicationContext(), imageUri);
                setOrientation(getApplicationContext(), imageUri, orientation);
                imageView.setImageBitmap(photo);
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

        /*Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Gallery_Intent);*/
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

        progressDialog.setMessage("Updating your Craft...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        user = firebaseAuth.getCurrentUser();
        userid = user.getUid();
        if (imageUri == null || Uri.EMPTY.equals(imageUri)) {
            if (resourcesFrom.getText().toString().equals("")) {
                resourcesFrom.setText("None");
            }
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy hh:mm a");
            String UploadedDate = sdf.format(currentTime);

            Craft newCraft = new Craft(craftName.getText().toString(), craftQty.getText().toString(), craftPrice.getText().toString(), craftDesc.getText().toString(), selectedCategory, sellerContact.getText().toString(), userid, UploadedDate.toString(), resourcesFrom.getText().toString(), strImageUrl , "0", "", reverseDate);
            databaseReference.child("Craft").child(strcraftCategory).child(strcraftID).setValue(newCraft);
            showMessage("Craft Updated Successfully");
            progressDialog.dismiss();
            startActivity(new Intent(MyItems_Edit_Craft.this,Home.class));
            return;
        }
        else {
            //Check if resourceFrom is null
            if (resourcesFrom.getText().toString().equals("")) {
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

                    Craft newCraft = new Craft(craftName.getText().toString(), craftQty.getText().toString(), craftPrice.getText().toString(), craftDesc.getText().toString(), selectedCategory, sellerContact.getText().toString(), userid, UploadedDate.toString(), resourcesFrom.getText().toString(), taskSnapshot.getDownloadUrl().toString(), "0", "", reverseDate);
                    databaseReference.child("Craft").child(strcraftCategory).child(strcraftID).setValue(newCraft);
                    showMessage("Craft Updated Successfully");
                    progressDialog.dismiss();
                    startActivity(new Intent(MyItems_Edit_Craft.this,Home.class));
                    return;
                }
            });
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
    public AlertDialog.Builder buildDeleteDialog(Context c) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Delete Craft");
        builder.setMessage("Are you sure you want to Delete your Craft?.");

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                databaseReference.child("Craft").child(strcraftCategory).child(strcraftID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Craft has been removed!");
                        startActivity(new Intent(MyItems_Edit_Craft.this, Home.class));
                        finishAndRemoveTask();
                    }
                });
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
    public void showSoldDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_sold, null);
        dialogBuilder.setView(dialogView);

        soldTo = (SearchableSpinner) dialogView.findViewById(R.id.soldTo);
        final Button submitSold = (Button) dialogView.findViewById(R.id.submitSold);
        final Button cancelSold= (Button) dialogView.findViewById(R.id.cancelSold);

        soldTo.setTitle("Interested Users");
        soldTo.setPositiveButton("OK");
        arrInterested = new ArrayList<>();
        arrInterestedNames = new ArrayList<>();

        mapUser = new HashMap<>();
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrInterestedNames);

        databaseReference.child("Craft").child(strcraftCategory).child(strcraftID).child("Interested").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    arrInterested.add(postSnapShot.getValue().toString());
                }

                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren())
                        {
                            mapUser.put(postSnapShot.getKey().toString(), postSnapShot.child("fullname").getValue().toString());
                        }

                        for(String inter : arrInterested)
                        {
                            String value = mapUser.get(inter);
                            arrInterestedNames.add(value);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        soldTo.setAdapter(dataAdapter);
        soldTo.setOnItemSelectedListener(this);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        submitSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (soldTo.getSelectedItem().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "No selected interested users", Toast.LENGTH_SHORT).show();;
                }
                else {
                    databaseReference.child("Craft").child(strcraftCategory).child(strcraftID).child("sold").setValue("1");

                    for (Map.Entry entry : mapUser.entrySet()) {
                        if (soldTo.getSelectedItem().toString().equals(entry.getValue())) {
                            key = entry.getKey().toString();
                            break; //breaking because its one to one map
                        }
                    }

                    databaseReference.child("Craft").child(strcraftCategory).child(strcraftID).child("soldTo").setValue(key);
                }

                alertDialog.dismiss();

                //Notification
                String notifId = databaseReferenceNotif.push().getKey();
                String location = "Craft" + ":" + strcraftCategory + ":" + strcraftID;
                String message = "Thank you " + mapUser.get(key) + " for purchasing Craft " + craftName.getText().toString() + ". This is one big leap in helping our Environment.";
                String ownerId =  key;
                String profileId = strUploadedBy;

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy hh:mm a");
                String UploadedDate = sdf.format(currentTime);

                Notification newNotif = new Notification();

                newNotif.setNotifDbLink(location);
                newNotif.setNotifMessage(message);
                newNotif.setNotifOwnerId(ownerId);
                newNotif.setNotifBy(profileId);
                newNotif.setNotifRead("0");
                newNotif.setNotifNotify("0");
                newNotif.setNotifByPic(activeUser.getProfilePicture());
                newNotif.setNotifDate(UploadedDate);

                databaseReferenceNotif.child(notifId).setValue(newNotif);

            }

        });

        cancelSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
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

}

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
import android.view.LayoutInflater;
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
import com.android.bentabasura.benta_basura.Models.Transaction_Craft;
import com.android.bentabasura.benta_basura.Models.Transaction_Trash;
import com.android.bentabasura.benta_basura.Models.Trash;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.RoundedTransformation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by reymond on 20/10/2017.
 */

public class MyItems_Edit_Trash extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ImageButton UploadimageView;
    private static final int Gallery_Intent = 100;
    Uri imageUri;
    EditText trashName,trashDesc,trashQty,trashPrice,sellerContact;
    Button SubmitTrash,editbtn,deletebtn,btnEditQty;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String userid;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    TextView navFullName, navEmail;
    ImageView navImage,imageView;
    ActiveUser activeUser;
    public static final String STORAGE_PATH="Products/Trash/";

    private Spinner spnTrashCategory;
    String selectedType,selectedCategory,currentUsername;
    private GoogleApiClient mGoogleApiClient;
    String strTrashId,strTrashCategory,strImageUrl, strUploadedDate;
    long reverseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_trash);

        //set persist to true
        Login.setPersist(true);


        //---------------------------------------------------------
        spnTrashCategory = (Spinner) findViewById(R.id.spnTrashCategory);

        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,R.array.trash_category_array,android.R.layout.simple_spinner_dropdown_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTrashCategory.setAdapter(adapterCategory);
        spnTrashCategory.setOnItemSelectedListener(this);


        //------------------------------------------------------------

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        activeUser = ActiveUser.getInstance();

        //------------------------------------------------------------


        UploadimageView = (ImageButton) findViewById(R.id.UploadImageView);
        UploadimageView.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);
        checkFilePermissions();

        //------------------------------------------------------------
        trashName = (EditText) findViewById(R.id.trashName);
        trashDesc = (EditText) findViewById(R.id.trashDesc);
        trashPrice = (EditText) findViewById(R.id.trashPrice);
        trashQty = (EditText) findViewById(R.id.trashQty);
        trashQty.setEnabled(false);
        sellerContact = (EditText) findViewById(R.id.sellerContact);

        SubmitTrash = (Button) findViewById(R.id.SubmitTrash);
        SubmitTrash.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        editbtn = (Button) findViewById(R.id.soldbtn);
        editbtn.setOnClickListener(this);
        deletebtn = (Button) findViewById(R.id.deletebtn);
        deletebtn.setOnClickListener(this);
        btnEditQty = (Button) findViewById(R.id.btnEditQty);
        btnEditQty.setOnClickListener(this);

        //--------------------------------------------------------------------
        Bundle receiveBundle = getIntent().getExtras();
        strTrashId = receiveBundle.get("TrashID").toString();
        strTrashCategory = receiveBundle.get("TrashCategory").toString();
        databaseReference.child("Trash").child(strTrashCategory).child(strTrashId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trashName.setText(dataSnapshot.child("trashName").getValue().toString());
                strImageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("imageUrl").getValue().toString()).placeholder(R.drawable.progress_animation).fit().into(imageView);
                trashDesc.setText(dataSnapshot.child("trashDescription").getValue().toString());
                trashPrice.setText(dataSnapshot.child("trashPrice").getValue().toString());
                trashQty.setText(dataSnapshot.child("trashQuantity").getValue().toString());
                sellerContact.setText(dataSnapshot.child("sellerContact").getValue().toString());
                spnTrashCategory.setSelection(getIndex(spnTrashCategory, dataSnapshot.child("trashCategory").getValue().toString()));
                strUploadedDate = dataSnapshot.child("uploadedDate").getValue().toString();
                reverseDate = Long.parseLong(dataSnapshot.child("reverseDate").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.UploadImageView:
               showPictureDialog();
                break;
            case R.id.SubmitTrash:
                onUpload();
                break;
            case R.id.deletebtn:
                buildDeleteDialog(MyItems_Edit_Trash.this).show();
                break;
            case R.id.soldbtn:
                showSoldDialog();
                break;
            case R.id.btnEditQty:
                showUpdateQtyDialog();
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

        /*super.onActivityResult(requestCode,resultCode,data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);*/

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
            int permissionCheck = MyItems_Edit_Trash.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += MyItems_Edit_Trash.this.checkSelfPermission("Manifest.permission.READ_WRITE_STORAGE");
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
        String trash_name = trashName.getText().toString().trim();
        String trash_qty = trashQty.getText().toString().trim();
        String trash_price = trashPrice.getText().toString().trim();
        String trash_desc = trashDesc.getText().toString().trim();
        String trash_seller = sellerContact.getText().toString().trim();

        if(TextUtils.isEmpty(trash_name) && TextUtils.isEmpty(trash_qty) && TextUtils.isEmpty(trash_desc) && TextUtils.isEmpty(trash_price) && TextUtils.isEmpty(trash_seller)){
            trashName.setError("Trash Name is empty!");
            trashQty.setError("Quantity is empty!");
            trashDesc.setError("Description is empty");
            trashPrice.setError("Price is empty!");
            sellerContact.setError("Meetup Location or Contact is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (TextUtils.isEmpty(trash_name) && !TextUtils.isEmpty(trash_qty) && !TextUtils.isEmpty(trash_desc) && !TextUtils.isEmpty(trash_price) && !TextUtils.isEmpty(trash_seller)){
            trashName.setError("Trash Name is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(trash_name) && TextUtils.isEmpty(trash_qty) && !TextUtils.isEmpty(trash_desc) && !TextUtils.isEmpty(trash_price) && !TextUtils.isEmpty(trash_seller)){
            trashQty.setError("Quantity is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(trash_name) && !TextUtils.isEmpty(trash_qty) && TextUtils.isEmpty(trash_desc) && !TextUtils.isEmpty(trash_price) && !TextUtils.isEmpty(trash_seller)){
            trashDesc.setError("Description is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(trash_name) && !TextUtils.isEmpty(trash_qty) && !TextUtils.isEmpty(trash_desc) && TextUtils.isEmpty(trash_price) && !TextUtils.isEmpty(trash_seller)){
            trashPrice.setError("Price is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(trash_name) && !TextUtils.isEmpty(trash_qty) && !TextUtils.isEmpty(trash_desc) && !TextUtils.isEmpty(trash_price) && TextUtils.isEmpty(trash_seller)){
            sellerContact.setError("Meetup Location or Contact is empty!");
            progressDialog.dismiss();
            return;
        }
        progressDialog.setMessage("Uploading your Trash...");
        progressDialog.show();

        user = firebaseAuth.getCurrentUser();
        userid = user.getUid();


        if (imageUri == null || Uri.EMPTY.equals(imageUri)) {

            Trash newTrash = new Trash(trashName.getText().toString(), trashQty.getText().toString(), trashPrice.getText().toString(), trashDesc.getText().toString(), selectedCategory, sellerContact.getText().toString(), userid, strUploadedDate, strImageUrl, "0", "", reverseDate);
            databaseReference.child("Trash").child(strTrashCategory).child(strTrashId).setValue(newTrash);
            showMessage("Trash Updated Successfully");
            progressDialog.dismiss();
            startActivity(new Intent(MyItems_Edit_Trash.this, Home.class));
        }
        else {
            StorageReference path = storageReference.child(STORAGE_PATH).child(userid).child(trashName.getText().toString() + "." + getImageExt(imageUri));
            path.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Adding the additional information on the real-time db

                    Trash newTrash = new Trash(trashName.getText().toString(), trashQty.getText().toString(), trashPrice.getText().toString(), trashDesc.getText().toString(), selectedCategory, sellerContact.getText().toString(), userid, strUploadedDate, taskSnapshot.getDownloadUrl().toString(), "0", "", reverseDate);
                    databaseReference.child("Trash").child(strTrashCategory).child(strTrashId).setValue(newTrash);
                    showMessage("Trash Updated Successfully");
                    progressDialog.dismiss();
                    startActivity(new Intent(MyItems_Edit_Trash.this, Home.class));

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
            case R.id.spnTrashCategory:
                selectedCategory = theSpinner.getItemAtPosition(pos).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public AlertDialog.Builder buildDeleteDialog(Context c) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Delete Trash");
        builder.setMessage("Are you sure you want to Delete your Trash?.");

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("Trash").child(strTrashCategory).child(strTrashId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Trash has been removed!");
                        startActivity(new Intent(MyItems_Edit_Trash.this, Home.class));
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
    public void showUpdateQtyDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_edit_quantity, null);
        dialogBuilder.setView(dialogView);

        final EditText editQty = (EditText) dialogView.findViewById(R.id.editQty);
        final Button updateQty = (Button) dialogView.findViewById(R.id.updateQty);
        final Button cancelbtn = (Button) dialogView.findViewById(R.id.cancelbtn);
        final Button plusQty = (Button) dialogView.findViewById(R.id.plusQty);
        final Button minusQty =(Button) dialogView.findViewById(R.id.minusQty);
        dialogBuilder.setTitle("Update Quanity");
        final AlertDialog  alertDialog = dialogBuilder.create();
        alertDialog.show();
        editQty.setEnabled(false);
        editQty.setText(trashQty.getText().toString());
        updateQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Uploading your Trash...");
                progressDialog.show();

                user = firebaseAuth.getCurrentUser();
                userid = user.getUid();

                if (imageUri == null || Uri.EMPTY.equals(imageUri)) {

                    Trash newTrash = new Trash(trashName.getText().toString(), editQty.getText().toString(), trashPrice.getText().toString(), trashDesc.getText().toString(), selectedCategory, sellerContact.getText().toString(), userid, strUploadedDate, strImageUrl, "0", "", reverseDate);
                    databaseReference.child("Trash").child(strTrashCategory).child(strTrashId).setValue(newTrash);
                    showMessage("Quantity Updated Successfully!");
                    progressDialog.dismiss();
                    startActivity(new Intent(MyItems_Edit_Trash.this, Home.class));
                }
                alertDialog.dismiss();

            }
        });
        plusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentqty = Integer.parseInt(editQty.getText().toString());
                int newQty = currentqty + 1;
                editQty.setText(Integer.toString(newQty));
            }
        });
        minusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentqty = Integer.parseInt(editQty.getText().toString());
                int newQty = currentqty - 1;
                editQty.setText(Integer.toString(newQty));
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
    public void showSoldDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_sold, null);
        dialogBuilder.setView(dialogView);

        final EditText soldTo= (EditText) dialogView.findViewById(R.id.soldTo);
        final EditText quantitySold= (EditText) dialogView.findViewById(R.id.quantitySold);
        final Button submitSold = (Button) dialogView.findViewById(R.id.submitSold);

        dialogBuilder.setTitle("Sold To");
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        submitSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                String remainingQty;
                String currentQty = trashQty.getText().toString();
                String qtySold = quantitySold.getText().toString();

                int newQty;
                newQty = Integer.parseInt(currentQty) - Integer.parseInt(qtySold);

                user = firebaseAuth.getCurrentUser();
                userid = user.getUid();

                if(Integer.parseInt(qtySold) > Integer.parseInt(currentQty)){
                    quantitySold.setError("Quantity Sold must not be greater than your Current Quantity");
                    progressDialog.dismiss();
                    return;
                }
                else if(TextUtils.isEmpty(quantitySold.getText().toString())){
                    quantitySold.setError("Quantity Sold is empty!");
                    return;
                }
                else {
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy hh:mm a");
                    String TrasnsactionDate = sdf.format(currentTime);

                    Transaction_Trash soldTrash = new Transaction_Trash(userid, soldTo.getText().toString(), strTrashId, quantitySold.getText().toString(), TrasnsactionDate);
                    databaseReference.child("Transaction_Trash").child(strTrashCategory).child(strTrashId).setValue(soldTrash);

                    if(newQty == 0){
                        remainingQty = "Sold Out";
                    }
                    else {
                        remainingQty = Integer.toString(newQty);
                    }
                    Trash newTrash = new Trash(trashName.getText().toString(), remainingQty, trashPrice.getText().toString(), trashDesc.getText().toString(), selectedCategory, sellerContact.getText().toString(), userid, strUploadedDate, strImageUrl, "0", "", reverseDate);
                    databaseReference.child("Trash").child(strTrashCategory).child(strTrashId).setValue(newTrash);

                    progressDialog.dismiss();
                    showMessage("Product has been sold!");
                    startActivity(new Intent(MyItems_Edit_Trash.this, MyItems.class));
                }
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

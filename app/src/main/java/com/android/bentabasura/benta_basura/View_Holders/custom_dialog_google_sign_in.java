package com.android.bentabasura.benta_basura.View_Holders;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Users;
import com.android.bentabasura.benta_basura.Pages.Home;
import com.android.bentabasura.benta_basura.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by reymond on 26/10/2017.
 */

public class custom_dialog_google_sign_in extends AppCompatActivity implements View.OnClickListener{
    EditText editFullname,editContact,editAddress;
    Button savebtn;
    ImageButton profileImageView;
    ImageView imageView;
    private static final int Gallery_Intent = 100;
    public static final String STORAGE_PATH="Profile/";
    Uri imageUri;
    ProgressDialog progressDialog;
    Bundle receivedBundle;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    ActiveUser activeUser;

    //keep track of camera capture intent
    static final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 3;
    //keep track of gallery intent
    final int PICK_IMAGE_REQUEST = 2;
    //captured picture uri
    private Uri picUri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_google_sign_in);
        checkFilePermissions();

        editFullname = (EditText) findViewById(R.id.editFullname);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editContact = (EditText) findViewById(R.id.editContact);
        savebtn = (Button) findViewById(R.id.savebtn);
        profileImageView = (ImageButton) findViewById(R.id.profileImageView);
        imageView = (ImageView) findViewById(R.id.imageView);

        savebtn.setOnClickListener(this);
        profileImageView.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        receivedBundle = getIntent().getExtras();
        editFullname.setText(receivedBundle.get("googleName").toString());
        activeUser = ActiveUser.getInstance();

    }
        @Override
    public void onClick(View view) {
            switch (view.getId()) {
                case R.id.savebtn:
                    progressDialog.setMessage("Saving your Information...");
                    progressDialog.show();
                    if(TextUtils.isEmpty(editFullname.getText().toString())&&TextUtils.isEmpty(editAddress.getText().toString())&&TextUtils.isEmpty(editContact.getText().toString())){
                        editFullname.setError("Name is Empty!");
                        editAddress.setError("Address is Empty!");
                        editContact.setError("Contact is Empty!");
                        progressDialog.dismiss();
                        return;
                    }
                    else if(TextUtils.isEmpty(editFullname.getText().toString())&&!TextUtils.isEmpty(editAddress.getText().toString())&&!TextUtils.isEmpty(editContact.getText().toString())){
                        editFullname.setError("Name is Empty!");
                        progressDialog.dismiss();
                        return;
                    }
                    else if(!TextUtils.isEmpty(editFullname.getText().toString())&&TextUtils.isEmpty(editAddress.getText().toString())&&TextUtils.isEmpty(editContact.getText().toString())){
                        editAddress.setError("Address is Empty!");
                        editContact.setError("Contact is Empty!");
                        progressDialog.dismiss();
                        return;
                    }
                    else if(TextUtils.isEmpty(editFullname.getText().toString())&&!TextUtils.isEmpty(editAddress.getText().toString())&&TextUtils.isEmpty(editContact.getText().toString())){
                        editFullname.setError("Name is Empty!");
                        editContact.setError("Contact is Empty!");
                        progressDialog.dismiss();
                        return;
                    }
                    else if(TextUtils.isEmpty(editFullname.getText().toString())&&TextUtils.isEmpty(editAddress.getText().toString())&&!TextUtils.isEmpty(editContact.getText().toString())){
                        editAddress.setError("Address is Empty!");
                        editFullname.setError("Name is Empty!");
                        progressDialog.dismiss();
                        return;
                    }
                    else if(!TextUtils.isEmpty(editFullname.getText().toString())&&TextUtils.isEmpty(editAddress.getText().toString())&&!TextUtils.isEmpty(editContact.getText().toString())){
                        editAddress.setError("Address is Empty!");
                        progressDialog.dismiss();
                        return;
                    }
                    else if(!TextUtils.isEmpty(editFullname.getText().toString())&&!TextUtils.isEmpty(editAddress.getText().toString())&&TextUtils.isEmpty(editContact.getText().toString())){
                        editContact.setError("Contact is Empty!");
                        progressDialog.dismiss();
                        return;
                    }
                    else {
                        activeUser.setUserId(receivedBundle.get("googleUserId").toString());
                        if ( picUri == null || Uri.EMPTY.equals(picUri)) {
                            final String defaultImage = "https://firebasestorage.googleapis.com/v0/b/benta-basura.appspot.com/o/Profile%2FbentaDefault.png?alt=media&token=a1dbed57-5061-4491-a2fb-56a8f728abc4";
                            Users updateUser = new Users(editFullname.getText().toString(), receivedBundle.get("googleEmail").toString(), "None", defaultImage, "Member", editAddress.getText().toString(), editContact.getText().toString());
                            databaseReference.child("Users").child(receivedBundle.get("googleUserId").toString()).setValue(updateUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    showMessage("Profile has been saved successfuly!");
                                    activeUser.setFullname(receivedBundle.get("googleName").toString());
                                    activeUser.setProfilePicture(defaultImage);
                                    activeUser.setEmail(receivedBundle.get("googleEmail").toString());
                                    activeUser.setContact_number(editContact.getText().toString());
                                    activeUser.setAddress(editAddress.getText().toString());
                                    progressDialog.dismiss();
                                    startActivity(new Intent(custom_dialog_google_sign_in.this, Home.class));

                                }
                            });
                        } else {
                            StorageReference path = storageReference.child(STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(picUri));
                            path.putFile(picUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Users updateUser = new Users(editFullname.getText().toString(), receivedBundle.get("googleEmail").toString(), "None", taskSnapshot.getDownloadUrl().toString(), "Member", editAddress.getText().toString(), editContact.getText().toString());
                                    databaseReference.child("Users").child(receivedBundle.get("googleUserId").toString()).setValue(updateUser);
                                    progressDialog.dismiss();
                                    showMessage("Profile has been saved successfully!");
                                        activeUser.setFullname(receivedBundle.get("googleName").toString());
                                        activeUser.setProfilePicture(taskSnapshot.getDownloadUrl().toString());
                                        activeUser.setEmail(receivedBundle.get("googleEmail").toString());
                                        activeUser.setContact_number(editContact.getText().toString());
                                        activeUser.setAddress(editAddress.getText().toString());
                                        progressDialog.dismiss();
                                    startActivity(new Intent(custom_dialog_google_sign_in.this, Home.class));

                                }
                            });

                        }
                    }

                    break;
                case R.id.profileImageView:
                    showPictureDialog();
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
                imageView.setImageBitmap(thePic);
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
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
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
            int permissionCheck = custom_dialog_google_sign_in.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += custom_dialog_google_sign_in.this.checkSelfPermission("Manifest.permission.READ_WRITE_STORAGE");
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

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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.Tips;
import com.android.bentabasura.benta_basura.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Admin_AddTips extends Admin_Navigation
{
    protected DrawerLayout mDrawer;
    EditText tipsTitleTxt, tipsContentTxt;
    ImageButton uploadImageView;
    ImageView imageView;
    Button postbtn;
    //keep track of camera capture intent
    static final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 3;
    //keep track of gallery intent
    final int PICK_IMAGE_REQUEST = 2;
    //captured picture uri
    private Uri picUri;

    public static final String STORAGE_PATH="Tips/";

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private FirebaseUser user;
    String userid;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_tips_template, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addView(contentView, 0);

        tipsTitleTxt = (EditText) contentView.findViewById(R.id.tipsTitleTxt);
        tipsContentTxt = (EditText) contentView.findViewById(R.id.tipsContentTxt);
        imageView = (ImageView) contentView.findViewById(R.id.imageView);
        uploadImageView = (ImageButton) contentView.findViewById(R.id.UploadImageView);
        postbtn = (Button) contentView.findViewById(R.id.postbtn);

        uploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTips();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
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
    private void addTips(){
        String title = tipsTitleTxt.getText().toString().trim();
        String content = tipsContentTxt.getText().toString().trim();

        if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content)){
            tipsTitleTxt.setError("Empty!");
            tipsContentTxt.setError("Empty!");
        }
        else if(TextUtils.isEmpty(title)  && !TextUtils.isEmpty(content)){
            tipsTitleTxt.setError("Empty");
        }
        else if(!TextUtils.isEmpty(title) && TextUtils.isEmpty(content)){
            tipsContentTxt.setError("Empty!");
        }
        progressDialog.setMessage("Posting Tips...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        user = firebaseAuth.getCurrentUser();
        userid = user.getUid();

        StorageReference path = storageReference.child(STORAGE_PATH).child(System.currentTimeMillis() + "." + getImageExt(picUri));
        path.putFile(picUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Adding the additional information on the real-time db
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy hh:mm a");
                String UploadedDate = sdf.format(currentTime);
                Tips newTips = new Tips(tipsTitleTxt.getText().toString(),tipsContentTxt.getText().toString(),UploadedDate, taskSnapshot.getDownloadUrl().toString());
                databaseReference.child("Tips").push().setValue(newTips);
                showMessage("Trash Uploaded Successfully");
                progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(),Admin_ManageTips.class));

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

}
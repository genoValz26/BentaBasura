package com.android.bentabasura.benta_basura.View_Holders;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Users;
import com.android.bentabasura.benta_basura.Pages.Home;
import com.android.bentabasura.benta_basura.Pages.MyProfile;
import com.android.bentabasura.benta_basura.Pages.MyProfile_Edit;
import com.android.bentabasura.benta_basura.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by reymond on 26/10/2017.
 */

public class custom_dialog_google_sign_in extends AppCompatActivity implements View.OnClickListener{
    EditText editFullname,editContact,editAddress;
    Button savebtn;
    ImageButton profileImageView;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_google_sign_in);
        checkFilePermissions();

        editFullname = (EditText) findViewById(R.id.editFullname);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editContact = (EditText) findViewById(R.id.editContact);
        savebtn = (Button) findViewById(R.id.savebtn);
        profileImageView = (ImageButton) findViewById(R.id.profileImageView);

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
                        if (imageUri == null || Uri.EMPTY.equals(imageUri)) {
                            final String defaultImage = " https://firebasestorage.googleapis.com/v0/b/benta-basura.appspot.com/o/Profile%2FbentaDefault.png?alt=media&token=a1dbed57-5061-4491-a2fb-56a8f728abc4";
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
                                    startActivity(new Intent(custom_dialog_google_sign_in.this, Home.class));

                                }
                            });
                        } else {
                            StorageReference path = storageReference.child(STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imageUri));
                            path.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
        super.onActivityResult(requestCode, resultCode, data);
        int CAMERA_REQUEST = 0;
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
}

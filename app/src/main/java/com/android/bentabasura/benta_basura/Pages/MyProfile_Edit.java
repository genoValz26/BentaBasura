package com.android.bentabasura.benta_basura.Pages;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Users;
import com.android.bentabasura.benta_basura.R;
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

/**
 * Created by reymond on 15/10/2017.
 */

public class MyProfile_Edit extends AppCompatActivity implements View.OnClickListener{

    EditText editUsername, editAddress, editContact;
    Button updatebtn,savebtn;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userid;
    ImageButton profileImageView;

    Uri imageUri;
    StorageReference storageReference;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference;
    private ImageView imageView;
    private static final int Gallery_Intent = 100;
    public static final String STORAGE_PATH="Profile/";
    public static final String TAG = "MyProfile_Edit";

    TextView navFullName, navEmail;
    ImageView navImage;
    ActiveUser activeUser;

  ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_profile);

        editUsername = (EditText) findViewById(R.id.editFullname);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editContact = (EditText) findViewById(R.id.editContact);
        updatebtn = (Button) findViewById(R.id.updatebtn);
        updatebtn.setOnClickListener(this);


        profileImageView = (ImageButton) findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userid = user.getUid();

        checkFilePermissions();

        progressDialog = new ProgressDialog(this);

        activeUser = ActiveUser.getInstance();
        Picasso.with(this).load(activeUser.getProfilePicture()).placeholder(R.drawable.progress_animation)
                .fit().into(profileImageView);
        editUsername.setText(activeUser.getFullname().toString());
        editContact.setText(activeUser.getContact_number().toString());
        editAddress.setText(activeUser.getAddress().toString());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updatebtn:
            String strUsername = editUsername.getText().toString().trim();
            String strContact = editContact.getText().toString().trim();
            String strAddress = editAddress.getText().toString().trim();

                progressDialog.setMessage("Updating your Information...");
                progressDialog.show();
                if (imageUri == null || Uri.EMPTY.equals(imageUri)) {

                    Users updateUser = new Users(editUsername.getText().toString(), activeUser.getEmail().toString(), activeUser.getGender().toString(), activeUser.getProfilePicture(), activeUser.getUserType(),editAddress.getText().toString(), editContact.getText().toString());
                    databaseReference.child("Users").child(userid).setValue(updateUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            showMessage("Profile has been Updated Successfuly");
                            startActivity(new Intent(MyProfile_Edit.this,MyProfile.class));
                            activeUser.setFullname(editUsername.getText().toString());
                            activeUser.setContact_number(editContact.getText().toString());
                            activeUser.setAddress(editAddress.getText().toString());
                        }
                    });
                }
                else{
                    StorageReference path = storageReference.child(STORAGE_PATH+ System.currentTimeMillis() +"." + getImageExt(imageUri));
                    path.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Users updateUser = new Users(editUsername.getText().toString(), activeUser.getEmail().toString(), activeUser.getGender().toString(), taskSnapshot.getDownloadUrl().toString(), activeUser.getUserType(), editAddress.getText().toString(), editContact.getText().toString());
                            databaseReference.child("Users").child(userid).setValue(updateUser);
                            progressDialog.dismiss();
                            showMessage("Profile has been Updated Successfuly");
                            startActivity(new Intent(MyProfile_Edit.this,MyProfile.class));
                            activeUser.setProfilePicture(taskSnapshot.getDownloadUrl().toString());
                            activeUser.setFullname(editUsername.getText().toString());
                            activeUser.setContact_number(editContact.getText().toString());
                            activeUser.setAddress(editAddress.getText().toString());
                        }
                    });

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

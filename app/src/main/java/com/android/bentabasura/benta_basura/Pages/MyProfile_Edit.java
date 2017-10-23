package com.android.bentabasura.benta_basura.Pages;

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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
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
    Button updatebtn,galleybtn,takephotobtn,savebtn;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userid;
    ImageView profileImageView;

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
        activeUser = ActiveUser.getInstance();
        Picasso.with(this).load(activeUser.getProfilePicture())
                .fit().into(profileImageView);
        editUsername.setText(activeUser.getUserName().toString());
        editContact.setText(activeUser.getContact_number().toString());
        editAddress.setText(activeUser.getAddress().toString());

        firebaseAuth = FirebaseAuth.getInstance();
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

            updateProfile(userid, strUsername, strContact, strAddress);
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
                progressDialog.setMessage("Uploading Profile Picture");
                progressDialog.show();
                StorageReference path = storageReference.child(STORAGE_PATH+ System.currentTimeMillis() +"." + getImageExt(imageUri));
                path.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       updateProfilePicture(userid,taskSnapshot.getDownloadUrl().toString());
                        progressDialog.dismiss();
                    }
                });

                savebtn.setEnabled(false);
                break;
        }

    }
    public boolean updateProfile(String userid, String username, String contact_number, String address)
    {
        progressDialog.setMessage("Updating your Information...");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        Users updateUser = new Users(username,activeUser.getEmail().toString(),"None","None",activeUser.getGender().toString(),activeUser.getProfilePicture(),activeUser.getUserType(),address,contact_number);
        databaseReference.setValue(updateUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Update Successful!");
                progressDialog.dismiss();
            }
        });

        activeUser.setUserName(username);
        activeUser.setContact_number(contact_number);
        activeUser.setAddress(address);

        return  true;

    }
    public boolean updateProfilePicture(String userid,String profile_picture){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                Users updateUserprofile = new Users(activeUser.getUserName(),activeUser.getEmail().toString(),"None","None",activeUser.getGender().toString(),profile_picture,activeUser.getUserType(),activeUser.getAddress(),activeUser.getContact_number());;
                databaseReference.setValue(updateUserprofile);
                showMessage("Upload Success!");
                activeUser.setProfilePicture(profile_picture);
        return true;
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

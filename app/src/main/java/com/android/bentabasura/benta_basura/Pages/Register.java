package com.android.bentabasura.benta_basura.Pages;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bentabasura.benta_basura.Models.Users;
import com.android.bentabasura.benta_basura.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

public class Register extends AppCompatActivity implements OnClickListener {
    Button register,backbtn;
    ImageButton btnImage;
    Intent loginPage;
    EditText txtUser, txtPass, txtFirstName, txtLastName,emailtxt,txtCPass,txtMobileNum,txtAddress;
    RadioButton malebtn,femalebtn;
    Uri imageUri;
    private static final int Gallery_Intent = 100;
    public static final String STORAGE_PATH="Profile/";

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private FirebaseUser user;
    String userid;

    static int counter = 1;
    ProgressBar passwordChecker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set persist to true
        Login.setPersist(true);

        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPass= (EditText) findViewById(R.id.txtPass);
        emailtxt = (EditText) findViewById(R.id.emailtxt);
        txtCPass = (EditText) findViewById(R.id.txtCPass);
        txtMobileNum = (EditText) findViewById(R.id.txtMobileNum);
        txtAddress = (EditText) findViewById(R.id.txtAddress);

        malebtn = (RadioButton) findViewById(R.id.malebtn);
        femalebtn = (RadioButton) findViewById(R.id.femalebtn);

        loginPage = new Intent(Register.this, Login.class);

        register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(this);
        backbtn = (Button) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
        btnImage = (ImageButton) findViewById(R.id.btnImage);
        btnImage.setOnClickListener(this);

        passwordChecker = (ProgressBar)findViewById(R.id.progress_pass);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);


        txtCPass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (txtCPass.getText().toString().length() == 0) {
                    txtCPass.setError("Enter your password..!");
                } else {
                    caculation();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

        });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                registerUser();
                break;
            case R.id.backbtn:
                startActivity(loginPage);
                break;
            case R.id.btnImage:
                showPictureDialog();
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    //Register User
    private void registerUser()
    {
        String password = txtPass.getText().toString().trim();
        String email = emailtxt.getText().toString().trim();
        String username = txtUser.getText().toString().trim();
        String cpassword = txtCPass.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String mobileNum = txtMobileNum.getText().toString().trim();

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(cpassword) && TextUtils.isEmpty(address) && TextUtils.isEmpty(mobileNum)) {

            txtPass.setError("Confirm Password is empty!");
            emailtxt.setError("Email is empty!");
            txtUser.setError("Name is empty!");
            txtCPass.setError("Password is empty!");
            txtAddress.setError("Address is empty!");
            txtMobileNum.setError("Mobile Number is empty!");
            progressDialog.dismiss();
            return;
        }
        else if ( TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtUser.setError("Name is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            emailtxt.setError("Email is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtPass.setError("Confirm Password is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtCPass.setError("Password is empty!");
            progressDialog.dismiss();
            return;
        }
        else if(password.length()<6 && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)){
            txtCPass.setError("Password must at least be 6 characters!");
            progressDialog.dismiss();
            return;
        }
        else if(cpassword.length()<6 && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)){
            txtPass.setError("Password must at least be 6 characters!");
            progressDialog.dismiss();
            return;
        }
        else if(!cpassword.contains(password) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)){
            txtPass.setError("Password did not match!");
            progressDialog.dismiss();
            return;
        }
        else if(!malebtn.isChecked() && !femalebtn.isChecked()){
            showMessage("Please choose a Gender!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtAddress.setError("Address is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && TextUtils.isEmpty(mobileNum)) {
            txtMobileNum.setError("Mobile Number is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && mobileNum.length() < 11) {
            txtMobileNum.setError("Mobile Number must be 11 digits!");
            progressDialog.dismiss();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user = firebaseAuth.getCurrentUser();
                            userid = user.getUid();
                            //Insert Image to Storage

                            if (imageUri == null || Uri.EMPTY.equals(imageUri))
                            {
                                String gender = "";
                                if (malebtn.isChecked()) {
                                    gender = "Male";
                                } else if (femalebtn.isChecked()) {
                                    gender = "Female";
                                }
                                String defaultPicUrl = "https://firebasestorage.googleapis.com/v0/b/benta-basura.appspot.com/o/Profile%2F1508832816411.jpeg?alt=media&token=405f17d5-ecbf-46a2-8e8e-698c6a4a1ca1";
                                //handle followUri
                                Users newUser = new Users(txtUser.getText().toString(), emailtxt.getText().toString(), gender, defaultPicUrl, "Member", txtAddress.getText().toString(), txtMobileNum.getText().toString());
                                databaseReference.child("Users").child(userid).setValue(newUser);
                                sendEmailVerification();
                                firebaseAuth.signOut();
                                progressDialog.dismiss();
                                startActivity(loginPage);
                                finishAndRemoveTask();
                                return;
                            }
                            else {
                                StorageReference path = storageReference.child(STORAGE_PATH+ System.currentTimeMillis() +"." + getImageExt(imageUri));
                                path.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //Checking of Gender
                                        String gender = "";
                                        if (malebtn.isChecked()) {
                                            gender = "Male";
                                        } else if (femalebtn.isChecked()) {
                                            gender = "Female";
                                        }
                                        //Insert to Database
                                        Users newUser = new Users(txtUser.getText().toString(), emailtxt.getText().toString(), gender, taskSnapshot.getDownloadUrl().toString(), "Member", txtAddress.getText().toString(), txtMobileNum.getText().toString());
                                        databaseReference.child("Users").child(userid).setValue(newUser);
                                        sendEmailVerification();
                                        firebaseAuth.signOut();
                                        progressDialog.dismiss();
                                        startActivity(loginPage);
                                        finishAndRemoveTask();
                                        return;
                                    }
                                });
                            }

                        }
                        else{
                            showMessage("Could not Register! Please try again later");
                            progressDialog.dismiss();
                        }
                    }
                });

    }
    public void sendEmailVerification()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        showMessage("Check Email for Verification!");
                        FirebaseAuth.getInstance().signOut();
                        progressDialog.dismiss();;
                    }

                }
            });
        }
    }
    public String getImageExt(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //----------------------------------------------------------------------------------------------

    //TOAST
    public void showMessage(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
    //----------------------------------------------------------------------------------------------

    //Getting an Image

    private void onGallery()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, Gallery_Intent);

    }
    private void onCamera()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,0);
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
                    btnImage.setImageBitmap(image);

                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            if(requestCode == CAMERA_REQUEST)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                btnImage.setImageBitmap(photo);
            }

        }

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

    //----------------------------------------------------------------------------------------------

    protected void caculation() {
        // TODO Auto-generated method stub
        String temp = txtCPass.getText().toString();
        System.out.println(counter + " current password is : " + temp);
        counter = counter + 1;

        int length = 0, uppercase = 0, lowercase = 0, digits = 0, symbols = 0, bonus = 0, requirements = 0;

        int lettersonly = 0, numbersonly = 0, cuc = 0, clc = 0;

        length = temp.length();
        for (int i = 0; i < temp.length(); i++) {
            if (Character.isUpperCase(temp.charAt(i)))
                uppercase++;
            else if (Character.isLowerCase(temp.charAt(i)))
                lowercase++;
            else if (Character.isDigit(temp.charAt(i)))
                digits++;

            symbols = length - uppercase - lowercase - digits;

        }

        for (int j = 1; j < temp.length() - 1; j++) {

            if (Character.isDigit(temp.charAt(j)))
                bonus++;

        }

        for (int k = 0; k < temp.length(); k++) {

            if (Character.isUpperCase(temp.charAt(k))) {
                k++;

                if (k < temp.length()) {

                    if (Character.isUpperCase(temp.charAt(k))) {

                        cuc++;
                        k--;

                    }

                }

            }

        }

        for (int l = 0; l < temp.length(); l++) {

            if (Character.isLowerCase(temp.charAt(l))) {
                l++;

                if (l < temp.length()) {

                    if (Character.isLowerCase(temp.charAt(l))) {

                        clc++;
                        l--;

                    }

                }

            }

        }

        System.out.println("length" + length);
        System.out.println("uppercase" + uppercase);
        System.out.println("lowercase" + lowercase);
        System.out.println("digits" + digits);
        System.out.println("symbols" + symbols);
        System.out.println("bonus" + bonus);
        System.out.println("cuc" + cuc);
        System.out.println("clc" + clc);

        if (length > 7) {
            requirements++;
        }

        if (uppercase > 0) {
            requirements++;
        }

        if (lowercase > 0) {
            requirements++;
        }

        if (digits > 0) {
            requirements++;
        }

        if (symbols > 0) {
            requirements++;
        }

        if (bonus > 0) {
            requirements++;
        }

        if (digits == 0 && symbols == 0) {
            lettersonly = 1;
        }

        if (lowercase == 0 && uppercase == 0 && symbols == 0) {
            numbersonly = 1;
        }

        int Total = (length * 4) + ((length - uppercase) * 2)
                + ((length - lowercase) * 2) + (digits * 4) + (symbols * 6)
                + (bonus * 2) + (requirements * 2) - (lettersonly * length*2)
                - (numbersonly * length*3) - (cuc * 2) - (clc * 2);

        System.out.println("Total" + Total);

        if(Total<30){
            passwordChecker.setProgress(Total-15);
        }

        else if (Total>=40 && Total <50)
        {
            passwordChecker.setProgress(Total-20);
        }

        else if (Total>=56 && Total <70)
        {
            passwordChecker.setProgress(Total-25);
        }

        else if (Total>=76)
        {
            passwordChecker.setProgress(Total-30);
        }
        else{
            passwordChecker.setProgress(Total-20);
        }
    }



}

package com.android.bentabasura.benta_basura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements OnClickListener {
    Button register,backbtn;
    Intent loginPage;
    EditText txtUser, txtPass, txtFirstName, txtLastName,emailtxt,txtCPass,txtMobileNum,txtAddress;
    RadioButton malebtn,femalebtn;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference;
    private FirebaseUser user;
    String userid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPass= (EditText) findViewById(R.id.txtPass);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
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

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);



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
        }
    }
    private void registerUser()
    {
        String fname = txtFirstName.getText().toString().trim();
        String lname = txtLastName.getText().toString().trim();
        String password = txtPass.getText().toString().trim();
        String email = emailtxt.getText().toString().trim();
        String username = txtUser.getText().toString().trim();
        String cpassword = txtCPass.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String mobileNum = txtMobileNum.getText().toString().trim();

        if (TextUtils.isEmpty(fname) && TextUtils.isEmpty(lname) && TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(cpassword) && TextUtils.isEmpty(address) && TextUtils.isEmpty(mobileNum)) {
            txtFirstName.setError("Firstname is empty!");
            txtLastName.setError("Lastname is empty!");
            txtPass.setError("Confirm Password is empty!");
            emailtxt.setError("Email is empty!");
            txtUser.setError("Username is empty!");
            txtCPass.setError("Password is empty!");
            txtAddress.setError("Address is empty!");
            txtMobileNum.setError("Mobile Number is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtFirstName.setError("Firstname is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(fname) && TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtLastName.setError("Lastname is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtUser.setError("Username is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            emailtxt.setError("Email is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtPass.setError("Confirm Password is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtCPass.setError("Password is empty!");
            progressDialog.dismiss();
            return;
        }
        else if(password.length()<6 && !TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)){
            txtCPass.setError("Password must at least be 6 characters!");
            progressDialog.dismiss();
            return;
        }
        else if(cpassword.length()<6 && !TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)){
            txtPass.setError("Password must at least be 6 characters!");
            progressDialog.dismiss();
            return;
        }
        else if(!cpassword.contains(password) && !TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)){
            txtPass.setError("Password did not match!");
            progressDialog.dismiss();
            return;
        }
        else if(!malebtn.isChecked() && !femalebtn.isChecked()){
            showMessage("Please choose a Gender!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && TextUtils.isEmpty(address) && !TextUtils.isEmpty(mobileNum)) {
            txtAddress.setError("Address is empty!");
            progressDialog.dismiss();
            return;
        }
        else if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && TextUtils.isEmpty(mobileNum)) {
            txtMobileNum.setError("Mobile Number is empty!");
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
                            String gender = "";
                            if(malebtn.isChecked()){
                                gender = "Male";
                            }
                            else if(femalebtn.isChecked()){
                                gender = "Female";
                            }
                            user = firebaseAuth.getCurrentUser();
                            userid = user.getUid();
                            Users newUser= new Users(txtUser.getText().toString(),emailtxt.getText().toString(),txtFirstName.getText().toString(),txtLastName.getText().toString(),gender.toString(),"None","Member",txtAddress.getText().toString(),txtMobileNum.getText().toString());
                            databaseReference.child("Users").child(userid).setValue(newUser);

                            sendEmailVerification();

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

    public void showMessage(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }

}

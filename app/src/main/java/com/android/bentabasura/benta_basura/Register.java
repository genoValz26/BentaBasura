package com.android.bentabasura.benta_basura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
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

    static int counter = 1;
    ProgressBar passwordChecker;


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

        passwordChecker = (ProgressBar)findViewById(R.id.progress_pass);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
        else if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(address) && mobileNum.length() < 11) {
            txtMobileNum.setError("Mobile Number must be 11 numbers!");
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
                            Users newUser= new Users(txtUser.getText().toString(),emailtxt.getText().toString(),txtFirstName.getText().toString(),txtLastName.getText().toString(),gender.toString(),"https://firebasestorage.googleapis.com/v0/b/benta-basura.appspot.com/o/Profile%2FbentaDefault.png?alt=media&token=a1dbed57-5061-4491-a2fb-56a8f728abc4","Member",txtAddress.getText().toString(),txtMobileNum.getText().toString());
                            databaseReference.child("Users").child(userid).setValue(newUser);

                            sendEmailVerification();
                            startActivity(loginPage);

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

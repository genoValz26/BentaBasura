package com.android.bentabasura.benta_basura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.android.bentabasura.benta_basura.R.id.loginBtn;

public class Login extends AppCompatActivity implements OnClickListener {
    Button login, register, loginGoogle;
    Intent homePage, registerPage;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    ProgressDialog progressDialog;
    EditText emailTxt, passTxt;
    ActiveUser activeUser;

    String userid;
    public static final String TAG = "Login";
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(loginBtn);
        register = (Button) findViewById(R.id.registerBtn);
        loginGoogle = (Button) findViewById(R.id.loginGoogle);

        emailTxt = (EditText) findViewById(R.id.emailTxt);
        passTxt = (EditText) findViewById(R.id.passTxt);

        homePage = new Intent(Login.this, Home.class);
        registerPage = new Intent(Login.this, Register.class);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        loginGoogle.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        activeUser = ActiveUser.getInstance();

        checkIfUserIsLogin();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        showMessage("Something went wrong. Please try again");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_press));
                userlogin();
                break;
            case R.id.registerBtn:
                register.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn_press));
                startActivity(registerPage);
                break;
            case R.id.loginGoogle:
                sighnInWithGoogle();
                break;
        }
    }

    private void userlogin() {
        String email = emailTxt.getText().toString().trim();
        String password = passTxt.getText().toString();
        if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            emailTxt.setError("Email is empty!");
            progressDialog.dismiss();
            login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn));
            return;
        } else if (!TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            passTxt.setError("Password is empty!");
            progressDialog.dismiss();
            login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn));
            return;
        } else if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            emailTxt.setError("Email is empty!");
            passTxt.setError("Password is empty!");
            progressDialog.dismiss();
            login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn));
            return;
        }

        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkEmailIsVerified();
                            return;
                        } else {
                            showMessage("Invalid Credentials");
                            progressDialog.dismiss();
                            login.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_btn));
                            return;
                        }
                    }
                });
    }

    public void checkEmailIsVerified() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();
        boolean emailVerified = user.isEmailVerified();

        if (!emailVerified) {
            showMessage("Verif your Email First!");
            firebaseAuth.signOut();
            progressDialog.dismiss();

            return;
        } else {
            checkIfUserIsLogin();
            showMessage("Welcome!");
            startActivity(homePage);
            progressDialog.dismiss();
            return;
        }
    }

    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void onStart() {
        super.onStart();
        ;
        firebaseAuth.addAuthStateListener(mAuthListener);

    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void sighnInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = firebaseAuth.getCurrentUser();
                            userid = user.getUid();
                            Users newUser = new Users("None", "Signed in with Google", "None", "None", "None", "None", "None");
                            databaseReference.child("Users").child(userid).setValue(newUser);
                            startActivity(homePage);
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    public void checkIfUserIsLogin()
    {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //If user is already logged-in redirect to homepage

                    progressDialog.setMessage("Already Signed-in.");
                    progressDialog.show();

                    activeUser.setUserId(user.getUid());

                    databaseReference.child("Users").child(activeUser.getUserId()).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    activeUser.setEmail(dataSnapshot.child("email").getValue().toString());
                                    activeUser.setFullname(dataSnapshot.child("firstname").getValue().toString() + " " +
                                            dataSnapshot.child("lastname").getValue().toString());
                                    activeUser.setGender(dataSnapshot.child("gender").getValue().toString());
                                    activeUser.setAge(dataSnapshot.child("age").getValue().toString());

                                    progressDialog.dismiss();
                                    startActivity(homePage);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }
                    );




                }
                else{
                    Log.d(TAG,"onAuthStateChange:signed_out");
                }
            }
        }; //Use to get current user State
    }

}

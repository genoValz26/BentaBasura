package com.android.bentabasura.benta_basura.Pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Page_Adapters.MyIntroPageAdapter;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Services.FirebaseNotificationService;
import com.android.bentabasura.benta_basura.Utils.ConnectionDetector;
import com.android.bentabasura.benta_basura.View_Holders.custom_dialog_google_sign_in;
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
    Button login, loginGoogle;
    TextView link_register;
    Intent homePage, registerPage, adminPage;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    ProgressDialog progressDialog;
    EditText emailTxt, passTxt;
    ActiveUser activeUser;

    String userid, google_email, name,googleuserid;
    public static final String TAG = "Login";
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    ConnectionDetector cd;
    static boolean firebasePersist = false;
    int notVerified = 0;
    public boolean isFirstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getApplicationContext().startService(new Intent(getApplicationContext(), FirebaseNotificationService.class));

        if(!getPersist() && savedInstanceState != null)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            setPersist(true);
        }

        //Add slide first
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Intro App Initialize SharedPreferences
                SharedPreferences getSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

                //  Check either activity or app is open very first time or not and do action
                if (isFirstStart) {

                    //  Launch application introduction screen
                    Intent i = new Intent(Login.this, MyIntroPageAdapter.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getSharedPreferences.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });
        t.start();

        login = (Button) findViewById(loginBtn);
        link_register = (TextView) findViewById(R.id.link_register);
        loginGoogle = (Button) findViewById(R.id.loginGoogle);

        emailTxt = (EditText) findViewById(R.id.emailTxt);
        passTxt = (EditText) findViewById(R.id.passTxt);

        homePage = new Intent(Login.this, Home.class);
        registerPage = new Intent(Login.this, Register.class);
        adminPage = new Intent(Login.this, Admin_ManageUsers.class);

        login.setOnClickListener(this);
        link_register.setOnClickListener(this);
        loginGoogle.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        activeUser = ActiveUser.getInstance();

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

        cd = new ConnectionDetector(this);


        checkIfUserIsLogin(1);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                if (cd.isConnected()) {
                    userlogin();
                } else {
                    buildDialog(Login.this).show();
                    finishAndRemoveTask();
                }
                break;
            case R.id.link_register:
                if (cd.isConnected()) {
                    startActivity(registerPage);
                } else {
                    buildDialog(Login.this).show();
                }
                break;
            case R.id.loginGoogle:
                if (cd.isConnected()) {
                    signInWithGoogle();
                    //buildMyDialog(Login.this).show();
                } else {
                    buildDialog(Login.this).show();
                }
                break;
        }
    }

    private void userlogin() {
        String email = emailTxt.getText().toString().trim();
        String password = passTxt.getText().toString();
        if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            emailTxt.setError("Email is empty!");
            return;
        } else if (!TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            passTxt.setError("Password is empty!");
            return;
        } else if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            emailTxt.setError("Email is empty!");
            passTxt.setError("Password is empty!");
            return;
        }

        progressDialog.setMessage("Signing In...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
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
            notVerified = 1;
            progressDialog.dismiss();
            showMessage("Verify your Email First!");
            firebaseAuth.signOut();

            return;
        } else {
            notVerified = 0;
            checkIfUserIsLogin(0);
            return;
        }
    }

    public void showMessage(String message) {

        ConstraintLayout clt = (ConstraintLayout) findViewById(R.id.loginLayout);
        Snackbar snackbar = Snackbar.make(clt, message, Snackbar.LENGTH_LONG);
        snackbar.show();
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

    public void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        progressDialog.setMessage("Signing In...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
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
                progressDialog.dismiss();
                showMessage("Unable to Sign in using Google");
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser googleuser = firebaseAuth.getCurrentUser();
                            googleuserid = googleuser.getUid();
                            name = googleuser.getDisplayName();
                            google_email = googleuser.getEmail();


                            databaseReference.child("Users").child(googleuserid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue() != null){
                                        if(dataSnapshot.child("userType").getValue().toString().equals("Member")) {
                                            progressDialog.dismiss();
                                            startActivity(homePage);
                                        }
                                        else{
                                            progressDialog.dismiss();
                                            startActivity(adminPage);
                                        }
                                    }
                                    else if(dataSnapshot.getValue() == null){
                                        Intent detailsIntent = new Intent(Login.this, custom_dialog_google_sign_in.class);
                                        detailsIntent.putExtra("googleUserId",googleuserid);
                                        detailsIntent.putExtra("googleName",name);
                                        detailsIntent.putExtra("googleEmail",google_email);
                                        progressDialog.dismiss();
                                        startActivity(detailsIntent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Log.d(TAG, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    public void checkIfUserIsLogin(final int sign) {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if(sign == 1)
                    {
                        progressDialog.setMessage("Signing In...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }
                    //If user is already logged-in redirect to homepage

                    if (cd.isConnected()) {
                        activeUser.setUserId(user.getUid());

                        databaseReference.child("Users").child(activeUser.getUserId()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            if (notVerified != 1) {
                                                if(dataSnapshot.child("userType").getValue().toString().equals("Member"))
                                                {
                                                    activeUser.setEmail(dataSnapshot.child("email").getValue().toString());
                                                    activeUser.setFullname(dataSnapshot.child("fullname").getValue().toString());
                                                    activeUser.setContact_number(dataSnapshot.child("contact_number").getValue().toString());
                                                    activeUser.setAddress(dataSnapshot.child("address").getValue().toString());
                                                    activeUser.setProfilePicture(dataSnapshot.child("profile_picture").getValue().toString());
                                                    activeUser.setGender(dataSnapshot.child("gender").getValue().toString());
                                                    activeUser.setUserType(dataSnapshot.child("userType").getValue().toString());
                                                    progressDialog.dismiss();
                                                    startActivity(homePage);
                                                }
                                                else
                                                {
                                                    activeUser.setEmail(dataSnapshot.child("email").getValue().toString());
                                                    activeUser.setFullname(dataSnapshot.child("fullname").getValue().toString());
                                                    activeUser.setContact_number(dataSnapshot.child("contact_number").getValue().toString());
                                                    activeUser.setAddress(dataSnapshot.child("address").getValue().toString());
                                                    activeUser.setProfilePicture(dataSnapshot.child("profile_picture").getValue().toString());
                                                    activeUser.setGender(dataSnapshot.child("gender").getValue().toString());
                                                    activeUser.setUserType(dataSnapshot.child("userType").getValue().toString());
                                                    startActivity(adminPage);
                                                }
                                            }
                                        }

                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );
                    } else {
                        buildDialog(Login.this).show();
                    }

                } else {
                    Log.d(TAG, "onAuthStateChange:signed_out");
                }
            }
        }; //Use to get current user State
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Internet Connection to access BentaBasura." + "\n" + " Press OK to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        return builder;
    }

    public AlertDialog.Builder buildMyDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Google Sign-in");
        builder.setMessage("This Service is not yet available at this moment.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onRestart();
            }
        });

        return builder;
    }

    public static void setPersist(boolean fPersist)
    {
        firebasePersist = fPersist;
    }

    public static boolean getPersist()
    {
        return firebasePersist;
    }

}
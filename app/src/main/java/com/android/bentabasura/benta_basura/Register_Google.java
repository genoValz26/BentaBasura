package com.android.bentabasura.benta_basura;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by reymond on 15/10/2017.
 */

public class Register_Google extends AppCompatActivity implements View.OnClickListener {
    Button register,backbtn;
    Intent loginPage;
    EditText txtUser,txtFirstName, txtLastName,txtMobileNum,txtAddress;
    RadioButton malebtn,femalebtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_google);

        txtUser = (EditText) findViewById(R.id.txtUser);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtMobileNum = (EditText) findViewById(R.id.txtMobileNum);
        txtAddress = (EditText) findViewById(R.id.txtAddress);

        malebtn = (RadioButton) findViewById(R.id.malebtn);
        femalebtn = (RadioButton) findViewById(R.id.femalebtn);

        loginPage = new Intent(Register_Google.this, Login.class);

        register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(this);
        backbtn = (Button) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}

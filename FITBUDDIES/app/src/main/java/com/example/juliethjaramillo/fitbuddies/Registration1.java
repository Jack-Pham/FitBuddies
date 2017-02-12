package com.example.juliethjaramillo.fitbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by michael on 12.02.17.
 */

public class Registration1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_part1);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void continueClick(View view) {
        String firstname = ((EditText) findViewById(R.id.userfirstname)).getText().toString();
        String lastname = ((EditText) findViewById(R.id.userlastname)).getText().toString();
        String dob = ((EditText) findViewById(R.id.userdob)).getText().toString();
        String email = ((EditText) findViewById(R.id.useremail)).getText().toString();
        String password = ((EditText) findViewById(R.id.userpassword)).getText().toString();

        //TODO: Validation

        Intent intent = new Intent(this, Registration2.class);
        intent.putExtra("firstname",firstname);
        intent.putExtra("lastname",lastname);
        intent.putExtra("dob",dob);
        intent.putExtra("email",email);
        intent.putExtra("password",password);


        finish();
        startActivity(intent);
    }
}

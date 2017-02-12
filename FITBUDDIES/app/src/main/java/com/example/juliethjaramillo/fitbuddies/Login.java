package com.example.juliethjaramillo.fitbuddies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by michael on 12.02.17.
 */

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void registerClick(View view) {
        Intent intent = new Intent(this, Registration1.class);
        finish();
        startActivity(intent);
    }


    public void loginClick(View view) {
        EditText editText1 = (EditText) findViewById(R.id.editText1);
        String email = editText1.getText().toString();

        EditText editPassword = (EditText) findViewById(R.id.password);
        String password = editPassword.getText().toString();

        tryLogin(email,password);
    }



    private void tryLogin(String email,String password){
        AsyncTask<String, Void, user> userLoginTask = new AsyncTask<String, Void, user>() {
            @Override
            protected user doInBackground(String... params) {
                try {
                    return user.login(params[0],params[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(user user) {
                gotUserDetails(user);
            }
        };

        userLoginTask.execute(email, password);
    }

    private void gotUserDetails(user user){
        if(user != null && user.firstname != null){
            (new Preferences(this)).setUserId(user.id);
            Intent intent = new Intent(this, home.class);
            finish();
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }

    }
}

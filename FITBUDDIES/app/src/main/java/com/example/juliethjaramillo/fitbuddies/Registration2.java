package com.example.juliethjaramillo.fitbuddies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by michael on 12.02.17.
 */

public class Registration2 extends AppCompatActivity {
    String firstname;
    String lastname;
    String dob;
    String email;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_part2);

        Intent intent = getIntent();
        firstname = intent.getStringExtra("firstname");
        lastname = intent.getStringExtra("lastname");
        dob = intent.getStringExtra("dob");
        email =intent.getStringExtra("email");
        password = intent.getStringExtra("password");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void register(View view) {
        AsyncTask<Void, Void, Void> registerTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    user.register(email,password,dob,firstname,lastname,"0","0");
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                done();
            }
        };

        registerTask.execute();
    }

    public void done(){
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Login.class);
        finish();
        startActivity(intent);
    }
}

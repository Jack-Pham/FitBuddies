package com.example.juliethjaramillo.fitbuddies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_menu);

        Preferences prefs = new Preferences(this);
        if(!prefs.isLoggedIn()){
            Intent intent = new Intent(this, Login.class);
            finish();
            startActivity(intent);
            return;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Preferences prefs = new Preferences(this);

        getDetails(prefs.getUserId());
    }

    private void getDetails(int id) {
        setMyPicture(id);

        AsyncTask<Integer, Void, user> userDetailTask = new AsyncTask<Integer, Void, user>() {
            @Override
            protected user doInBackground(Integer... params) {
                try {
                    return user.get(params[0]);
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

        userDetailTask.execute(id);

        AsyncTask<Integer, Void, Points> userPointsTask = new AsyncTask<Integer, Void, Points>() {
            @Override
            protected Points doInBackground(Integer... params) {
                try {
                    return Points.get(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Points points) {
                gotOwnPoints(points);
            }
        };

        userPointsTask.execute(id);

        AsyncTask<Integer, Void, BuddyInformation> userBuddyTask = new AsyncTask<Integer, Void, BuddyInformation>() {
            int currentUser;

            @Override
            protected BuddyInformation doInBackground(Integer... params) {
                try {
                    currentUser = params[0];
                    return BuddyInformation.get(currentUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(BuddyInformation buddyInformation) {
                gotBuddyInformation(currentUser,buddyInformation);
            }
        };

        userBuddyTask.execute(id);

    }

    private void gotBuddyInformation(int currentUser, BuddyInformation buddyInformation) {
        int buddy = (currentUser == buddyInformation.id1)?buddyInformation.id2:buddyInformation.id1;

        setBuddyPicture(buddy);

        AsyncTask<Integer, Void, user> buddyDetailTask = new AsyncTask<Integer, Void, user>() {
            @Override
            protected user doInBackground(Integer... params) {
                try {
                    return user.get(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(user user) {
                gotBuddyDetails(user);
            }
        };

        buddyDetailTask.execute(buddy);

        AsyncTask<Integer, Void, Points> buddyPointsTask = new AsyncTask<Integer, Void, Points>() {
            @Override
            protected Points doInBackground(Integer... params) {
                try {
                    return Points.get(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Points points) {
                gotBuddyPoints(points);
            }
        };

        buddyPointsTask.execute(buddy);

    }

    private void gotOwnPoints(Points points) {
        TextView point = (TextView) findViewById(R.id.pu);
        point.setText(Integer.toString(points.points));
    }

    private void gotBuddyPoints(Points points) {
        TextView point = (TextView) findViewById(R.id.pb);
        point.setText(Integer.toString(points.points));
    }


    private void gotUserDetails(user user) {
        if(user == null){
            Toast.makeText(this, "Excuse our mess, our server is apparently out for a coffee break.", Toast.LENGTH_SHORT).show();
        }

        setMyFlag(user.countryIsoAlpha2);

        TextView name = (TextView) findViewById(R.id.nameu);
        name.setText("" + user.firstname+ " " + user.lastname + "");

        TextView age = (TextView) findViewById(R.id.au);
        age.setText(Long.toString(((System.currentTimeMillis() / 1000L) - user.dob)/(24*365*60*60)));

    }

    private void gotBuddyDetails(user user) {
        if(user == null){
            Toast.makeText(this, "Excuse our mess, our server is apparently out for a coffee break.", Toast.LENGTH_SHORT).show();
        }

        setBuddyFlag(user.countryIsoAlpha2);

        TextView name = (TextView) findViewById(R.id.nameb);
        name.setText("" + user.firstname+ " " + user.lastname + "");

        TextView age = (TextView) findViewById(R.id.ab);
        age.setText(Long.toString(((System.currentTimeMillis() / 1000L) - user.dob)/(24*365*60*60)));

    }

    private void setMyPicture(int id) {
        new DownloadImageTask((ImageButton) findViewById(R.id.imageButton1))
                .execute(config.baseUrl + "/getPicture/" + id + ".jpg/");
    }

    private void setBuddyPicture(int id) {
        new DownloadImageTask((ImageButton) findViewById(R.id.imageButton2))
                .execute(config.baseUrl + "/getPicture/" + id + ".jpg/");
    }

    private void setMyFlag(String country){
        new DownloadImageTaskView((ImageView) findViewById(R.id.imageView5))
                .execute("http://www.geognos.com/api/en/countries/flag/" + country.toUpperCase() + ".png");
    }

    private void setBuddyFlag(String country){
        new DownloadImageTaskView((ImageView) findViewById(R.id.imageView3))
                .execute("http://www.geognos.com/api/en/countries/flag/" + country.toUpperCase() + ".png");
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageButton bmImage;

        public DownloadImageTask(ImageButton bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private class DownloadImageTaskView extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTaskView(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

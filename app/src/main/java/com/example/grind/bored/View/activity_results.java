package com.example.grind.bored.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grind.bored.Contoller.DatabaseHelper;
import com.example.grind.bored.Contoller.HttpHandler;
import com.example.grind.bored.R;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class activity_results extends Activity {

    String apiKey = "7ad44d7fc1746d1a0c0d964fb926fb1e"; // Cle API de TMDB
    String TAG = activity_results.class.getSimpleName();
    ProgressDialog pDialog;
    int randomPage;
    int randomMovie;
    int totalPages;

    String title;
    double rate;
    String poster_path;
    String release;
    String overview;
    String genre_id;

    DatabaseHelper mDatabaseHelper;
    SparkButton sparkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        sparkButton = (SparkButton)findViewById(R.id.spark_button);
        mDatabaseHelper = new DatabaseHelper(this);
        new getMovie().execute();

        // Pour ajouter au favoris
        sparkButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(title != null) {
                    if (buttonState) {
                        try {
                            mDatabaseHelper.addData(title);
                        } catch (SQLiteException e) {
                            toastMessage("this movie is already in your favorites");
                        }
                    } else {
                        try {
                            mDatabaseHelper.deleteData(title);
                        } catch (SQLiteException e) {
                            toastMessage("Something went wrong :(");
                        }
                    }
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

    }

    private class getMovie extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(activity_results.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            try
            {
                Bundle genreData = getIntent().getExtras();
                genre_id = genreData.getString("Key");
                //Pour avoir un resultat aleatoire
                randomPage = (int)(Math.random()*(20+1-1)+1);
                randomMovie = (int)(Math.random()*(20+1)+0);

                // Le lien qui contient les differentes informations en JSON qu'on recuperera apres
                String jsonStr_1 = sh.makeServiceCall("https://api.themoviedb.org/3/discover/movie?api_key="+apiKey+"&vote_average.gte=6.5&include_adult=false&page="+randomPage+"&with_genres="+genre_id+"&language=en-US");
                Log.e(TAG,"Response from url: " +jsonStr_1);


                // Pour recuperer les details du film du JSON
                if(jsonStr_1 != null)
                {
                    JSONObject jsonObj_2 = new JSONObject(jsonStr_1);
                    JSONArray results = jsonObj_2.getJSONArray("results");

                    JSONObject r = results.getJSONObject(randomMovie);

                    title = r.getString("title");
                    rate = r.getDouble("vote_average");
                    poster_path = r.getString("poster_path");
                    release = r.getString("release_date");
                    overview = r.getString("overview");
                }

            } catch(Exception e)
            {
                e.getMessage();
            }


            return null;
        }

        //Pour afficher les details dans la vue
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            ImageView iv = (ImageView) findViewById(R.id.poster);
            Picasso.with(activity_results.this).load("https://image.tmdb.org/t/p/w500"+poster_path).into(iv);
            TextView textview_title = (TextView) findViewById(R.id.title);
            textview_title.setText(title);
            TextView textview_release = (TextView) findViewById(R.id.release);
            textview_release.setText("RELEASE DATE : "+release);
            TextView edittext_overview = (TextView) findViewById(R.id.synop);
            edittext_overview.setText("OVERVIEW : "+overview);
            TextView textview_rate = (TextView) findViewById(R.id.rate);
            textview_rate.setText("RATED : "+rate);
        }
    }

    public void toastMessage(String Message)
    {
        Toast.makeText(this,Message, Toast.LENGTH_SHORT).show();
    }
}

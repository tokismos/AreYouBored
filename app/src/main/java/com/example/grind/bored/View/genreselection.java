package com.example.grind.bored.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.grind.bored.Contoller.HttpHandler;
import com.example.grind.bored.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class genreselection extends Activity {

    ProgressDialog pDialog;
    String TAG = activity_results.class.getSimpleName();
    String apiKey = "7ad44d7fc1746d1a0c0d964fb926fb1e"; // Ma propre clé API du site TMDB

    int genre_id;
    String genre;
    Hashtable table = new Hashtable();
    List<String> spinnerTable;

    Intent intent;
    String selectedKey;
    Button favButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genreselection);
        new getGenre().execute(); // la fonction qui permet de recuperer le genre selectionné

        Button getmovie = (Button) findViewById(R.id.getmovie_button);
        getmovie.setOnClickListener(  //
                new Button.OnClickListener(){
                    public void onClick(View v){

                        Intent intent = new Intent(genreselection.this, activity_results.class);
                        intent.putExtra("Key",selectedKey);

                        startActivity(intent);
                    }
                }
        );

        favButton = (Button)findViewById(R.id.fav_button);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(genreselection.this,activity_favorite.class);

                startActivity(intent);
            }
        });
    }

    private class getGenre extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() { // Avant l'exécution
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(genreselection.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {   //Recuperer tous les genres de films depuis JSON et les afficher dans un spinner ( cela se fait en arriere plan)
            HttpHandler sh = new HttpHandler();
            spinnerTable = new ArrayList<String>();

            try {
                String jsonStr_1 = sh.makeServiceCall("https://api.themoviedb.org/3/genre/movie/list?api_key="+apiKey);
                Log.e(TAG,"Response from url: " +jsonStr_1);
                intent = new Intent(genreselection.this, activity_results.class);


                if(jsonStr_1 != null)
                {
                    JSONObject jsonObj_1 = new JSONObject(jsonStr_1);
                    JSONArray results = jsonObj_1.getJSONArray("genres");

                    for(int i=0; i<results.length(); i++) //Parcourir tout l'objet
                    {
                        JSONObject g = results.getJSONObject(i);

                        genre_id = g.getInt("id");
                        genre = g.getString("name");
                        table.put(genre_id,genre); // enregistrer les données dans un HashMap avec genre_id la clé et genre sa valeure!
                        spinnerTable.add(genre);// ajouter le genre dans le spinner
                    }
                }
            } catch (Exception e)
            {
                e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {   // s'execute à la fin
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, spinnerTable);  // pour lister les differents genres
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override // recuperer la clé de la valeur selectionné
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();

                    for(Object o : table.entrySet())
                    {
                        Map.Entry entry = (Map.Entry) o;
                        if(entry.getValue().equals(selectedItem))
                        {
                            selectedKey = entry.getKey().toString();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }
}

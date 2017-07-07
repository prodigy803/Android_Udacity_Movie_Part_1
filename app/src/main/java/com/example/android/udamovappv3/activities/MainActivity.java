package com.example.android.udamovappv3.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.udamovappv3.BasicInformation;
import com.example.android.udamovappv3.R;
import com.example.android.udamovappv3.adapters.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    boolean mediumType = false;
    private MovieAdapter movieAdapter;

    private RecyclerView.LayoutManager layoutManager;

    private ArrayList titles_images;

    private String original_name,title_image,id;

    private ArrayList<String> imageURL_List = new ArrayList<String>();

    private ArrayList<String> title_List = new ArrayList<String>();

    private ArrayList<String> item_id = new ArrayList<String>();

    private ArrayList<String> item_type = new ArrayList<String>();

    boolean b = false;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        Uri.Builder uri_builder_for_now_playing = new Uri.Builder();

        uri_builder_for_now_playing.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("now_playing")
                .appendQueryParameter("api_key",getString(R.string.api_key_query));

        String addressForNowPlaying = uri_builder_for_now_playing.build().toString();
        recyclerView(addressForNowPlaying);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_now_playing_movies:
                b = true;
                mediumType = true;
                //Pass the URL after building it:
                Uri.Builder uri_builder_for_now_playing = new Uri.Builder();

                uri_builder_for_now_playing.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath("now_playing")
                        .appendQueryParameter("api_key",getString(R.string.api_key_query));

                String addressForNowPlaying = uri_builder_for_now_playing.build().toString();

                recyclerView(addressForNowPlaying);
                return true;

            case R.id.action_upcoming_movies:
                b = true;
                mediumType = true;
                Uri.Builder uri_builder_for_upcoming = new Uri.Builder();

                uri_builder_for_upcoming.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath("upcoming")
                        .appendQueryParameter("api_key",getString(R.string.api_key_query));

                String addressForUpcoming = uri_builder_for_upcoming.build().toString();
                recyclerView(addressForUpcoming);
                return true;

            case R.id.action_popular_movies:
                b = true;
                mediumType = true;
                Uri.Builder uri_builder_for_popular = new Uri.Builder();

                uri_builder_for_popular.scheme("https")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath("popular")
                        .appendQueryParameter("api_key",getString(R.string.api_key_query));

                String addressForPopular = uri_builder_for_popular.build().toString();
                recyclerView(addressForPopular);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void recyclerView(String s) {

        if(b==false){
            titles_images = prepareDataForTV(s);
        }else if(b==true){
            titles_images.clear();
            imageURL_List.clear();
            title_List.clear();
            item_id.clear();
            item_type.clear();

            titles_images = prepareDataForTV(s);
        }

        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getApplicationContext(),2);

        recyclerView.setLayoutManager(layoutManager);
        //MovieAdapter movieAdapter = new MovieAdapter(this, titles_images);

        movieAdapter = new MovieAdapter(this, titles_images);

        recyclerView.setAdapter(movieAdapter);

        movieAdapter.notifyDataSetChanged();
    }

    private ArrayList prepareDataForTV(String URL){
        final String type;

        final ArrayList short_info = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray result = response.getJSONArray("results");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObj2 = result.getJSONObject(i);
                                title_image = jsonObj2.getString("poster_path");
                                title_image = title_image.substring(1);
                                id = jsonObj2.getString("id");
                                //TODO handle an empty poster path

                                title_List.add(original_name);
                                imageURL_List.add(title_image);
                                item_id.add(id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for(int i=0;i<title_List.size();i++){
                            BasicInformation information = new BasicInformation();
                            information.set_ImageUrl(imageURL_List.get(i));

                            information.set_ItemId(item_id.get(i));
                            short_info.add(information);
                        }

                        movieAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"There was an error getting the Basic Movie Info",Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsObjRequest);
        return short_info;
    }


}

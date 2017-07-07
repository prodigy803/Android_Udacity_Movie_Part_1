package com.example.android.udamovappv3.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.udamovappv3.R;
import com.example.android.udamovappv3.adapters.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {
    TextView tv_overview,tv_title_name,tv_runTime,tv_releaseDate,tv_rating;

    ArrayList<String> trailerKeysArray = new ArrayList<String>();;

    RecyclerView rv_trailer;

    TrailerAdapter trailerAdapter;

    ImageView iv_Image;

    RequestQueue queue;

    String id;

    private LinearLayoutManager layoutManager;

    int id_to_be_passed;

    Button mark_as_fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        initViews();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        id_to_be_passed = Integer.parseInt(id);

        getMovieData(id);
        getMovieTrailerData(id);
        mark_as_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailedActivity.this,"Doesnt Do anything for now",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(){
        queue = Volley.newRequestQueue(this);
        rv_trailer = (RecyclerView)findViewById(R.id.rv_trailer);
        rv_trailer.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setAutoMeasureEnabled(true);
        rv_trailer.setLayoutManager(layoutManager);

        tv_title_name = (TextView)findViewById(R.id.title_name);
        tv_overview = (TextView)findViewById(R.id.tv_overview);
        iv_Image = (ImageView)findViewById(R.id.iv_poster);
        tv_runTime = (TextView)findViewById(R.id.tv_runTime);
        tv_releaseDate = (TextView)findViewById(R.id.tv_releaseDate);
        tv_rating = (TextView)findViewById(R.id.tv_rating);
        mark_as_fav = (Button)findViewById(R.id.bt_mark_as_fav);
    }

    private void getMovieData(String id){
        Uri.Builder uri_builder_detailed = new Uri.Builder();
        uri_builder_detailed
                .scheme("https")
                .authority(getString(R.string.api_basic_movie_url_authority))
                .appendPath("3")
                .appendPath("movie")
                .appendPath(id)
                .appendQueryParameter("api_key",getString(R.string.api_key_query));
        String string = uri_builder_detailed.build().toString();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, string, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String overView = response.getString("overview");
                            String posterPath = response.getString("poster_path");
                            String name = response.getString("original_title");
                            String runtime = response.getString("runtime");
                            String releaseDateFullForm = response.getString("release_date");
                            String segments[] = releaseDateFullForm.split("-");
                            String releaseDate = segments[0];
                            String rating = response.getString("vote_average");

                            String url_of_image = getString(R.string.PIC_BASE_URL) + posterPath;


                            runtime = runtime.concat("mins");
                            rating = rating.concat("/10");

                            Picasso.with(DetailedActivity.this).load(url_of_image).into(iv_Image);
                            tv_overview.setText(overView);
                            tv_title_name.setText(name);
                            tv_runTime.setText(runtime);
                            tv_releaseDate.setText(releaseDate);
                            tv_rating.setText(rating);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getMovieTrailerData(String id){
        Uri.Builder uri_builder_trailers = new Uri.Builder();
        uri_builder_trailers
                .scheme("https")
                .authority(getString(R.string.api_basic_movie_url_authority))
                .appendPath("3")
                .appendPath("movie")
                .appendPath(id)
                .appendPath("videos")
                .appendQueryParameter("api_key",getString(R.string.api_key_query));

        String string = uri_builder_trailers.build().toString();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, string, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject jsonObj2 = results.getJSONObject(i);
                                String key = jsonObj2.getString("key");
                                String Youtube_url = getString(R.string.Youtube_Video_Url).concat(key);
                                trailerKeysArray.add(Youtube_url);
                            }

                            trailerAdapter = new TrailerAdapter(DetailedActivity.this, trailerKeysArray);
                            rv_trailer.setAdapter(trailerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailedActivity.this,getString(R.string.error_for_trailer),Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailedmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_review:
                Intent intentToReview = new Intent(DetailedActivity.this,ReviewActivity.class);
                intentToReview.putExtra("id_to_url",id_to_be_passed);
                startActivity(intentToReview);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}

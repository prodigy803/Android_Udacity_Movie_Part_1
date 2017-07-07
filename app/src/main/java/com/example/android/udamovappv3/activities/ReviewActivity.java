package com.example.android.udamovappv3.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.udamovappv3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    ArrayList<String> reviewsArray;

    ArrayAdapter arrayAdapterForReviews;

    ListView listViewForReviews;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        reviewsArray = null;
        reviewsArray = new ArrayList<String>();
        listViewForReviews = (ListView)findViewById(R.id.lv_reviews);

        Intent intentFromDet = getIntent();

        i = intentFromDet.getIntExtra("id_to_url",0);

        RequestQueue queue2 = Volley.newRequestQueue(this);

        Uri.Builder uri_builder_reviews = new Uri.Builder();
        uri_builder_reviews
                .scheme("https")
                .authority(getString(R.string.api_basic_movie_url_authority))
                .appendPath("3")
                .appendPath("movie")
                .appendPath(String.valueOf(i))
                .appendPath("reviews")
                .appendQueryParameter("api_key",getString(R.string.api_key_query));

        String string = uri_builder_reviews.build().toString();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, string, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray result = response.getJSONArray("results");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObj2 = result.getJSONObject(i);
                                String content = jsonObj2.getString("content");
                                Log.i("content",content);
                                reviewsArray.add(content);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        arrayAdapterForReviews = new ArrayAdapter(ReviewActivity.this,android.R.layout.simple_list_item_1,reviewsArray);
                        listViewForReviews.setAdapter(arrayAdapterForReviews);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReviewActivity.this,getString(R.string.error_for_reviews),Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reviewmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_goBack:
                finish();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }
}

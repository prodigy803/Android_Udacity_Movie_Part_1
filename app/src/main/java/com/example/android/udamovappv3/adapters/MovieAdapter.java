package com.example.android.udamovappv3.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udamovappv3.BasicInformation;
import com.example.android.udamovappv3.R;

import com.example.android.udamovappv3.activities.DetailedActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private ArrayList<BasicInformation> titles_images;

    private Context context;

    public MovieAdapter(Context context, ArrayList<BasicInformation> titles_images){
        this.context = context;
        this.titles_images = titles_images;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder  {
        ImageView img_android;
        TextView item_id;

        public MovieAdapterViewHolder(View view){
            super(view);
            img_android = (ImageView)view.findViewById(R.id.img_android);
            item_id = (TextView)view.findViewById(R.id.item_id);
        }
    }

    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.MovieAdapterViewHolder holder, int position) {
        Uri.Builder uri_builder = new Uri.Builder();

        uri_builder
                .scheme("https")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(titles_images.get(position).get_ImageUrl());
        String url = uri_builder.build().toString();

        Picasso.with(context).load(url).resize(260, 320).into(holder.img_android);

        holder.item_id.setText(titles_images.get(position).get_ItemId());

        holder.img_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("id",holder.item_id.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(titles_images.size()==0){
            return 0;
        }else{
            return titles_images.size();
        }
    }
    public void clear() {
        int size = this.titles_images.size();
        this.titles_images.clear();
        notifyItemRangeRemoved(0, size);
    }
}

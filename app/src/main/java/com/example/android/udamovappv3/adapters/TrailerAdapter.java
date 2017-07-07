package com.example.android.udamovappv3.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udamovappv3.R;
import com.example.android.udamovappv3.activities.DetailedActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private ArrayList<String> Youtube_URLS;

    private Context Context;

    public TrailerAdapter(Context context, ArrayList<String> Youtube_URLS){
        this.Context = context;
        this.Youtube_URLS = Youtube_URLS;
    }
    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        Picasso.with(Context).load(R.drawable.ic_play_arrow_black_24dp).into(holder.iv_playbutton);
        holder.item_id.setText(Youtube_URLS.get(position));
        holder.item_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Text_to_Youtube = (String) holder.item_id.getText();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Text_to_Youtube));
                Context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(Youtube_URLS.size()==0){
            return 0;
        }else{
            return Youtube_URLS.size();
        }
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_playbutton;
        TextView item_id;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            iv_playbutton = (ImageView)itemView.findViewById(R.id.play_button);
            item_id = (TextView)itemView.findViewById(R.id.tv_trailer_sequence);
        }
    }
}

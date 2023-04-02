package com.example.movietrailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietrailer.model.MovieModel;
import com.example.movietrailer.R;
import com.example.movietrailer.retrofit.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MovieModel.Results> resultsList;
    private Context context;

    public MainAdapter(List<MovieModel.Results> resultsList, Context context) {
        this.resultsList = resultsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.adapter_movie,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieModel.Results result = resultsList.get(position);

        holder.title.setText(result.getTitle());
        Picasso.get()
                .load(Constant.IMAGE_PATH + result.getPoster_path())
                .placeholder(R.drawable.placeholder_portrait)
                .error(R.drawable.placeholder_portrait)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_poster);
            title = itemView.findViewById(R.id.text_poster);
        }
    }

    public void setData(List<MovieModel.Results> res) {
        resultsList.clear();
        resultsList.addAll(res);
        notifyDataSetChanged();
    }
}

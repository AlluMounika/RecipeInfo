package com.example.lenovo.recipeinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Lenovo on 30-05-2018.
 */

public class RecipeNamesAdapter extends RecyclerView.Adapter<RecipeNamesAdapter.MyViewHolder> {
    List<RecipePojo> recipeDetails;
    Context c;
    public RecipeNamesAdapter(Context c, List<RecipePojo> recipeDetails) {
        this.c=c;
        this.recipeDetails = recipeDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.recipeName_tv.setText(recipeDetails.get(position).getName());
        Picasso.get().load(R.drawable.dsih_icon).into(holder.recipeimg);
    }

    @Override
    public int getItemCount() {
        return recipeDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName_tv;
        ImageView recipeimg;
        public MyViewHolder(View itemView) {
            super(itemView);
            recipeName_tv=(TextView)itemView.findViewById(R.id.recipename);
            recipeimg=(ImageView) itemView.findViewById(R.id.recipeimage);
            recipeName_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i=getLayoutPosition();
                    Intent intent = new Intent(c,RecipeStepsListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("recipe",recipeDetails.get(i));
                    intent.putExtra("bundle",bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}

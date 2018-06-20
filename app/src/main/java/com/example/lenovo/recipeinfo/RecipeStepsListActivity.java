package com.example.lenovo.recipeinfo;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.lenovo.recipeinfo.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepsDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepsListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    RecipePojo recipePojo;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipesteps_list);
        TextView ingredients_tv=findViewById(R.id.ingredient_View);
        Bundle bundle=getIntent().getBundleExtra("bundle");
        Bundle bundle1 = new Bundle();
         recipePojo=bundle.getParcelable("recipe");
        bundle1.putParcelable("recipe",recipePojo);

        String  fullingredients="";
        fullingredients=ingredientsSetView(recipePojo);
        Log.i("ingredients",fullingredients);
        ingredients_tv.setText(fullingredients);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to widget", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                DataBase.recipeName=recipePojo.getName();
            }
        });
        if (findViewById(R.id.recipesteps_detail_container) != null) {
            mTwoPane = true;
        }
        if (findViewById(R.id.recipesteps_detail_container) == null) {
            mTwoPane = false;
        }
        View recyclerView = findViewById(R.id.recipesteps_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView,recipePojo);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView,RecipePojo recipePojo) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this,recipePojo , mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeStepsListActivity mParentActivity;
        private final RecipePojo Recipe;
        private final boolean mTwoPane;
        SimpleItemRecyclerViewAdapter(RecipeStepsListActivity parent, RecipePojo items, boolean twoPane) {
            Recipe = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipesteps_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.StepsView.setText(Recipe.getSteps().get(position).getShortDescription());
            holder.itemView.setTag(Recipe.getSteps().get(position));
        }

        @Override
        public int getItemCount() {
            return Recipe.getSteps().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView StepsView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                StepsView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                StepsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i=getLayoutPosition();
                        Step item=Recipe.getSteps().get(i);
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putParcelable("adapterBundle",item);
                            RecipeStepsDetailFragment fragment = new RecipeStepsDetailFragment();
                            fragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.recipesteps_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = mParentActivity;
                            Intent intent = new Intent(context, RecipeStepsDetailActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putParcelable("stepitem",item);
                            intent.putExtra("stepsbundle",bundle);
                            v.getContext().startActivity(intent);
                        }
                    }
                });
            }
        }
    }
    public String ingredientsSetView(RecipePojo recipePojo)
    {
        String ingredientdetails,fullingredients="";
        for (int i=0;i<recipePojo.getIngredients().size();i++){
            if (recipePojo.getIngredients().get(i)!=null){
                ingredientdetails=recipePojo.getIngredients().get(i).getIngredient()+"\t\t"+recipePojo.getIngredients().get(i).getQuantity()+recipePojo.getIngredients().get(i).getMeasure()+"\n";
                fullingredients+=ingredientdetails;
            }
        }
        return fullingredients;
    }

}

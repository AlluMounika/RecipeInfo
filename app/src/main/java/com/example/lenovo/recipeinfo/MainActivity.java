package com.example.lenovo.recipeinfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    static RecyclerView rev;
    String recipename1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rev=(RecyclerView)findViewById(R.id.recycler);
        getRecipesnames();

    }

    private void getRecipesnames() {
        List<RecipePojo> recipes;
        final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeApi retrofitClass = retrofit.create(RecipeApi.class);
        Call<List<RecipePojo>> call = retrofitClass.getRecipes();
        call.enqueue(new Callback<List<RecipePojo>>() {
            @Override
            public void onResponse(Call<List<RecipePojo>> call, Response<List<RecipePojo>> response) {
                List<RecipePojo> RecipeList = response.body();
                recipename1= RecipeList.get(1).getName();
                Cursor Ingredients=getContentResolver().query(Uri.parse(DataBase.CONTENT_URI+""),null,null,null,null);
                if(Ingredients.getCount()<=0) {
                   Insert(RecipeList);
                }
                MainActivity.rev.setLayoutManager(new GridLayoutManager(MainActivity.this, colspan()));
                MainActivity.rev.setAdapter(new RecipeNamesAdapter(MainActivity.this, RecipeList));
            }

            @Override
            public void onFailure(Call<List<RecipePojo>> call, Throwable t) {
                Log.i("response",t.getMessage() );
                Toast.makeText(MainActivity.this, "onFailure: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void Insert(List<RecipePojo> recipelist) {
        for (int j = 0; j < recipelist.size(); j++) {
            List<Ingredient> insertIngredients = recipelist.get(j).getIngredients();
            String recipename = recipelist.get(j).getName();
            Log.i("ingredientspojo", "in insert method");
            for (int i = 0; i < insertIngredients.size(); i++) {
                ContentValues ingredientsInfoValues = new ContentValues();
                ingredientsInfoValues.put(DataBase.RecipeName, recipename);
                ingredientsInfoValues.put(DataBase.IngredientsQuantity, insertIngredients.get(i).getQuantity());
                ingredientsInfoValues.put(DataBase.IngredientsMeasure, insertIngredients.get(i).getMeasure());
                ingredientsInfoValues.put(DataBase.IngredientsUsed, insertIngredients.get(i).getIngredient());
                getContentResolver().insert(Uri.parse(DataBase.CONTENT_URI + ""), ingredientsInfoValues);
            }
        }
    }
    public int colspan(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nCols = width/widthDivider;
        if (nCols<=2){
            return 2;
        }
        return nCols;
    }
   /* public String showIngredients() {
        Cursor ingredientsdetails=getContentResolver().query(Uri.parse(DataBase.CONTENT_URI+"*//*"),null,DataBase.RecipeName+" LIKE ? ",new String[]{"Brownies"},null);

        ArrayList<Ingredient> ingredientsInfo = new ArrayList<>();
        String quantity,measure,ingredients;
        String fulllist="";
        Log.i("ingredientspojo",Integer.toString(ingredientsdetails.getCount()));
        if (ingredientsdetails.getCount() > 0) {
            if (ingredientsdetails.moveToFirst()) {
                do {
                    Ingredient ingredientinformation = new Ingredient();
                    ingredientinformation.setQuantity(ingredientsdetails.getFloat(2));
                    ingredientinformation.setMeasure((ingredientsdetails.getString(3)));
                    ingredientinformation.setIngredient(ingredientsdetails.getString(4));
                    ingredientsInfo.add(ingredientinformation);
                    quantity=Float.toString(ingredientsdetails.getFloat(2));
                    measure=ingredientsdetails.getString(3);
                            ingredients=ingredientsdetails.getString(4);
                            fulllist+=ingredients+" "+quantity+" "+measure+"\n";
                } while (ingredientsdetails.moveToNext());
            }
        } else {
            Toast.makeText(this, "NO INGREDIENTS", Toast.LENGTH_LONG).show();
        }
        Log.i("text",fulllist);
        return fulllist;
    }*/
}

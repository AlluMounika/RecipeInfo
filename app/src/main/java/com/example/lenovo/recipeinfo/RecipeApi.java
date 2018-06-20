package com.example.lenovo.recipeinfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Lenovo on 29-05-2018.
 */

public interface RecipeApi {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<RecipePojo>> getRecipes();
}

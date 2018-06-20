package com.example.lenovo.recipeinfo;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lenovo on 01-06-2018.
 */

public class DataBase extends SQLiteOpenHelper {
    public static final String AUTHORITY = "com.example.lenovo.recipeinfo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String path_Tasks = "Ingredients";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(path_Tasks).build();
    public static final String Databasename = "IngredientsDatabase";
    public static final String tablename = "Ingredients";
    public static final int version = 2;
    public static final String id="id";
    public static final String RecipeName="RecipeName";
    public static final String IngredientsQuantity = "IngredientsQuantity";
    public static final String IngredientsMeasure="IngredientsMeasure";
    public static final String IngredientsUsed="IngredientsUsed";
    public static String recipeName="";
    public Context c;

    public DataBase(Context context) {
        super(context, tablename,null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            final String query= "CREATE TABLE "+ tablename + "("+ id +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    RecipeName+" STRING,"+IngredientsQuantity+" FLOAT,"+
                    IngredientsMeasure+" STRING,"+IngredientsUsed+" STRING);";
            db.execSQL(query);
    }
    public void settingRecipename(String recipeName)
    {
        this.recipeName=recipeName;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tablename);
        onCreate(db);
    }
    public String showFavoriteMovies()
    {
        String query="SELECT * FROM "+tablename+" WHERE "+RecipeName+" IN ('"+recipeName+"')";
        SQLiteDatabase database=getReadableDatabase();
        Cursor ingredientsdetails=database.rawQuery(query,null);
        ArrayList<Ingredient> ingredientsInfo = new ArrayList<>();
        String quantity,measure,ingredients;
        String fulllist="";
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
        }
        return fulllist;
    }
}


package com.example.lenovo.recipeinfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipePojo implements Parcelable{

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private List<Ingredient> ingredients = new ArrayList<>();
    @SerializedName("steps")
    private List<Step> steps = new ArrayList<>();
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    protected RecipePojo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    public static final Creator<RecipePojo> CREATOR = new Creator<RecipePojo>() {
        @Override
        public RecipePojo createFromParcel(Parcel in) {
            return new RecipePojo(in);
        }

        @Override
        public RecipePojo[] newArray(int size) {
            return new RecipePojo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeInt(getId());
       dest.writeString(getName());
       dest.writeTypedList(getIngredients());
        dest.writeTypedList(getSteps());
        dest.writeInt(getServings());
       dest.writeString(getImage());
    }

}

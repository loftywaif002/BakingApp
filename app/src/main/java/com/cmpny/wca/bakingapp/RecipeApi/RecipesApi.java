package com.cmpny.wca.bakingapp.RecipeApi;

import android.util.Log;

import com.cmpny.wca.bakingapp.Models.Recipe;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class RecipesApi implements Serializable {

    public static final String TAG = RecipesApi.class.getSimpleName();

    public static final String RECIPES_API_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static volatile RecipesApi sharedInstance = new RecipesApi();
    private ApiInterface recipesApiService;

    private RecipesApi() {
        //Prevent from the reflection api.
        if (sharedInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPES_API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        recipesApiService = retrofit.create(ApiInterface.class);
    }

    public static RecipesApi getInstance() {
        if (sharedInstance == null) {
            synchronized (RecipesApi.class) {
                if (sharedInstance == null) sharedInstance = new RecipesApi();
            }
        }

        return sharedInstance;
    }

    public void getRecipes(final RecipesApiCallback<List<Recipe>> recipesApiCallback) {
        recipesApiService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipesApiCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.e(TAG,"Request was cancelled");
                    recipesApiCallback.onCancel();
                } else {
                    Log.e(TAG,t.getMessage());
                    recipesApiCallback.onResponse(null);
                }
            }
        });
    }

    public interface RecipesApiCallback<T> {
        void onResponse(T result);

        void onCancel();
    }
}

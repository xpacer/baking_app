package com.xpacer.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xpacer.bakingapp.R;
import com.xpacer.bakingapp.adapter.RecipeListAdapter;
import com.xpacer.bakingapp.data.Recipe;
import com.xpacer.bakingapp.data.RecipeIngredient;
import com.xpacer.bakingapp.data.RecipeStep;
import com.xpacer.bakingapp.utils.Constants;
import com.xpacer.bakingapp.utils.NetworkUtils;
import com.xpacer.bakingapp.utils.Prefs;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NetworkUtils.QueryResult, RecipeListAdapter.onRecipeClickListener {

    @BindView(R.id.rv_recipe_step)
    RecyclerView rvRecipeList;

    NetworkUtils.ApiCallerTask apiCaller;
    RecipeListAdapter recipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recipeListAdapter = new RecipeListAdapter(this, this);
        setupView();
    }

    private void setupView() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        int spanCount = tabletSize ? 3 : 1;

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        rvRecipeList.setLayoutManager(layoutManager);
        rvRecipeList.setAdapter(recipeListAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        apiCaller = new NetworkUtils.ApiCallerTask(this, this, null);
        apiCaller.execute();
    }

    @Override
    public void onSuccess(String result) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Recipe>>() {
            }.getType();
            ArrayList<Recipe> recipes = gson.fromJson(result, listType);
            recipeListAdapter.setRecipes(recipes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);

        ArrayList<RecipeStep> recipeSteps = recipe.getSteps();
        ArrayList<RecipeIngredient> recipeIngredients = recipe.getIngredients();

        Prefs.getInstance(this).saveCurrentRecipe(recipe);

        intent.putExtra(Constants.ARG_RECIPE, recipe);
        intent.putParcelableArrayListExtra(Constants.ARG_RECIPE_STEPS, recipeSteps);
        intent.putParcelableArrayListExtra(Constants.ARG_RECIPE_INGREDIENTS, recipeIngredients);

        startActivity(intent);
    }
}

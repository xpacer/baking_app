package com.xpacer.bakingapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.xpacer.bakingapp.R;
import com.xpacer.bakingapp.data.Recipe;
import com.xpacer.bakingapp.data.RecipeIngredient;
import com.xpacer.bakingapp.data.RecipeStep;
import com.xpacer.bakingapp.fragments.MasterRecipeStepListFragment;
import com.xpacer.bakingapp.fragments.RecipeIngredientDetailFragment;
import com.xpacer.bakingapp.fragments.RecipeStepDetailFragment;
import com.xpacer.bakingapp.utils.Constants;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity implements
        MasterRecipeStepListFragment.OnItemClickListener, RecipeStepDetailFragment.OnRecipeButtonsClickListener {

    private boolean mTwoPane;
    private Recipe recipe;
    private ArrayList<RecipeStep> recipeSteps;
    private ArrayList<RecipeIngredient> recipeIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mTwoPane = getResources().getBoolean(R.bool.isTablet);

        Bundle extras = getIntent().getExtras();

        if (extras == null)
            return;

        recipe = extras.getParcelable(Constants.ARG_RECIPE);
        recipeIngredients = extras.getParcelableArrayList(Constants.ARG_RECIPE_INGREDIENTS);
        recipeSteps = extras.getParcelableArrayList(Constants.ARG_RECIPE_STEPS);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null && recipe != null) {
            actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupMasterListFragment(extras);

        if (mTwoPane) {
            setupTwoPaneView();
        }
    }

    private void setupTwoPaneView() {
        onItemSelected(0);
    }

    private void setupMasterListFragment(Bundle extras) {
        MasterRecipeStepListFragment recipeStepListFragment = new MasterRecipeStepListFragment();
        recipeStepListFragment.setArguments(extras);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ll_fragment_container, recipeStepListFragment)
                .commit();
    }

    @Override
    public void onItemSelected(int position) {
        if (position == 0) {
            showIngredients(mTwoPane);
        } else {
            RecipeStep recipeStep = recipeSteps.get(position - 1);
            showStep(recipeStep, position - 1, mTwoPane);
        }
    }


    private void showIngredients(boolean isTwoPane) {
        RecipeIngredientDetailFragment recipeIngredientDetailFragment = new RecipeIngredientDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(Constants.ARG_RECIPE_INGREDIENTS, recipeIngredients);
        recipeIngredientDetailFragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (isTwoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.ll_content_container, recipeIngredientDetailFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.ll_fragment_container, recipeIngredientDetailFragment)
                    .addToBackStack("fragIngredientDetail")
                    .commit();
        }
    }


    private void showStep(RecipeStep recipeStep, int position, boolean isTwoPane) {
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.ARG_RECIPE_STEP_DETAIL, recipeStep);
        arguments.putInt(Constants.ARG_RECIPE_STEP_INDEX, position);
        recipeStepDetailFragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (isTwoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.ll_content_container, recipeStepDetailFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.ll_fragment_container, recipeStepDetailFragment)
                    .addToBackStack("fragStepDetail")
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.home:
                onBackButtonPress();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void onBackButtonPress() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public void onRecipeStepButtonClicked(int position) {
        onItemSelected(position + 1);
    }
}

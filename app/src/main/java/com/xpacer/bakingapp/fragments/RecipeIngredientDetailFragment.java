package com.xpacer.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xpacer.bakingapp.R;
import com.xpacer.bakingapp.adapter.RecipeIngredientListAdapter;
import com.xpacer.bakingapp.data.RecipeIngredient;
import com.xpacer.bakingapp.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientDetailFragment extends Fragment {

    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView rvRecipeIngredients;

    public RecipeIngredientDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recipe_ingredients_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(Constants.ARG_RECIPE_INGREDIENTS)) {
            return view;
        }

        ArrayList<RecipeIngredient> recipeIngredients = args.getParcelableArrayList(Constants.ARG_RECIPE_INGREDIENTS);

        if (recipeIngredients != null) {
            setupView(recipeIngredients);
        }

        return view;
    }

    private void setupView(ArrayList<RecipeIngredient> recipeIngredients) {
        RecipeIngredientListAdapter recipeIngredientListAdapter = new RecipeIngredientListAdapter(getContext(), recipeIngredients);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvRecipeIngredients.setLayoutManager(layoutManager);
        rvRecipeIngredients.setAdapter(recipeIngredientListAdapter);
    }

}

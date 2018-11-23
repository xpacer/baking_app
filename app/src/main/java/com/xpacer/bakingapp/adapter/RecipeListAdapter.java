package com.xpacer.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xpacer.bakingapp.R;
import com.xpacer.bakingapp.data.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> recipes;
    private onRecipeClickListener recipeClickListener;

    public RecipeListAdapter(Context context, onRecipeClickListener recipeClickListener) {
        mContext = context;
        this.recipeClickListener = recipeClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.item_recipe_layout, viewGroup, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {
        if (recipes == null || recipes.isEmpty())
            return;

        recipeViewHolder.bindView(recipes.get(i));
    }

    @Override
    public int getItemCount() {
        if (recipes == null)
            return 0;

        return recipes.size();
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_recipe_card)
        CardView recipeCardLayout;

        @BindView(R.id.tv_recipe_name)
        TextView recipeName;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(final Recipe recipe) {
            recipeName.setText(recipe.getName());
            recipeCardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeClickListener.onRecipeClick(recipe);
                }
            });
        }
    }

    public interface onRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }
}

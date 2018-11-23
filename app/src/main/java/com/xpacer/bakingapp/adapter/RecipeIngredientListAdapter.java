package com.xpacer.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xpacer.bakingapp.R;
import com.xpacer.bakingapp.data.RecipeIngredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientListAdapter extends RecyclerView.Adapter<RecipeIngredientListAdapter.RecipeIngredientViewHolder> {

    private Context mContext;
    private ArrayList<RecipeIngredient> recipeIngredients;

    public RecipeIngredientListAdapter(Context context, ArrayList<RecipeIngredient> recipeIngredients) {
        mContext = context;
        this.recipeIngredients = recipeIngredients;
    }

    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.item_ingredient_layout, viewGroup, false);

        return new RecipeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder recipeIngredientViewHolder, int i) {
        recipeIngredientViewHolder.bindView(recipeIngredients.get(i));
    }

    @Override
    public int getItemCount() {
        if (recipeIngredients == null)
            return 0;

        return recipeIngredients.size();
    }

    class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;

        @BindView(R.id.tv_measure)
        TextView tvMeasure;

        @BindView(R.id.tv_quantity)
        TextView tvQuantity;

        RecipeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(RecipeIngredient recipeIngredient) {
            tvIngredient.setText(recipeIngredient.getIngredient());
            tvMeasure.setText(recipeIngredient.getMeasure());
            tvQuantity.setText(String.valueOf(recipeIngredient.getQuantity()));
        }
    }
}

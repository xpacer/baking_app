package com.xpacer.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpacer.bakingapp.R;
import com.xpacer.bakingapp.data.RecipeStep;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterRecipeStepListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<RecipeStep> recipeSteps;
    private InteractionListener mInteractionListener;

    private static final int INGREDIENT_VIEW_TYPE = 755;
    private static final int STEP_VIEW_TYPE = 465;


    public interface InteractionListener {
        void onItemClicked(int position);
    }

    public MasterRecipeStepListAdapter(Context context,
                                       ArrayList<RecipeStep> recipeSteps, InteractionListener listener) {

        mContext = context;
        this.recipeSteps = recipeSteps;
        mInteractionListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view;
        if (i == INGREDIENT_VIEW_TYPE) {
            view = inflater.inflate(R.layout.item_recipe_ingredient_layout, viewGroup, false);
            return new MasterRecipeStepListAdapter.RecipeIngredientViewHolder(view);
        }

        view = inflater.inflate(R.layout.item_recipe_step_layout, viewGroup, false);
        return new MasterRecipeStepListAdapter.RecipeStepViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder recipeStepViewHolder, int i) {
        if (i == 0)
            ((RecipeIngredientViewHolder) recipeStepViewHolder).bindIngredientsView();
        else
            ((RecipeStepViewHolder) recipeStepViewHolder).bindSteps(recipeSteps.get(i - 1));

    }

    @Override
    public int getItemCount() {
        if (recipeSteps == null)
            return 1;

        return recipeSteps.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? INGREDIENT_VIEW_TYPE : STEP_VIEW_TYPE;
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_item_container)
        LinearLayout container;
        @BindView(R.id.tv_recipe_step_name)
        TextView tvRecipeStepName;

        RecipeStepViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindSteps(RecipeStep recipeStep) {
            tvRecipeStepName.setText(recipeStep.getShortDescription());
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mInteractionListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }

    class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_item_container)
        LinearLayout container;
        @BindView(R.id.tv_recipe_step_name)
        TextView tvRecipeStepName;

        RecipeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindIngredientsView() {
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mInteractionListener.onItemClicked(getAdapterPosition());
                }
            });
            tvRecipeStepName.setText(mContext.getString(R.string.recipe_ingredients));
        }
    }
}

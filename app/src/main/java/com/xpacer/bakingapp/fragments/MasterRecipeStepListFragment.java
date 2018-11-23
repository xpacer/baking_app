package com.xpacer.bakingapp.fragments;

import android.content.Context;
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
import com.xpacer.bakingapp.adapter.MasterRecipeStepListAdapter;
import com.xpacer.bakingapp.data.RecipeStep;
import com.xpacer.bakingapp.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterRecipeStepListFragment extends Fragment implements MasterRecipeStepListAdapter.InteractionListener {

    @BindView(R.id.rv_recipe_step_list)
    RecyclerView rvRecipeList;

    OnItemClickListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnItemClickListener");
        }
    }

    @Override
    public void onItemClicked(int itemPosition) {
        mCallback.onItemSelected(itemPosition);
    }


    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnItemClickListener {
        void onItemSelected(int position);
    }

    // Required empty constructor
    public MasterRecipeStepListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.master_fragment_recipe_steps_list, container, false);

        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(Constants.ARG_RECIPE)) {
            return rootView;
        }


        ArrayList<RecipeStep> recipeSteps = args.getParcelableArrayList(Constants.ARG_RECIPE_STEPS);

        MasterRecipeStepListAdapter mAdapter = new MasterRecipeStepListAdapter(getContext(),
                recipeSteps, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvRecipeList.setLayoutManager(linearLayoutManager);
        rvRecipeList.setAdapter(mAdapter);

        return rootView;
    }

}

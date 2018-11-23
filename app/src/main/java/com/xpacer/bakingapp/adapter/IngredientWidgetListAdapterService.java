package com.xpacer.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xpacer.bakingapp.R;
import com.xpacer.bakingapp.data.RecipeIngredient;
import com.xpacer.bakingapp.utils.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class IngredientWidgetListAdapterService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        ArrayList<RecipeIngredient> ingredientList = new ArrayList<>();
        if (intent.hasExtra(Constants.ARG_RECIPE_INGREDIENTS)) {
            //ingredientList = intent.getParcelableArrayListExtra(Constants.ARG_RECIPE_INGREDIENTS);
            String jsonText = intent.getStringExtra(Constants.ARG_RECIPE_INGREDIENTS);
            Gson gson = new Gson();
            Type ingredientListType = new TypeToken<ArrayList<RecipeIngredient>>() {
            }.getType();
            ingredientList = gson.fromJson(jsonText, ingredientListType);

        }

        return new IngredientListViewRemoteFactory(this.getApplicationContext(), ingredientList);
    }

    class IngredientListViewRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

        private ArrayList<RecipeIngredient> mIngredientList;
        private Context mContext;

        IngredientListViewRemoteFactory(Context context, ArrayList<RecipeIngredient> ingredientList) {
            mIngredientList = ingredientList;
            mContext = context;
        }


        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            mIngredientList.clear();
        }

        @Override
        public int getCount() {
            if (mIngredientList == null)
                return 0;

            return mIngredientList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews ingredientTextView = new RemoteViews(mContext.getPackageName(), R.layout.layout_item_list);
            RecipeIngredient ingredient = mIngredientList.get(i);
            ingredientTextView.setTextViewText(R.id.label, String.format(Locale.ENGLISH,
                    "%s %.1f %s", ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure()));
            return ingredientTextView;

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

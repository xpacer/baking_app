package com.xpacer.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.xpacer.bakingapp.R;
import com.xpacer.bakingapp.adapter.IngredientWidgetListAdapterService;
import com.xpacer.bakingapp.data.Recipe;
import com.xpacer.bakingapp.utils.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);

        if (recipe == null) {
            CharSequence widgetText = context.getString(R.string.recipe_ingredients);
            views.setTextViewText(R.id.widget_recipe_name, widgetText);
            views.setViewVisibility(R.id.tv_empty_state, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.tv_empty_state, View.GONE);
            views.setTextViewText(R.id.widget_recipe_name, recipe.getName().concat(" Ingredients"));

            Intent serviceIntent = new Intent(context, IngredientWidgetListAdapterService.class);
            serviceIntent.putExtra(Constants.ARG_RECIPE_INGREDIENTS, (new Gson()).toJson(recipe.getIngredients()));
            views.setRemoteAdapter(R.id.widget_ingredients_list, serviceIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager,
                                   int[] appWidgetIds, Recipe recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        BakingAppWidgetService.updateWidget(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}


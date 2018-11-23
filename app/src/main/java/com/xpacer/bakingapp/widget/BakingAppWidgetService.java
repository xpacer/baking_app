package com.xpacer.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.xpacer.bakingapp.data.Recipe;
import com.xpacer.bakingapp.utils.Prefs;

public class BakingAppWidgetService extends IntentService {

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    public static void updateWidget(Context context) {
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            handleWidgetUpdate(this);
        }
    }

    private void handleWidgetUpdate(final Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Recipe recipe = Prefs.getInstance(context).getCurrentRecipe();
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidgetProvider.class));
        BakingAppWidgetProvider.updateRecipeWidget(context, appWidgetManager, appWidgetIds, recipe);
    }
}

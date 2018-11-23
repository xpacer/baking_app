package com.xpacer.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xpacer.bakingapp.activity.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
public class SimpleActivityFlowTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static final String RECYCLER_VIEW_ITEM_NAME = "Nutella Pie";

    @Test
    public void clickItemInRecyclerView_OpensItemWithCorrectTitleBar() {

        onView(withId(R.id.rv_recipe_step))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(RECYCLER_VIEW_ITEM_NAME)),
                        click()));

        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(RECYCLER_VIEW_ITEM_NAME)));

    }

    @Test
    public void checkIfStepsRecyclerListIsDisplayed_OnRecipeActivity() {
        onView(withId(R.id.rv_recipe_step))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        onView(allOf(withId(R.id.rv_recipe_step_list), isDisplayed()));
    }

    @Test
    public void checkIfFirstItemInStepsRecyclerList_IsRecipeIngredients() {
        onView(withId(R.id.rv_recipe_step))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        onView(nthChildOf(nthChildOf(withId(R.id.rv_recipe_step_list), 0), 0))
                .check(matches(withText("Recipe Ingredients")));

    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }
}

package com.jidesh.erosnowjidesh.ui.main;

import android.view.View;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.jidesh.erosnowjidesh.ui.main.Activities.MainActivity;
import com.jidesh.erosnowjidesh.R;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;

public class PlaceholderFragmentTest {
@Rule
    public ActivityScenarioRule<MainActivity> activityScenario= new ActivityScenarioRule<MainActivity>(MainActivity.class);


    @Test
    public void Check_for_recyclerview() {
        onView(withId(R.id.main_recycler)).check(matches(isDisplayed()));
    }

    @Test
    public void SeeIfItemIsClicakble() {
        onView(withId(R.id.main_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
        onView(withId(R.id.languageTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void CheckIfBackButtonMakestherecyclerviewcolapse() {
        onView(withId(R.id.main_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
        onView(withId(R.id.languageTextView)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.main_recycler)).check(matches(isDisplayed()));

    }
    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }
    @Test
    public void PerformFavouritechangesthesource() {
        onView(withId(R.id.main_recycler)).check(matches(isDisplayed()));
        onView(withId(R.id.main_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0,MyViewAction.clickChildViewWithId(R.id. favourite)));


    }


}


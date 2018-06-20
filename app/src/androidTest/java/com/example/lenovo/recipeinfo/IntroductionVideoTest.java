package com.example.lenovo.recipeinfo;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IntroductionVideoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test

    public void introductionVideoTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.recipename), withText("Nutella Pie"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView.check(matches(withText("Nutella Pie")));
        appCompatTextView.perform(click());



        ViewInteraction textView2 = onView(
                allOf(withText("Ingredients"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Ingredients")));


       /* ViewInteraction textView3 = onView(
                allOf(withId(R.id.ingredient_View), withText("Graham Cracker crumbs\t\t2.0CUP\nunsalted butter, melted\t\t6.0TBLSP\ngranulated sugar\t\t0.5CUP\nsalt\t\t1.5TSP\nvanilla\t\t5.0TBLSP\nNutella or other chocolate-hazelnut spread\t\t1.0K\nMascapone Cheese(room temperature)\t\t500.0G\nheavy cream(cold)\t\t1.0CUP\ncream cheese(softened)\t\t4.0OZ\n"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Graham Cracker crumbs\t\t2.0CUP unsalted butter, melted\t\t6.0TBLSP granulated sugar\t\t0.5CUP salt\t\t1.5TSP vanilla\t\t5.0TBLSP Nutella or other chocolate-hazelnut spread\t\t1.0K Mascapone Cheese(room temperature)\t\t500.0G heavy cream(cold)\t\t1.0CUP cream cheese(softened)\t\t4.0OZ ")));
*/
        ViewInteraction textView4 = onView(
                allOf(withText("RecipeSteps"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                2),
                        isDisplayed()));
        textView4.check(matches(withText("RecipeSteps")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.id_text), withText("Recipe Introduction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipesteps_list),
                                        0),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Recipe Introduction")));


        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.id_text), withText("Recipe Introduction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipesteps_list),
                                        0),
                                0)));
        appCompatTextView2.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.exo_play), withContentDescription("Play"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

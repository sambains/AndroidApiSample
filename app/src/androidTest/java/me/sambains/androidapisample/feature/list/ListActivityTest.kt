package me.sambains.androidapisample.feature.list

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.sambains.androidapisample.R
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.base.BaseApplicationTest
import me.sambains.androidapisample.core.dependencies.AppComponentTest
import me.sambains.androidapisample.core.helpers.RecyclerViewItemCountAssertion
import me.sambains.androidapisample.core.helpers.RecyclerViewMatcher
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.core.models.TopRatedMovies
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import me.sambains.androidapisample.feature.detail.DetailActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.io.InputStreamReader
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class ListActivityTest {

    @get:Rule
    val listActivity: ActivityScenarioRule<ListActivity> = ActivityScenarioRule(
        ListActivity::class.java
    )

    @Inject
    lateinit var theMovieDbApi: TheMovieDbApi

    private lateinit var moviesExpectedResult: List<Movie>

    @Before
    fun setUp() {
        val listModule: ListModule = Mockito.mock(ListModule::class.java)
        BaseApplicationTest.getAppComponent()
            .plus(listModule)
            .inject(this)

        val schedulerProvider: SchedulerProvider = Mockito.mock(SchedulerProvider::class.java)

        val gson = Gson()
        val topRatedMoviesExpectedResponse: TopRatedMovies = gson.fromJson(
            InputStreamReader(
            InstrumentationRegistry.getInstrumentation().targetContext.assets
                .open("movies.json")
        ), object : TypeToken<TopRatedMovies>() {}.type
        )
        moviesExpectedResult = topRatedMoviesExpectedResponse.movies
        val movie = moviesExpectedResult[19]

        `when`(schedulerProvider.mainThread())
            .thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.backgroundThread())
            .thenReturn(Schedulers.trampoline())

        `when`(theMovieDbApi.getTopRatedMovies(anyString()))
            .thenReturn(Single.just(topRatedMoviesExpectedResponse))
        `when`(theMovieDbApi.getMovie(ArgumentMatchers.anyLong(), anyString()))
            .thenReturn(Single.just(movie))
    }

    @Test
    fun testRecyclerViewHasTheCorrectNumberOfItems() {
        //listActivity.launchActivity(Intent())
        onView(withId(R.id.recycler_view)).check(RecyclerViewItemCountAssertion(20))
    }

    @Test
    fun testRecyclerViewHasCorrectItemAtSpecificPosition() {
        //listActivity.launchActivity(Intent())
        onView(withId(R.id.recycler_view)).perform(scrollToPosition<RecyclerView.ViewHolder>(7))
        onView(RecyclerViewMatcher(R.id.recycler_view).atPosition(7))
            .check(matches(hasDescendant(withText("Avengers: Infinity War"))))
    }

    @Test
    fun testRecyclerViewClick() {
        Intents.init()
        //listActivity.launchActivity(Intent())
        onView(withId(R.id.recycler_view)).perform(scrollToPosition<RecyclerView.ViewHolder>(7))
        onView(RecyclerViewMatcher(R.id.recycler_view).atPosition(7)).perform(click())
        intended(hasComponent(DetailActivity::class.java.canonicalName))
        Intents.release()
    }

    @Test
    fun testSnackbarIsDisplayedWhenThereIsAnError() {
        val testThrowable = Throwable("Test error message!")
        `when`(theMovieDbApi.getTopRatedMovies(anyString()))
            .thenReturn(Single.error(testThrowable))
        //listActivity.launchActivity(Intent())
        onView(withText("Test error message!"))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}
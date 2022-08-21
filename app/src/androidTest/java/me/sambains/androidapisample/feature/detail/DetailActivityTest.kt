package me.sambains.androidapisample.feature.detail

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.sambains.androidapisample.R
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.base.BaseApplicationTest
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.io.InputStreamReader
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    //TODO Look into bug:
    //  Why don't the tests run by default in this new way
    //  They only run if you run the tests and then manually start the main app
    //  At that point, the tests pass
    /*@get:Rule
    val detailActivity: ActivityScenarioRule<DetailActivity> = ActivityScenarioRule(
        DetailActivity::class.java
    )*/

    @get:Rule
    var detailActivity: ActivityTestRule<DetailActivity> = ActivityTestRule(
        DetailActivity::class.java,
        true,
        false
    )

    @Inject
    lateinit var theMovieDbApi: TheMovieDbApi

    private lateinit var movie: Movie
    private lateinit var detailIntent: Intent

    @Before
    fun setUp() {
        val detailModule: DetailModule = mock(DetailModule::class.java)
        BaseApplicationTest
        BaseApplicationTest.getAppComponent()
            .plus(detailModule)
            .inject(this)

        val schedulerProvider: SchedulerProvider = mock(SchedulerProvider::class.java)

        val gson = Gson()
        movie = gson.fromJson(
            InputStreamReader(
                InstrumentationRegistry.getInstrumentation().context.assets
                    .open("movie.json")
            ), object : TypeToken<Movie>() {}.type
        )

        `when`(schedulerProvider.mainThread())
            .thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.backgroundThread())
            .thenReturn(Schedulers.trampoline())

        `when`(theMovieDbApi.getMovie(anyLong(), anyString()))
            .thenReturn(Single.just(movie))

        detailIntent = Intent()
        detailIntent.putExtra(DetailActivity.ARGS_MOVIE_ID, movie.id)
    }

    @Test
    fun testTitleIsCorrect() {
        detailActivity.launchActivity(detailIntent)
        onView(withId(R.id.title)).check(matches(withText("Avengers: Infinity War")))
    }

    @Test
    fun testTaglineIsCorrect() {
        detailActivity.launchActivity(detailIntent)
        onView(withId(R.id.tagline)).check(matches(withText("An entire universe. Once and for all.")))
    }

    @Test
    fun testOverviewIsCorrect() {
        detailActivity.launchActivity(detailIntent)
        onView(withId(R.id.overview)).check(matches(withText("As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.")))
    }

    @Test
    fun testRatingIsCorrect() {
        detailActivity.launchActivity(detailIntent)
        onView(withId(R.id.rating)).check(matches(withText("8.268")))
    }

    @Test
    fun testYearIsCorrect() {
        detailActivity.launchActivity(detailIntent)
        onView(withId(R.id.year)).check(matches(withText("2018-04-25")))
    }

    @Test
    fun testRuntimeIsCorrect() {
        detailActivity.launchActivity(detailIntent)
        onView(withId(R.id.runtime)).check(matches(withText("2 hrs 29 mins")))
    }

    @Test
    fun testSnackbarIsDisplayedWhenThereIsAnError() {
        val testThrowable = Throwable("Test error message!")
        `when`(theMovieDbApi.getMovie(anyLong(), anyString()))
            .thenReturn(Single.error(testThrowable))
        detailActivity.launchActivity(detailIntent)
        onView(withText("Oops! Something went wrong. Please try again."))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}
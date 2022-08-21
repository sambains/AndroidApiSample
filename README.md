# AndroidApiSample

This app uses The Movie Database (TMDB) API (https://developers.themoviedb.org/3) API.

The first screen in the app is the ListActivity and gets the Top Rated Movies and displays them in a RecyclerView.

Tapping on one of the Top Rated Movies opens the DetailActivity and fetches the data for the specific Movie from the API.

The app uses a Model View Presenter Interactor design pattern (MVPI).

There are both unit tests (using Junit) and Android UI tests (using Mockito) implemented.

There are a couple of TODOs in the project.

One is a future improvement where it would be good to test the response codes that allow caching.
The second if fixing a bug when migrating from ActivityTestRule to ActivityScenarioRule, where the test only runs when you manually run the app. Without this, the test hangs and fails until Android gives the error that it did not receive any Activity Lifecycle callbacks.

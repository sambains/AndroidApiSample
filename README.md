# AndroidApiSample

This app uses The Movie Database (TMDB) API (https://developers.themoviedb.org/3) API.

The first screen in the app is the ListActivity and gets the Top Rated Movies and displays them in a RecyclerView.

Tapping on one of the Top Rated Movies opens the DetailActivity and fetches the data for the specific Movie from the API.

The app uses a Model View Presenter Interactor design pattern (MVPI).

There are both unit tests (using Junit) and Android UI tests (using Mockito) implemented.

Please note, whilst the unit tests work, more time would be needed to get the Android UI tests working.

This is due to a knowledge migration from the old AndroidTestRule to the new ActivityScenarioRule implementations. That said, the test code has been written so my thoughts and my test implementation can still be seen when reading the code.

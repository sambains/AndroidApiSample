package me.sambains.androidapisample.feature.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import me.sambains.androidapisample.BuildConfig
import me.sambains.androidapisample.R
import me.sambains.androidapisample.core.base.BaseToolbarActivity
import me.sambains.androidapisample.core.dependencies.AppComponent
import me.sambains.androidapisample.core.helpers.TimeHelper
import me.sambains.androidapisample.core.helpers.ViewHelper
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.databinding.ActivityDetailBinding
import javax.inject.Inject


class DetailActivity : BaseToolbarActivity(), DetailContract.DetailView {

    @Inject
    lateinit var detailPresenter: DetailPresenter

    @Inject
    lateinit var viewHelper: ViewHelper

    @Inject
    lateinit var timeHelper: TimeHelper

    private lateinit var binding: ActivityDetailBinding
    private var movieId: Long = 0
    private var movieTitle: String? = null

    private val appBarLayoutOnOffsetChangedListener = object : AppBarLayout.OnOffsetChangedListener {

        var isShow = false
        var scrollRange = -1

        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }

            if (scrollRange + verticalOffset == 0) {
                binding.collapsingToolbarLayout.title = movieTitle
                isShow = true
            } else if (isShow) {
                binding.collapsingToolbarLayout.title = " "//There should a space between double quote otherwise it won't work
                isShow = false
            }
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(DetailModule(this))
            .inject(this)
    }

    override fun getToolbar(): Toolbar {
        return binding.toolbar
    }

    override val toolbarNavigationIcon: Int
        get() = R.drawable.ic_arrow_back

    override val toolbarTitle: String?
        get() = null

    override fun onNavigationClickListenerAction() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent == null || !intent.hasExtra(ARGS_MOVIE_ID)) {
            finish()
            return
        }

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.getLongExtra(ARGS_MOVIE_ID, -1)

        getMovie(movieId)

        binding.appBarLayout.addOnOffsetChangedListener(appBarLayoutOnOffsetChangedListener)
    }

    override fun onDestroy() {
        super.onDestroy()

        detailPresenter.detachView()
    }

    override fun getMovie(movieId: Long) {
        detailPresenter.getMovie(movieId)
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showMovie(movie: Movie) {
        movieTitle = movie.title

        binding.content.visibility = View.VISIBLE

        Glide.with(binding.heroImage)
            .load(BuildConfig.BACKDROP_IMAGE_URL + movie.backdropPath)
            .into(binding.heroImage)

        Glide.with(binding.poster)
            .load(BuildConfig.POSTER_IMAGE_URL + movie.posterPath)
            .into(binding.poster)

        binding.title.text = movie.title
        binding.tagline.text = movie.tagline
        binding.overview.text = movie.overview
        binding.rating.text = movie.voteAverage.toString()
        binding.year.text = movie.releaseDate

        val hours = timeHelper.getNumberOfHoursInMinutes(movie.runtime)
        val minutes = timeHelper.getNumberOfMinutesWithoutHours(movie.runtime)
        binding.runtime.text =
            getString(
                R.string.runtime, hours, resources.getQuantityString(R.plurals.hours, hours),
                minutes, resources.getQuantityString(R.plurals.minutes, minutes)
            )
    }

    override fun showError(errorMessage: String) {
        val snackbar = viewHelper.makeSnackbar(binding.coordinatorLayout, errorMessage, Snackbar.LENGTH_INDEFINITE, applicationContext)
        snackbar.setAction(R.string.error_message_default_action) {
            snackbar.dismiss()
            getMovie(movieId)
        }
        snackbar.show()
    }

    companion object {

        const val ARGS_MOVIE_ID = "args_movie_id"

        fun getIntent(context: Context, movieId: Long): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(ARGS_MOVIE_ID, movieId)

            return intent
        }

        fun startActivity(activity: FragmentActivity, movieId: Long) {
            ActivityCompat.startActivity(activity, getIntent(activity, movieId), null)
        }
    }
}
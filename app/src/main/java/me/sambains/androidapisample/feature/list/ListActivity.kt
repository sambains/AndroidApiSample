package me.sambains.androidapisample.feature.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.sambains.androidapisample.R
import me.sambains.androidapisample.core.base.BaseToolbarActivity
import me.sambains.androidapisample.core.dependencies.AppComponent
import me.sambains.androidapisample.core.helpers.ViewHelper
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.databinding.ActivityListBinding
import me.sambains.androidapisample.feature.detail.DetailActivity
import javax.inject.Inject

class ListActivity : BaseToolbarActivity(), ListContract.ListView, ListAdapter.MovieListListener {

    @Inject
    lateinit var listPresenter: ListPresenter

    @Inject
    lateinit var listAdapter: ListAdapter

    @Inject
    lateinit var viewHelper: ViewHelper

    private lateinit var binding: ActivityListBinding

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.plus(ListModule(this))
            .inject(this)
    }

    override fun getToolbar(): Toolbar {
        return binding.toolbar
    }

    override val toolbarNavigationIcon: Int
        get() = 0

    override val toolbarTitle: String
        get() = getString(R.string.app_name)

    override fun onNavigationClickListenerAction() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = listAdapter
        listAdapter.setMovieListListener(this)

        getMovies()
    }

    override fun onDestroy() {
        super.onDestroy()

        listPresenter.detachView()
    }

    override fun getMovies() {
        listPresenter.getMovies()
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showMovies(movies: List<Movie>) {
        listAdapter.updateMovies(movies)
    }

    override fun showError(errorMessage: String) {
        val snackbar = viewHelper.makeSnackbar(binding.coordinatorLayout, errorMessage, Snackbar.LENGTH_INDEFINITE, applicationContext)
        snackbar.setAction(R.string.error_message_default_action) {
            snackbar.dismiss()
            getMovies()
        }
        snackbar.show()
    }

    override fun openMovieDetail(movieId: Long) {
        DetailActivity.startActivity(this, movieId)
    }

    override fun onMovieClicked(movieId: Long) {
        openMovieDetail(movieId)
    }
}
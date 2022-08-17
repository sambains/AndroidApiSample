package me.sambains.androidapisample.feature.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.sambains.androidapisample.BuildConfig
import me.sambains.androidapisample.R
import me.sambains.androidapisample.core.base.BaseRecyclerViewAdapter
import me.sambains.androidapisample.core.dependencies.AppComponent
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.databinding.ListItemMovieBinding

class ListAdapter internal constructor(context: Context) : BaseRecyclerViewAdapter<ListAdapter.ListItemViewHolder>(), View.OnClickListener {

    private val margin = context.resources.getDimensionPixelOffset(R.dimen.margin_default)
    private var movies: MutableList<Movie> = ArrayList()
    private var movieListListener: MovieListListener? = null

    interface MovieListListener {
        fun onMovieClicked(movieId: Long)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val itemBinding = ListItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        itemBinding.cardView.setOnClickListener(this)
        return ListItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //Sets the margins of the cards manually so that only the first card has a margin at the top.
        //The other cards do not because all cards have a margin at the bottom
        layoutParams.setMargins(margin, if (position == 0) margin else 0, margin, margin)

        holder.itemView.layoutParams = layoutParams

        holder.bind(position, movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        movieListListener = null
    }

    override fun onClick(v: View?) {
        if (v != null) {
            val position = v.tag as? Int
            if (position != null && (movies.size > position))
                movieListListener?.onMovieClicked(movies[position].id)
        }
    }

    fun updateMovies(movies: List<Movie>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun setMovieListListener(movieListListener: MovieListListener) {
        this.movieListListener = movieListListener
    }

    inner class ListItemViewHolder(private val itemBinding: ListItemMovieBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int, movie: Movie) {
            itemBinding.cardView.tag = position
            Glide.with(itemBinding.thumbnail)
                .load(BuildConfig.POSTER_IMAGE_URL + movie.posterPath)
                .into(itemBinding.thumbnail)
            itemBinding.title.text = movie.title
            itemBinding.year.text = movie.releaseDate
            itemBinding.rating.text = movie.voteAverage.toString()
        }
    }
}
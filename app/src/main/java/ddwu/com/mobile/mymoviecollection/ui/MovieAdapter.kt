package ddwu.com.mobile.mymoviecollection.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.mymoviecollection.data.Movie
import ddwu.com.mobile.mymoviecollection.databinding.ListItemBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieHolder>(){
    private val TAG = "DetailActivity"

    var movies: List<Movie>? = null

    override fun getItemCount(): Int {
        return movies?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.itemBinding.krTitleTv.text = movies?.get(position)?.movieNm.toString()
        holder.itemBinding.enTitleTv.text = movies?.get(position)?.movieNmEn.toString()
        holder.itemBinding.openDtTv.text = movies?.get(position)?.openDt.toString()
        holder.itemBinding.eachMovie.setOnClickListener{
            clickListener?.onItemClick(it, position)
        }
    }

    class MovieHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    //onClick 구현
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }
}

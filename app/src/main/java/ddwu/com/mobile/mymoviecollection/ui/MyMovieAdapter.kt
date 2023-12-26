package ddwu.com.mobile.mymoviecollection.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.mymoviecollection.R
import ddwu.com.mobile.mymoviecollection.data.Movie
import ddwu.com.mobile.mymoviecollection.databinding.MylistItemBinding

class MyMovieAdapter(private val viewModel: MovieViewModel) : RecyclerView.Adapter<MyMovieAdapter.MyMovieHolder>(){
    private val TAG = "DetailActivity"

    var movies: List<Movie>? = null


    override fun getItemCount(): Int {
        return movies?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMovieHolder {
        val itemBinding = MylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyMovieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyMovieHolder, position: Int) {
        holder.itemBinding.krTitleTv.text = movies?.get(position)?.movieNm.toString()
        holder.itemBinding.enTitleTv.text = movies?.get(position)?.movieNmEn.toString()
        holder.itemBinding.todayDtTv.text = movies?.get(position)?.todayDt.toString()

        //DB의 isLiked 값에 따라 좋아요 버튼 다르게 설정
        if (movies?.get(position)?.isLiked!!){
            holder.itemBinding.likeBtn.setImageResource(R.drawable.ic_my_like_on)
        } else {
            holder.itemBinding.likeBtn.setImageResource(R.drawable.ic_my_like_off)
        }

        //좋아요 버튼 클릭시 이미지 변경
        holder.itemBinding.likeBtn.setOnClickListener {
            //isLiked 상태 true false 변경해주기
            val currentMovie = movies?.get(position)
            currentMovie?.isLiked = !currentMovie?.isLiked!!

            // isLiked 상태에 따라 이미지 변경
            val likeBtnImageResource =
                if (currentMovie.isLiked!!) {
                    R.drawable.ic_my_like_on }
                else {
                    R.drawable.ic_my_like_off
                }
            holder.itemBinding.likeBtn.setImageResource(likeBtnImageResource)

            viewModel.updateLike(
                currentMovie?.isLiked!!, holder.itemBinding.krTitleTv.text.toString()
            )
        }

        holder.itemBinding.eachMovie.setOnClickListener{
            clickListener?.onItemClick(it, position)
        }
    }

    class MyMovieHolder(val itemBinding: MylistItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    //onClick 구현
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }
}

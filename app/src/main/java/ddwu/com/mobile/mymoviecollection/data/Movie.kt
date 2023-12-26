package ddwu.com.mobile.mymoviecollection.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//코틀린은 컴파일하면서 알아서 클래스 나눠줘서 걍 같이 써도 됨
data class Root(
    val movieListResult: MovieListResult,
)

data class MovieListResult(
    val totCnt: Long,
    val source: String,
    @SerializedName("movieList")
    val movies: List<Movie>
)

@Entity(tableName = "movie_table")
data class Movie (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var movieNm: String?,
    var movieNmEn: String?,
    var openDt: String?,

    var myLocation: String?,
    var todayDt: String?,
    var content: String,
    var isLiked: Boolean?,
)



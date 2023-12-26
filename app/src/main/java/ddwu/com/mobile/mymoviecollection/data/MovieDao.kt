package ddwu.com.mobile.mymoviecollection.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie_table")
    fun getAllMovies() : Flow<List<Movie>>

    @Insert
    suspend fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movie_table WHERE movieNm = :movieName")
    suspend fun findMovie(movieName : String) : Movie

    @Query("UPDATE movie_table SET content = :content, todayDt = :todayDt  WHERE movieNm = :movieName")
    suspend fun updateMovie(content : String, todayDt : String, movieName: String)

    @Query("UPDATE movie_table SET isLiked = :isLiked WHERE movieNm = :movieName")
    suspend fun updateLike(isLiked : Boolean, movieName: String)

    @Query("DELETE FROM movie_table WHERE movieNm = :movieName")
    suspend fun deleteMovie(movieName : String)
}
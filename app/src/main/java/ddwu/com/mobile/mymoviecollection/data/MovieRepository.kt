package ddwu.com.mobile.mymoviecollection.data

import kotlinx.coroutines.flow.Flow


class MovieRepository (private val movieDao:MovieDao){
    val allMovies : Flow<List<Movie>> = movieDao.getAllMovies()

    suspend fun addMovie(movie : Movie) {  //Repository 의 addMovie 이용해서 MovieDao의 inserMovie 접근
       movieDao.insertMovie(movie) //MovieDao의 insertMovie가 suspend함수니까 얘를 사용하는 함수도 suspend여야한다
    }

    suspend fun findMovie(movieName : String) {
        movieDao.findMovie(movieName)
    }

    suspend fun updateMovie(content : String, todayDt : String, movieName: String) {
        movieDao.updateMovie(content, todayDt, movieName)
    }

    suspend fun updateLike(isLiked : Boolean, movieName: String) {
        movieDao.updateLike(isLiked, movieName)
    }

    suspend fun deleteMovie(movieName : String) {
        movieDao.deleteMovie(movieName)
    }
}
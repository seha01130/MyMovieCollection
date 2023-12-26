package ddwu.com.mobile.mymoviecollection

import android.app.Application
import ddwu.com.mobile.mymoviecollection.data.MovieDatabase
import ddwu.com.mobile.mymoviecollection.data.MovieRepository

class MovieApplication : Application() {  //애플리케이션이 만들어지면서 멤버변수가 두개 만들어지는데 애플리케이션
    // 만들어질때는 context 정보가 없으니까 만들수가 없는거죠 나중에 context 만들어지면 by lazy 때문에 그 때 이게 만들어지는거죠
    val moviedatabase : MovieDatabase by lazy {
        MovieDatabase.getDatabase(this)
    }

    val movieRepository : MovieRepository by lazy {
        MovieRepository(moviedatabase.movieDao())
    }
}
package ddwu.com.mobile.mymoviecollection.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ddwu.com.mobile.mymoviecollection.data.Movie
import ddwu.com.mobile.mymoviecollection.data.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel (var repository: MovieRepository): ViewModel() {
    var allMovies: LiveData<List<Movie>> = repository.allMovies.asLiveData() //Flow를 LiveData형식으로 바꿈
    //데이터가 바뀌었을때만 관찰하는 녀석이 LiveData

    fun addMovie(movie: Movie) = viewModelScope.launch {
        repository.addMovie(movie)
    }
    fun findMovie(movieName:String) = viewModelScope.launch {
        repository.findMovie(movieName)
    }
    fun updateMovie(content:String, todayDt: String, movieName : String) = viewModelScope.launch {
        repository.updateMovie(content, todayDt, movieName)
    }
    fun updateLike(isLiked:Boolean, movieName : String) = viewModelScope.launch {
        repository.updateLike(isLiked, movieName)
    }
    fun deleteMovie(movieName:String) = viewModelScope.launch {
        repository.deleteMovie(movieName)
    }
}
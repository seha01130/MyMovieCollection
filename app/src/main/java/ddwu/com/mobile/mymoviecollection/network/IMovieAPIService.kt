package ddwu.com.mobile.mymoviecollection.network

import ddwu.com.mobile.mymoviecollection.data.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMovieAPIService {
    @GET("kobisopenapi/webservice/rest/movie/searchMovieList.{type}")
    fun getMovieOffice(
        @Path("type") type: String,
        @Query("key") key: String,
        @Query ("movieNm") MovieTitle: String,
    )
    : Call<Root>
}


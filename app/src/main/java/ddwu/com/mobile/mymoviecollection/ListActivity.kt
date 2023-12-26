package ddwu.com.mobile.mymoviecollection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.mymoviecollection.data.Root
import ddwu.com.mobile.mymoviecollection.databinding.ActivityListBinding
import ddwu.com.mobile.mymoviecollection.network.IMovieAPIService
import ddwu.com.mobile.mymoviecollection.ui.MovieAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListActivity : AppCompatActivity() {
    private val TAG = "ListActivity"

    val listBinding by lazy {
        ActivityListBinding.inflate(layoutInflater)
    }
    lateinit var adapter : MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(listBinding.root)

        adapter = MovieAdapter()
        listBinding.rvMovies.adapter = adapter
        listBinding.rvMovies.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val krTitle = adapter.movies?.get(position)?.movieNm
                val enTitle = adapter.movies?.get(position)?.movieNmEn
                val openDate = adapter.movies?.get(position)?.openDt
                val myLocation = intent.getStringExtra("myLocation")
//                Log.d(TAG, "${openDate}")

                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("krTitle", krTitle)
                intent.putExtra("enTitle", enTitle)
                intent.putExtra("openDate", openDate)
                intent.putExtra("myLocation", myLocation)
                startActivity(intent)
            }
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.kobis_url))
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        val service = retrofit.create(IMovieAPIService::class.java)

        val movieTitle = intent.getStringExtra("title")

        val apiCallback = object: Callback<Root> {

            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                if (response.isSuccessful) {
                    val root : Root? = response.body()
                    Log.d(TAG, "응답 성공: ${root?.movieListResult?.movies}")
                    adapter.movies = root?.movieListResult?.movies
                    adapter.notifyDataSetChanged()

                } else {
                    Log.d(TAG, "응답 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Root>, t: Throwable) {
                Log.d(TAG, "OpenAPI 호출 실패: ${t.message}")
            }

        }

        val apiCall : Call<Root>
                = service.getMovieOffice(
            "json",
            resources.getString(R.string.kobis_key),
            movieTitle!! )

        apiCall.enqueue(apiCallback)
    }
}
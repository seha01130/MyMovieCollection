package ddwu.com.mobile.mymoviecollection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.mymoviecollection.databinding.ActivityMylistBinding
import ddwu.com.mobile.mymoviecollection.ui.MovieViewModel
import ddwu.com.mobile.mymoviecollection.ui.MovieViewModelFactory
import ddwu.com.mobile.mymoviecollection.ui.MyMovieAdapter

class MylistActivity : AppCompatActivity() {
    private val TAG = "MyListActivity"

    val myListBinding by lazy {
        ActivityMylistBinding.inflate(layoutInflater)
    }
    lateinit var adapter : MyMovieAdapter

    val viewModel : MovieViewModel by viewModels { //viewModel만든거죠 Activity내에서 viewModel 쓸 수 있는거죠 viewModels에 위임시켜서
        MovieViewModelFactory( (application as MovieApplication).movieRepository )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(myListBinding.root)

        adapter = MyMovieAdapter(viewModel)
        myListBinding.rvMyMovies.adapter = adapter
        myListBinding.rvMyMovies.layoutManager = LinearLayoutManager(this)

        viewModel.allMovies.observe(this, Observer {movies ->  //List<Food>가 바뀌면 adapter.foods에 새로운 데이터로 바꿔주겠죠
            adapter.movies = movies
            adapter.notifyDataSetChanged()
            Log.d(TAG, "Observing!!!")
        })

        myListBinding.homeBtn.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        // 해당 리스트 클릭시
        adapter.setOnItemClickListener(object : MyMovieAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val krTitle = adapter.movies?.get(position)?.movieNm
                val enTitle = adapter.movies?.get(position)?.movieNmEn
                val openDate = adapter.movies?.get(position)?.openDt
                val content = adapter.movies?.get(position)?.content
                val myLocation = adapter.movies?.get(position)?.myLocation



                val intent = Intent(applicationContext, UpdateActivity::class.java)
                intent.putExtra("krTitle", krTitle)
                intent.putExtra("enTitle", enTitle)
                intent.putExtra("openDate", openDate)
                intent.putExtra("content", content)
                intent.putExtra("myLocation", myLocation)
                startActivity(intent)
            }
        })

    }
}

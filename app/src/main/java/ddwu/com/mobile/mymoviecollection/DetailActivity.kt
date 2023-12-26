package ddwu.com.mobile.mymoviecollection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.mymoviecollection.data.Movie
import ddwu.com.mobile.mymoviecollection.ui.MovieViewModel
import ddwu.com.mobile.mymoviecollection.ui.MovieViewModelFactory
import ddwu.com.mobile.mymoviecollection.databinding.ActivityDetailBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailActivity : AppCompatActivity() {
    private val TAG = "DetailActivity"

    val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    val viewModel : MovieViewModel by viewModels { //viewModel만든거죠 Activity내에서 viewModel 쓸 수 있는거죠 viewModels에 위임시켜서
        MovieViewModelFactory( (application as MovieApplication).movieRepository )
        //여기에 repository 들어가야됨. Activity의 멤버변수로 만들어놨죠.
        // FoodApplication이라는걸 따로 만들었죠? 그래서 그걸로 타입캐스팅해줘야됨
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

        val timeStamp: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val krTitle = intent.getStringExtra("krTitle")
        val enTitle = intent.getStringExtra("enTitle")
        val openDate = intent.getStringExtra("openDate")
        val content = intent.getStringExtra("content")
        val myLocation = intent.getStringExtra("myLocation")
        Log.d(TAG, "${krTitle}${openDate}")

        //화면에 보여주기
        detailBinding.krTitle.text = krTitle
        detailBinding.enTitle.text = enTitle
        detailBinding.openDt.text = openDate
        detailBinding.contentET.setText(content)
        detailBinding.todayDt.text = timeStamp
        detailBinding.locationTV.text = myLocation
        
        //db저장
        detailBinding.createBtn.setOnClickListener {
            val result = viewModel.findMovie(
                krTitle ?: "none"
            )

            viewModel.addMovie(
                Movie(0, krTitle, enTitle, openDate, myLocation, timeStamp, detailBinding.contentET.text.toString(), false)
            )

            val intent = Intent(applicationContext, MylistActivity::class.java)
            startActivity(intent)
        }
    }
}
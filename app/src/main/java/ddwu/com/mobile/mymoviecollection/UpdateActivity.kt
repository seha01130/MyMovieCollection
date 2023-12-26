package ddwu.com.mobile.mymoviecollection

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.mymoviecollection.databinding.ActivityUpdateBinding
import ddwu.com.mobile.mymoviecollection.ui.MovieViewModel
import ddwu.com.mobile.mymoviecollection.ui.MovieViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UpdateActivity : AppCompatActivity() {
    private val TAG = "UpdateActivity"

    val updateBinding by lazy {
        ActivityUpdateBinding.inflate(layoutInflater)
    }

    val viewModel : MovieViewModel by viewModels {
        MovieViewModelFactory( (application as MovieApplication).movieRepository )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(updateBinding.root)

        val timeStamp: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val krTitle = intent.getStringExtra("krTitle")
        val enTitle = intent.getStringExtra("enTitle")
        val openDate = intent.getStringExtra("openDate")
        val content = intent.getStringExtra("content")
        val myLocation = intent.getStringExtra("myLocation")
//        Log.d(TAG, "${krTitle}${openDate}"

        //화면에 보여주기
        updateBinding.krTitle.text = krTitle
        updateBinding.enTitle.text = enTitle
        updateBinding.openDt.text = openDate
        updateBinding.contentET.setText(content)
        updateBinding.todayDt1.text = timeStamp
        updateBinding.locationTV.text = myLocation

        //db 수정, 저장
        updateBinding.updateBtn.setOnClickListener {
            viewModel.updateMovie(
                updateBinding.contentET.text.toString(), timeStamp, krTitle ?: "none"
            )

            val intent = Intent(applicationContext, MylistActivity::class.java)
            startActivity(intent)
        }

        //db 삭제
        updateBinding.deleteBtn.setOnClickListener {
            viewModel.deleteMovie(
                krTitle ?: "none"
            )

            val intent = Intent(applicationContext, MylistActivity::class.java)
            startActivity(intent)
        }
    }
}
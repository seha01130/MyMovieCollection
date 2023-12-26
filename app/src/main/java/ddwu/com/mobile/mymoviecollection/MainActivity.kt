package ddwu.com.mobile.mymoviecollection

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ddwu.com.mobile.mymoviecollection.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    lateinit var mainBinding : ActivityMainBinding

    //지도 부분
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var geocoder : Geocoder
    private lateinit var currentLoc : Location

    private lateinit var googleMap : GoogleMap
    var centerMarker : Marker? = null

    private var myLocation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.myListBtn.setOnClickListener{
            val intent = Intent(applicationContext, MylistActivity::class.java)
            startActivity(intent)
        }

        mainBinding.btnSearch.setOnClickListener {
            var movieTitle = mainBinding.movieTitleEt.text.toString()

            mainBinding.tvData.setText("현재 위치 버튼을 클릭하세요")
            centerMarker?.remove()
            
            val intent = Intent(applicationContext, ListActivity::class.java)
            intent.putExtra("title", movieTitle)
            intent.putExtra("myLocation", myLocation)
            startActivity(intent)
        }

//        val url = resources.getString(R.string.image_url)
//        Glide.with(this)
//            .load(url)
//            .into(mainBinding.imageView)


        //지도부분
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())

        checkPermissions()

        mainBinding.btnLocStart.setOnClickListener {
            startLocUpdates()
            addMarker(LatLng(37.606320, 127.041808))
        }

        mainBinding.btnLocStop.setOnClickListener {
            fusedLocationClient.removeLocationUpdates(locCallback)
            showData(myLocation)
        }

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment

        mapFragment.getMapAsync (mapReadyCallback)
    }

    //지도부분
    /*GoogleMap 로딩이 완료될 경우 실행하는 Callback*/
    val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            Log.d(TAG, "GoogleMap is ready")

            googleMap.setOnMarkerClickListener { marker ->
                Toast.makeText(this@MainActivity, "현재 위치입니다", Toast.LENGTH_SHORT).show()
                false //이벤트 처리 안끝났다  infoWindow뜨게 해주려고. 이벤트가 계속 전달될 수 잇게 window뜨는 곳까지
            }

            googleMap.setOnInfoWindowClickListener { marker ->
                Toast.makeText(this@MainActivity, marker.title, Toast.LENGTH_SHORT).show()
            }

        }
    }

    /*마커 추가*/
    fun addMarker(targetLoc: LatLng) {  // LatLng(37.606320, 127.041808)
        val markerOptions: MarkerOptions = MarkerOptions()
        markerOptions.position(targetLoc)
            .title("현재 위치")
            .snippet("맞다면 선택 클릭")
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker))

        centerMarker = googleMap.addMarker(markerOptions)
        centerMarker?.showInfoWindow()
        centerMarker?.tag = "database_id"

    }

    /*위치 정보 수신 시 수행할 동작을 정의하는 Callback*/
    val locCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locResult: LocationResult) {
            currentLoc = locResult.locations.get(0)

            //minSdk 33일때
//            geocoder.getFromLocation(currentLoc.latitude, currentLoc.longitude, 5) { addresses ->
//                CoroutineScope(Dispatchers.Main).launch {
////                    showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
//                    showData(addresses?.get(0)?.getAddressLine(0).toString())
//                }
//            }
            //minSdk 27일때
            CoroutineScope(Dispatchers.Main).launch {
                val addresses = geocoder.getFromLocation(currentLoc.latitude, currentLoc.longitude, 5)
//                showData(addresses?.get(0)?.getAddressLine(0).toString())
                Log.d(TAG, "fromLocation CoroutineScope ${addresses?.get(0)?.getAddressLine(0).toString()}")

                myLocation = addresses?.get(0)?.getAddressLine(0).toString()
            }

            val targetLoc: LatLng = LatLng(currentLoc.latitude, currentLoc.longitude)

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))
            centerMarker?.position = targetLoc
        }
    }

    /*위치 정보 수신 설정*/
    val locRequest = LocationRequest.Builder(5000)
        .setMinUpdateIntervalMillis(3000)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()

    /*위치 정보 수신 시작*/
    private fun startLocUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locRequest,     // LocationRequest 객체
            locCallback,    // LocationCallback 객체
            Looper.getMainLooper()  // System 메시지 수신 Looper
        )
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locCallback)
    }

    private fun showData(data : String) {
        mainBinding.tvData.setText(mainBinding.tvData.text.toString() + "\n${data}")
    }

    fun checkPermissions () {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            showData("현재 위치 버튼을 클릭하세요")  // textView에 출력
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    /*registerForActivityResult 는 startActivityForResult() 대체*/
    val locationPermissionRequest
            = registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions() ) {
            permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                showData("현재 위치 버튼을 클릭하세요")
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                showData("현재 위치 버튼을 클릭하세요")
            }
            else -> {
                showData("위치를 가져올 수 없습니다")
            }
        }
    }
}

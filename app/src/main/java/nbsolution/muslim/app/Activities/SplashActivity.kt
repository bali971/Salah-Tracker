package nbsolution.muslim.app.Activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import nbsolution.muslim.app.Dashboard
import nbsolution.muslim.app.Helper.GpsTracker
import nbsolution.muslim.app.SharedData.SharedClass
import nbsolution.muslim.app.databinding.ActivitySplashBinding
import nbsolution.muslim.app.utils.DialogUtils
import nbsolution.muslim.app.utils.GeneralUtils
import nbsolution.muslim.app.utils.PermissionUtils
import nbsolution.muslim.app.utils.PrefsUtils
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var dialog: DialogUtils
    private lateinit var binding: ActivitySplashBinding
    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    var locationManager: LocationManager? = null
    var localBroadcastManager: LocalBroadcastManager? = null
    lateinit var prefs: PrefsUtils
    var mFirebaseAnalytics: FirebaseAnalytics? = null
    // private val THEME_KEY = "Theme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this)
        prefs = PrefsUtils(this)
        val dialogPermission = SharedClass.getDialogPermission(this, "isDialogPermission")
        val permission = SharedClass.getPermission(this, "isPermissionGranted")

        FirebaseApp.initializeApp(this)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager!!.registerReceiver(onNotice, IntentFilter("notice"))
        dialog = DialogUtils(this, layoutInflater)
        dialog.showProgressDialog()
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager?

//        if(SharedClass.getLocationDetails(this, "country").equals("null")) {
//            binding.btnProceed.visibility = View.GONE
//            PermissionUtils.checkPermissions(this)
        // }

        binding.btnProceed.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Install App")
            mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)
            if (permission) {
                PermissionUtils.checkPermissions(this)
                binding.btnProceed.visibility = View.GONE
            } else if (!permission && !dialogPermission) {
                PermissionUtils.checkPermissions(this)
                binding.btnProceed.visibility = View.GONE
            } else if (!permission && dialogPermission) {
                val gpsService = GpsTracker(this@SplashActivity)
                val intent = Intent(this, gpsService.javaClass)
                if (!GeneralUtils.isMyServiceRunning(this, gpsService.javaClass)) {
                    startService(intent)
                }
            }
        }

//        binding.btnProceed.setOnClickListener {
//            if (permission) {
//                val dashboardIntent = Intent(this, Dashboard::class.java)
//                startActivity(dashboardIntent)
//                finish()
//            } else {
//                PermissionUtils.checkPermissions(this)
//                binding.btnProceed.visibility = View.GONE
//            }
//        }
        // }
    }

    override fun onPause() {
        super.onPause()
        dialog.hideProgressDialog()
    }

    public override fun onResume() {
        super.onResume()
        // Getting GPS status
        isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isGPSEnabled && isNetworkEnabled) {
            dialog.hideProgressDialog()
        } else {
            dialog.hideProgressDialog()
            GeneralUtils.showSettingsAlert(this)
        }
    }

    private val onNotice: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context?,
                intent: Intent,
            ) {
                // intent can contain anydata
                val lat = intent.getDoubleExtra("lat", 0.0)
                val longt = intent.getDoubleExtra("longt", 0.0)
                setCityDetails(longt, lat)
            }
        }

    fun setCityDetails(
        longitude: Double,
        latitude: Double,
    ) {
        val context: Context = this@SplashActivity
        val geocoder = Geocoder(context, Locale.getDefault())
        if (latitude != 0.0 && longitude != 0.0) {
            val addresses = geocoder.getFromLocation(latitude, longitude, 5)
            if (addresses != null && !addresses.isEmpty()) {
                val address = addresses[0]
                val cityName = address.locality
                val countryName = address.countryName
                SharedClass.setLocationFlag(
                    this,
                    countryName,
                    cityName,
                    latitude.toString(),
                    longitude.toString(),
                    1,
                )

                val dashboardIntent = Intent(this, Dashboard::class.java)
                startActivity(dashboardIntent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Location not found, Please restart the application.",
                    Toast.LENGTH_LONG,
                ).show()
            }
        } else {
            PermissionUtils.checkPermissions(context)
        }
    }
}

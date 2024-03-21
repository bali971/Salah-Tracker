package nbsolution.muslim.app.Activities

import nbsolution.muslim.app.Dashboard
import nbsolution.muslim.app.SharedData.SharedClass
import nbsolution.muslim.app.databinding.ActivitySplashBinding
import nbsolution.muslim.app.utils.DialogUtils
import nbsolution.muslim.app.utils.GeneralUtils
import nbsolution.muslim.app.utils.PermissionUtils
import nbsolution.muslim.app.utils.PrefsUtils
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.ads.MobileAds
import java.io.IOException
import java.util.Locale


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var dialog: DialogUtils
    private lateinit var binding: ActivitySplashBinding
    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private var locationManager: LocationManager? = null
    var localBroadcastManager: LocalBroadcastManager? = null
    lateinit var prefs: PrefsUtils
    // private val THEME_KEY = "Theme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this)
        prefs = PrefsUtils(this)

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager!!.registerReceiver(onNotice, IntentFilter("notice"));
        dialog = DialogUtils(this, layoutInflater)
        dialog.showProgressDialog()
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager?

        if(SharedClass.getLocationDetails(this, "country").equals("null")) {
            binding.btnProceed.visibility = View.GONE
            PermissionUtils.checkPermissions(this)
        } else {
            binding.btnProceed.setOnClickListener {
                PermissionUtils.checkPermissions(this)
                binding.btnProceed.visibility = View.GONE
            }
        }

    }

    override fun onPause() {
        super.onPause()
        dialog.hideProgressDialog()
    }

    override fun onResume() {
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

    private val onNotice: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // intent can contain anydata
            val lat = intent.getDoubleExtra("lat", 0.0)
            val longt = intent.getDoubleExtra("longt", 0.0)
            setCityDetails(longt, lat);
        }
    }

    @Throws(IOException::class)
    fun setCityDetails(longitude: Double, latitude: Double) {
        val context: Context = this@SplashActivity
        val geocoder = Geocoder(context, Locale.getDefault())
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
                1
            )

            val dashboardIntent = Intent(this, Dashboard::class.java)
             startActivity(dashboardIntent)
            finish()
        }
    }
}

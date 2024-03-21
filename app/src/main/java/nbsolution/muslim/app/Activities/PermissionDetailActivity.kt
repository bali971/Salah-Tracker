package nbsolution.muslim.app.Activities

import nbsolution.muslim.app.Dashboard
import nbsolution.muslim.app.Helper.GpsTracker
import nbsolution.muslim.app.databinding.ActivityPermissionDetailBinding
import nbsolution.muslim.app.utils.GeneralUtils
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX

@SuppressLint("CustomPermissionScreen")
class PermissionDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionDetailBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGrant.setOnClickListener {
            grantPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun grantPermissions() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.SCHEDULE_EXACT_ALARM,
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK",
                    "Cancel",
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()

                    val gpsService = GpsTracker(this@PermissionDetailActivity)
                    val intent = Intent(this, gpsService.javaClass)
                    if (!GeneralUtils.isMyServiceRunning(this, gpsService.javaClass)) {
                        startService(intent)
                    }

                    val dashboardIntent = Intent(this, Dashboard::class.java)
                   // startActivity(dashboardIntent)
                    finish()
                } else {
                    grantPermissions()
                }
            }
    }
}

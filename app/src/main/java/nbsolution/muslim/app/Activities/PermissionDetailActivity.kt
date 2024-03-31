package nbsolution.muslim.app.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import nbsolution.muslim.app.Dashboard
import nbsolution.muslim.app.Helper.GpsTracker
import nbsolution.muslim.app.SharedData.SharedClass
import nbsolution.muslim.app.databinding.ActivityPermissionDetailBinding
import nbsolution.muslim.app.fragment.InputDialogFragment
import nbsolution.muslim.app.utils.GeneralUtils

@SuppressLint("CustomPermissionScreen")
class PermissionDetailActivity : AppCompatActivity(), InputDialogFragment.InputDialogListener {
    private lateinit var binding: ActivityPermissionDetailBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGrant.setOnClickListener {
            grantPermissions()
        }
        binding.btnManually.setOnClickListener {
            val dialogFragment = InputDialogFragment()
            dialogFragment.show(supportFragmentManager, "InputDialogFragment")
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
                    SharedClass.setPermission(this, true)
                    SharedClass.setDialogPermission(this, false)
                    SharedClass.firstTime(this, true)

                    val dashboardIntent = Intent(this, Dashboard::class.java)
                    // startActivity(dashboardIntent)
                    finish()
                } else {
                    grantPermissions()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun grantPermissionsForDialog() {
        PermissionX.init(this)
            .permissions(
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
                    grantPermissionsForDialog()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onSaveClicked(
        city: String?,
        country: String?,
    ) {
        SharedClass.setPermission(this, false)
        SharedClass.setDialogPermission(this, true)
        SharedClass.firstTime(this, true)
        SharedClass.setManualLocation(this, city, country)
        grantPermissionsForDialog()
    }
}

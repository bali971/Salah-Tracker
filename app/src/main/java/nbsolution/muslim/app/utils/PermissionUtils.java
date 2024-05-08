package nbsolution.muslim.app.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import nbsolution.muslim.app.Activities.PermissionDetailActivity;
import nbsolution.muslim.app.Dashboard;
import nbsolution.muslim.app.Helper.GpsTracker;

public class PermissionUtils {

    public static void checkPermissions(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Intent permissionIntent = new Intent(context, PermissionDetailActivity.class);
            context.startActivity(permissionIntent);
            // ((AppCompatActivity)context).finish();
        } else {
            GpsTracker gpsService = new GpsTracker(context);
            Intent intent = new Intent(context, gpsService.getClass());
            if (!GeneralUtils.isMyServiceRunning(context, gpsService.getClass())) {
                context.startService(intent);
            }

            Intent dashboardIntent = new Intent(context, Dashboard.class);
        //    context.startActivity(dashboardIntent);
            //  ((AppCompatActivity)context).finish();
        }
    }

}
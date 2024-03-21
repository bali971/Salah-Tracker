package nbsolution.muslim.app.prayer

import nbsolution.muslim.app.Constants.TIME_12
import nbsolution.muslim.app.scheduler.PrayAlarmReceiver
import nbsolution.muslim.app.utils.PermissionUtils
import nbsolution.muslim.app.utils.PrefsUtils
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PrayerViewModel(val appContext: Application) : AndroidViewModel(appContext) {
    private var utils: PrefsUtils = PrefsUtils(appContext)

    fun onNextButtonClick() {
        PermissionUtils.checkPermissions(appContext)
    }

    fun checktimingsbefore(
        time: String,
        endtime: String,
    ): Boolean {
        val sdf = SimpleDateFormat(TIME_12)
        try {
            val date1 = sdf.parse(time)
            val date2 = sdf.parse(endtime)
            return date1.before(date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    fun checktimingsafter(
        time: String,
        endtime: String,
    ): Boolean {
        val sdf = SimpleDateFormat(TIME_12)
        try {
            val date1 = sdf.parse(time)
            val date2 = sdf.parse(endtime)
            return date1.after(date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    fun getCurrentTime(): String {
        val simpleDateFormat = SimpleDateFormat(TIME_12)
        return simpleDateFormat.format(Calendar.getInstance().time).lowercase(Locale.getDefault())
    }

    fun updateAlarmStatus() {
        val prayerAlarmReceiver = PrayAlarmReceiver()
        prayerAlarmReceiver.cancelAlarm(appContext)
        prayerAlarmReceiver.setAlarm(appContext)
    }
}

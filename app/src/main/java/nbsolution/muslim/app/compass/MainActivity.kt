package nbsolution.muslim.app.compass

import nbsolution.muslim.app.Constants
import nbsolution.muslim.app.Helper.CalculateQibla
import nbsolution.muslim.app.R
import nbsolution.muslim.app.base.BaseActivity
import nbsolution.muslim.app.databinding.ActivityCompassBinding
import nbsolution.muslim.app.pref.ConfigPreferences
import nbsolution.muslim.app.utils.GeneralUtils
import nbsolution.muslim.app.utils.PrefsUtils
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : BaseActivity(), SensorEventListener {
    private var supported: Boolean? = null
    private var qiblaDegree = 0.0
    private lateinit var utils: PrefsUtils

    @Suppress("ktlint:standard:property-naming")
    private val TAG = MainActivity::class.java.name
    private lateinit var binding: ActivityCompassBinding
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    private val currentDegree = 0f
    private lateinit var viewModel: MainViewModel
    private lateinit var factory: MainViewModelFactory
    private var mLastAccelerometer = FloatArray(3)
    private var mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private var pointerPosition = true
    private var start = false
    private val mR = FloatArray(9)
    private val mOrientation = FloatArray(3)
    private var previousAzimuthInDegrees = 0.0
    private var lastRoll = 0.0
    private var lastPitch = 0.0
    private var lastTime = 0.0

    override fun observeViewModel() {
        // No observers yet
    }

    override fun initViewBinding() {
        binding = ActivityCompassBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        val view = binding.root
        binding.viewModel = viewModel
        setContentView(view)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun setupViewModel() {
        factory = MainViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_QiblaCompassWhite)
        super.onCreate(savedInstanceState)

        init()

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            // Success! There's a magnetometer.
            binding.ifnosensortxt.visibility = View.GONE
            binding.indicator.visibility = View.VISIBLE
            binding.baseimg.visibility = View.VISIBLE
            binding.notetxt.visibility = View.VISIBLE
        } else {
            // Failure! No magnetometer.
            binding.ifnosensortxt.visibility = View.VISIBLE
            binding.indicator.visibility = View.GONE
            binding.baseimg.visibility = View.GONE
            binding.notetxt.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (isSupported()) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
            mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSupported()) {
            mSensorManager.unregisterListener(this, mAccelerometer)
            mSensorManager.unregisterListener(this, mMagnetometer)
        }
    }

    // 40.71288818872338, -73.30130108772184
    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        utils = PrefsUtils(this)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        qiblaDegree =
            CalculateQibla.calculateQibla(
//                utils.getFromPrefsWithDefault(Constants.LATITUDE, 40.71288818872338),
//                utils.getFromPrefsWithDefault(Constants.LONGITUDE, -73.30130108772184),
                40.71288818872338,
                -73.30130108772184,
            )
        utils.saveToPrefs(Constants.QUIBLA_DEGREE, qiblaDegree)
        qiblaDegree = utils.getFromPrefsWithDefault(ConfigPreferences.QUIBLA_DEGREE, 0.0)
        // println("qiblaDegree")
        Log.d("qiblaDegree", qiblaDegree.toString())
        mSensorManager = this.getSystemService(SENSOR_SERVICE) as SensorManager

        Log.d("qiblaDegree", isSupported().toString())
        if (isSupported()) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!

            // animate compass pointer
            val ra =
                RotateAnimation(
                    currentDegree,
                    qiblaDegree.toFloat(),
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                )
            ra.duration = 200
            ra.fillAfter = true
            binding.poinerInner.startAnimation(ra)
        }

        binding.btnNav.setOnClickListener {
            GeneralUtils.optionsDrawerFlag = -1
        }
    }

    fun isSupported(): Boolean {
        mSensorManager =
            this.getSystemService(SENSOR_SERVICE) as SensorManager
        val sensors: List<Sensor> =
            mSensorManager.getSensorList(
                Sensor.TYPE_MAGNETIC_FIELD,
            )
        supported = sensors.size > 0
        return supported as Boolean
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val startTime = System.currentTimeMillis().toDouble()

        if (event!!.sensor == mAccelerometer) {
            mLastAccelerometer = event.values
            mLastAccelerometerSet = true
        } else if (event.sensor == mMagnetometer) {
            mLastMagnetometer = event.values
            mLastMagnetometerSet = true
        }

        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            val success =
                SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer)
            SensorManager.getOrientation(mR, mOrientation)
            val azimuthInRadians: Float = mOrientation.get(0)
            var azimuthInDegress =
                ((-(Math.toDegrees(azimuthInRadians.toDouble()) + 360)).toFloat() % 360).toDouble()

            val north =
                RotateAnimation(
                    currentDegree,
                    azimuthInDegress.toFloat(),
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                )
            north.duration = 200
            north.fillAfter = true
            binding.indicator.startAnimation(north)

            if (Math.abs(azimuthInDegress - previousAzimuthInDegrees) > 300) {
                previousAzimuthInDegrees = azimuthInDegress
            }
            azimuthInDegress =
                viewModel.lowPass(azimuthInDegress, previousAzimuthInDegrees, startTime)
            val ra =
                RotateAnimation(
                    previousAzimuthInDegrees.toFloat(),
                    azimuthInDegress.toFloat(),
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                )
            ra.duration = 500
            ra.fillAfter = true
            binding.compassContainer.startAnimation(ra)
            binding.innerplace.startAnimation(ra)
            previousAzimuthInDegrees = azimuthInDegress
            if (pointerPosition == true) {
                pointerPosition = false
            }
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(mR, orientation)
                var pitch = (orientation[1] * 57.2957795f).toDouble()
                var roll = (orientation[2] * 57.2957795f).toDouble()
                if (pitch > 90) pitch -= 180.0
                if (pitch < -90) pitch += 180.0
                if (roll > 90) roll -= 180.0
                if (roll < -90) roll += 180.0
                val time = System.currentTimeMillis().toDouble()
                if (!start) {
                    lastTime = time
                    lastRoll = roll
                    lastPitch = pitch
                }
                start = true
                val dt: Double = (time - lastTime) / 1000.0
                roll = viewModel.lowPassPointerLevel(roll, lastRoll, dt)
                pitch = viewModel.lowPassPointerLevel(pitch, lastPitch, dt)
                lastTime = time
                lastRoll = roll
                lastPitch = pitch
                binding.accelerometerView.sensorValue.setRotation(
                    azimuthInRadians,
                    roll.toFloat(),
                    pitch.toFloat(),
                )
            }
        }
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int,
    ) {
    }

    companion object {
        @JvmStatic
        fun startMe(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            (context as AppCompatActivity).finish()
        }
    }
}

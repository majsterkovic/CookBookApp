package put.poznan.cookbook

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import kotlin.math.sqrt


class AnimationFragment : Fragment(), SensorEventListener {


    private var mSceneView: View? = null
    private var mCloudView: View? = null
    private var mTitleView: View? = null
    private var mMeatBallView: View? = null

    private lateinit var sensorManager: SensorManager
    private var accelerometerSensor: Sensor? = null
    private var lastShakeTime: Long = 0L
    private val shakeThreshold = 15

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animation, container, false)
        mSceneView = view
        mSceneView!!.setOnClickListener { startAnimation() }
        mCloudView = view.findViewById(R.id.clouds)
        mTitleView = view.findViewById(R.id.title)
        mMeatBallView = view.findViewById(R.id.meatball)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
                sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }

        return view
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastShakeTime) > 1000) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
                if (acceleration > shakeThreshold) {
                    lastShakeTime = currentTime
                    startAnimationAlternative()
                }
            }
        }
    }


    private fun startAnimation() {
        val cloudsYStart = mCloudView!!.top.toFloat()
        val cloudsYEnd = mSceneView!!.top.toFloat()
        val heightAnimator =
            ObjectAnimator.ofFloat(mCloudView, View.Y, cloudsYStart, cloudsYEnd).setDuration(4000)

        val cloudsXStart = mCloudView!!.left.toFloat()
        val cloudsXEnd = mCloudView!!.right.toFloat()
        val widthAnimator =
            ObjectAnimator.ofFloat(mCloudView, View.X, cloudsXStart, cloudsXEnd - 100.0f)
                .setDuration(4000)

        val opacityAnimator =
            ObjectAnimator.ofFloat(mCloudView, View.ALPHA, 1f, 0f).setDuration(3000)
        val titleOpacityAnimator =
            ObjectAnimator.ofFloat(mTitleView, View.ALPHA, 0f, 1f).setDuration(3000)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            heightAnimator,
            widthAnimator,
            opacityAnimator,
            titleOpacityAnimator
        )
        animatorSet.start()
        animatorSet.doOnEnd {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun startAnimationAlternative() {

        val meatBallYStart = mMeatBallView!!.top.toFloat()
        val meatBallYEnd = mMeatBallView!!.bottom.toFloat() - 250.0f

        val meatBallHeightAnimator =
            ObjectAnimator.ofFloat(mMeatBallView, View.Y, meatBallYStart, meatBallYEnd)
                .setDuration(2000)
        meatBallHeightAnimator.interpolator = AccelerateInterpolator(2.0f)
        val preMeatBallFadeAnimator =
            ObjectAnimator.ofFloat(mMeatBallView, View.ALPHA, 0f, 1f).setDuration(125)
        val meatBallFadeAnimator =
            ObjectAnimator.ofFloat(mMeatBallView, View.ALPHA, 1f, 0f).setDuration(125)
        meatBallFadeAnimator.startDelay = 1855

        val cloudsYStart = mCloudView!!.top.toFloat()
        val cloudsYEnd = mSceneView!!.top.toFloat()
        val heightAnimator =
            ObjectAnimator.ofFloat(mCloudView, View.Y, cloudsYStart, cloudsYEnd).setDuration(4000)

        val cloudsXStart = mCloudView!!.left.toFloat()
        val cloudsXEnd = mCloudView!!.right.toFloat()
        val widthAnimator =
            ObjectAnimator.ofFloat(mCloudView, View.X, cloudsXStart, cloudsXEnd - 100.0f)
                .setDuration(4000)

        val opacityAnimator =
            ObjectAnimator.ofFloat(mCloudView, View.ALPHA, 1f, 0f).setDuration(3000)
        val titleOpacityAnimator =
            ObjectAnimator.ofFloat(mTitleView, View.ALPHA, 0f, 1f).setDuration(3000)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            preMeatBallFadeAnimator,
            meatBallHeightAnimator,
            meatBallFadeAnimator,
            heightAnimator,
            widthAnimator,
            opacityAnimator,
            titleOpacityAnimator
        )
        animatorSet.start()
        animatorSet.doOnEnd {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }


}
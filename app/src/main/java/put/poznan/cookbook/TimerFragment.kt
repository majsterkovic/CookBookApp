package put.poznan.cookbook

import android.content.Context
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment


//
class TimerFragment : Fragment(), View.OnClickListener {
    private var seconds: Int = 0
    private var running = false
    private var wasRunning: Boolean? = null
    private var startSeconds: Int? = null
    private var playing = false
    private lateinit var stopButton: Button
    private lateinit var timeView: TextView
    private lateinit var ringtone: Ringtone
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        } else {
            startSeconds = arguments?.getInt("seconds") ?: 0
            seconds = startSeconds as Int
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : View = inflater.inflate(R.layout.fragment_timer, container, false)
        runTimer(layout)

        val startButton = layout.findViewById(R.id.start_button) as Button
        startButton.setOnClickListener(this)
        stopButton = layout.findViewById(R.id.stop_button) as Button
        stopButton.setOnClickListener(this)
        timeView = layout.findViewById(R.id.time_view) as TextView
        return layout
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning == true) {
            running = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning!!)
    }

    private fun onClickStart() {
        running = true
        stopButton.text = getString(R.string.interrupt)
        stopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_media_stop, 0, 0, 0)
    }

    private fun onClickStop() {
        if (playing) {
            ringtone.stop()
        }
        running = false
        playing = false
        timeView.text = startSeconds?.let { getDisplayTime(it) }
        seconds = startSeconds!!
    }


    private fun getDisplayTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%d:%02d:%02d", hours, minutes, secs)
    }

    private fun runTimer(view: View) {
        val handler = Handler()
        audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        var alert: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        ringtone = RingtoneManager.getRingtone(context, alert)
        ringtone.isLooping = true
        ringtone.volume = 0.0f

        handler.post(object : Runnable {
            override fun run() {
                if (seconds >= 0) {
                    timeView.text = getDisplayTime(seconds)
                    if (running) {
                        seconds--
                    }
                } else {
                    if (playing) {
                        if (ringtone.volume <= 0.92f) {
                            ringtone.volume += 0.08f
                        }
                    } else {
                        if (running) {
                            stopButton.text = getString(R.string.stop)
                            stopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_media_rew, 0, 0, 0)
                            ringtone.play()
                            playing = true
                        }
                    }
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.start_button -> onClickStart()
            R.id.stop_button -> onClickStop()
        }
    }
}
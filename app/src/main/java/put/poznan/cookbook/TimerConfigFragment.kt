package put.poznan.cookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class TimerConfigFragment : Fragment(), View.OnClickListener {

    private lateinit var hourSpinner : Spinner
    private lateinit var minuteSpinner : Spinner
    private lateinit var secondSpinner : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val hours = (0..9).toList()
        val minutes = (0..59).toList()
        val seconds = (0..59).toList()

        val t = inflater.inflate(R.layout.fragment_timer_config, container, false)

        hourSpinner = t.findViewById(R.id.hourSpinner)
        minuteSpinner = t.findViewById(R.id.minuteSpinner)
        secondSpinner = t.findViewById(R.id.secondSpinner)
        hourSpinner.adapter = ArrayAdapter(
            requireActivity().applicationContext,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            hours
        )
        minuteSpinner.adapter = ArrayAdapter(
            requireActivity().applicationContext,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            minutes
        )
        secondSpinner.adapter = ArrayAdapter(
            requireActivity().applicationContext,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            seconds
        )

        val button = t.findViewById<Button>(R.id.button)
        button.setOnClickListener(this)

        return t
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button -> onClickAdd()
        }
    }

    fun onClickAdd() {

        val s = secondSpinner.selectedItem as Int
        val m = minuteSpinner.selectedItem as Int
        val h = hourSpinner.selectedItem as Int

        if (s != 0 || m != 0 || h != 0) {
            val seconds = h * 3600 + m * 60 + s
            val timer = TimerFragment()
            val args = Bundle()
            args.putInt("seconds", seconds)
            timer.arguments = args
            val ft: FragmentTransaction = childFragmentManager.beginTransaction()
            ft.add(R.id.timer_container, timer)
            ft.addToBackStack(null)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        }
    }

}
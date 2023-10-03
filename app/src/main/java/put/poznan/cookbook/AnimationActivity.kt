package put.poznan.cookbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AnimationActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        return AnimationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        lifecycleScope.launch {
            Dish.loadDesertsFromFile() as MutableList<Dish>
        }

        lifecycleScope.launch {
            Dish.loadDishesFromFile() as MutableList<Dish>
        }

    }
}
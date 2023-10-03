package put.poznan.cookbook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DishDetailFragment : Fragment() {

    private var dishId: Long? = null
    private var dishType: Int? = null

    private lateinit var servingsNumberMinus: Button
    private lateinit var servingsNumberPlus: Button
    private lateinit var servingsTextView: TextView
    private lateinit var caloriesTextView: TextView
    private lateinit var ingredientsTextView: TextView
    private lateinit var servingsNumberPickerTextView: TextView

    private lateinit var recipes: List<Dish>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val ft: FragmentTransaction = childFragmentManager.beginTransaction()
            ft.add(R.id.timer_config_container, TimerConfigFragment())
            ft.addToBackStack(null)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        } else {
            dishId = savedInstanceState.getLong("dishId")
            dishType = savedInstanceState.getInt("dishType")
            recipes = if (dishType == 0) Dish.dishes else Dish.deserts
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dish_detail, container, false)
        servingsNumberMinus = view.findViewById(R.id.decrementButton) as Button
        servingsNumberPlus = view.findViewById(R.id.incrementButton) as Button

        servingsTextView = view.findViewById(R.id.textServings) as TextView
        caloriesTextView = view.findViewById(R.id.textCalories) as TextView
        ingredientsTextView = view.findViewById(R.id.textIngredients) as TextView
        servingsNumberPickerTextView =
            view.findViewById(R.id.servingsNumberPickerTextView) as TextView
        val servingsNumber = null

        servingsNumberMinus.setOnClickListener {
            if (servingsNumberPickerTextView.text.toString().toInt() == 1) {
                return@setOnClickListener
            }
            val newServingsVal = servingsNumberPickerTextView.text.toString().toInt() - 1
            servingsNumberPickerTextView.text = newServingsVal.toString()
            updateServings(newServingsVal)
            updateCalories(newServingsVal)
            updateIngridients(newServingsVal)
        }

        servingsNumberPlus.setOnClickListener {
            if (servingsNumberPickerTextView.text.toString().toInt() == 10) {
                return@setOnClickListener
            }
            val newServingsVal = servingsNumberPickerTextView.text.toString().toInt() + 1
            servingsNumberPickerTextView.text = newServingsVal.toString()
            updateServings(newServingsVal)
            updateCalories(newServingsVal)
            updateIngridients(newServingsVal)
        }

        //dodawanie FAB do fragmentu w wersji na tablety
        val fab = view.findViewById<FloatingActionButton>(R.id.FAButton)
        if (fab != null) {
            fab.setOnClickListener {
                floatingActionButtonListener()
            }
        }


        return view
    }

    private fun updateServings(newServingsVal: Int) {
        val view = view
        if (view != null) {
            servingsTextView.text = "Liczba porcji: " + newServingsVal.toString()
        }
    }

    private fun updateCalories(newServingsVal: Int) {
        val view = view
        if (view != null) {
            val dish = recipes[dishId!!.toInt()]
            caloriesTextView.text =
                "Kalorie: " + (dish.caloriesPerServing * newServingsVal).toString()
        }
    }

    private fun updateIngridients(newServingsVal: Int) {
        val view = view
        if (view != null) {
            val dish = recipes[dishId!!.toInt()]
            val newIngredientsList = dish.calculateIngredientsForServings(newServingsVal)
            val ingredientsText = StringBuilder()
            for (ingredient in newIngredientsList) {
                val ingredientText = "${ingredient.first}: ${ingredient.second}"
                ingredientsText.append(ingredientText).append("\n")
            }
            ingredientsTextView.text = ingredientsText.toString()
        }
    }


    override fun onStart() {
        super.onStart()
        recipes = if (dishType == 0) Dish.dishes else Dish.deserts

        val view = view
        if (view != null) {
            val title = view.findViewById(R.id.textTitle) as TextView
            val dish = recipes[dishId!!.toInt()]
            title.text = dish.name

            val description = view.findViewById(R.id.textDescription) as TextView
            description.text = dish.description


            val ingredientsText = StringBuilder()
            for (ingredient in dish.ingredients) {
                val ingredientText = "${ingredient.first}: ${ingredient.second}"
                ingredientsText.append(ingredientText).append("\n")
            }
            ingredientsTextView.text = ingredientsText.toString()

            caloriesTextView.text = "Kalorie: " + dish.calculateCalories().toString()
            servingsTextView.text = "Liczba porcji: " + dish.numberOfServings.toString()
            servingsNumberPickerTextView.text = dish.numberOfServings.toString()

            val instructionsTextView = view.findViewById(R.id.textInstructions) as TextView
            instructionsTextView.text = dish.instructions
        }
    }

    private fun floatingActionButtonListener() {
        val ingredients = recipes[dishId!!.toInt()].ingredients
        val textToSend = StringBuilder()
        for (ingredient in ingredients) {
            val ingredientText = "${ingredient.first}: ${ingredient.second}"
            textToSend.append(ingredientText).append("\n")
        }
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToSend.toString())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Prześlij listę składników")
        startActivity(shareIntent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        dishId?.let { outState.putLong("dishId", it) }
        dishType?.let { outState.putInt("dishType", it) }
    }

    fun setDishId(id: Long) {
        dishId = id
    }

    fun setDishType(type: Int) {
        dishType = type
    }


}
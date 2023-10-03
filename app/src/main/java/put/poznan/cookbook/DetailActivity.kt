package put.poznan.cookbook

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailActivity : AppCompatActivity() {

    private var dishId: Long? = null
    private var dishType: Int? = null
    private lateinit var recipes: List<Dish>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val frag = supportFragmentManager.findFragmentById((R.id.detail_frag)) as DishDetailFragment
        dishId = intent.extras!!.getInt(EXTRA_DISH_ID).toLong()
        dishType = intent.extras!!.getInt(EXTRA_DISH_TYPE)
        recipes = if (dishType == 0) Dish.dishes else Dish.deserts
        frag.setDishId(dishId!!)
        frag.setDishType(dishType!!)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fab = findViewById<FloatingActionButton>(R.id.FAButton)
        fab.setOnClickListener { floatingActionButtonListener() }


        val image = findViewById<ImageView>(R.id.detail_image)
        val imageUrl = recipes[dishId!!.toInt()].imageUrl

        Glide.with(this)
            .load(imageUrl)
            .into(image)
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



    companion object {
        const val EXTRA_DISH_ID = "id"
        const val EXTRA_DISH_TYPE = "type"
    }
}
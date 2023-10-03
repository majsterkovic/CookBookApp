package put.poznan.cookbook

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class Dish(
    val name: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<Pair<String, Int>>,
    val numberOfServings: Int,
    val caloriesPerServing: Int,
    val instructions: String
) {
    fun calculateIngredientsForServings(servings: Int): List<Pair<String, Int>> {
        val scaledIngredients = mutableListOf<Pair<String, Int>>()
        val scaleRatio = servings.toDouble() / numberOfServings.toDouble()

        for (ingredient in ingredients) {
            val scaledAmount = (ingredient.second * scaleRatio).toInt()
            scaledIngredients.add(Pair(ingredient.first, scaledAmount))
        }
        return scaledIngredients
    }

    fun calculateCalories(): Int {
        return caloriesPerServing * numberOfServings
    }

    companion object {
        val TYPE_DISH = 0
        val TYPE_DESERT = 1

        var dishes = mutableListOf<Dish>()
        var deserts = mutableListOf<Dish>()
        suspend fun loadDishesFromFile(): List<Dish> = withContext(Dispatchers.IO) {

            val url = URL("http://mariusz.hybiak.student.put.poznan.pl/cookbook/dishes.json")
            val connection = url.openConnection()

            val inputStream = connection.getInputStream()
            val jsonText = inputStream.bufferedReader().use { it.readText() }

            val dishesJsonArray = JSONArray(jsonText)

            for (i in 0 until dishesJsonArray.length()) {
                val dishJsonObject = dishesJsonArray.getJSONObject(i)
                val name = dishJsonObject.getString("name")
                val description = dishJsonObject.getString("description")
                val imageUrl = dishJsonObject.getString("imageResourceId")
                val numberOfServings = dishJsonObject.getInt("numberOfServings")
                val caloriesPerServing = dishJsonObject.getInt("caloriesPerServing")

                val ingredientsJsonArray = dishJsonObject.getJSONArray("ingredients")
                val ingredients = mutableListOf<Pair<String, Int>>()
                for (j in 0 until ingredientsJsonArray.length()) {
                    val ingredientJsonObject = ingredientsJsonArray.getJSONObject(j)
                    val ingredientName = ingredientJsonObject.getString("first")
                    val ingredientAmount = ingredientJsonObject.getInt("second")
                    ingredients.add(Pair(ingredientName, ingredientAmount))
                }

                val instructions = dishJsonObject.getString("instructions")

                val dish = Dish(
                    name,
                    description,
                    imageUrl,
                    ingredients,
                    numberOfServings,
                    caloriesPerServing,
                    instructions
                )
                dishes.add(dish)
            }
            dishes
        }

        suspend fun loadDesertsFromFile(): List<Dish> = withContext(Dispatchers.IO) {

            val url = URL("http://mariusz.hybiak.student.put.poznan.pl/cookbook/desserts.json")
            val connection = url.openConnection()

            val inputStream = connection.getInputStream()
            val jsonText = inputStream.bufferedReader().use { it.readText() }

            val dishesJsonArray = JSONArray(jsonText)

            for (i in 0 until dishesJsonArray.length()) {
                val dishJsonObject = dishesJsonArray.getJSONObject(i)
                val name = dishJsonObject.getString("name")
                val description = dishJsonObject.getString("description")
                val imageUrl = dishJsonObject.getString("imageResourceId")
                val numberOfServings = dishJsonObject.getInt("numberOfServings")
                val caloriesPerServing = dishJsonObject.getInt("caloriesPerServing")

                val ingredientsJsonArray = dishJsonObject.getJSONArray("ingredients")
                val ingredients = mutableListOf<Pair<String, Int>>()
                for (j in 0 until ingredientsJsonArray.length()) {
                    val ingredientJsonObject = ingredientsJsonArray.getJSONObject(j)
                    val ingredientName = ingredientJsonObject.getString("first")
                    val ingredientAmount = ingredientJsonObject.getInt("second")
                    ingredients.add(Pair(ingredientName, ingredientAmount))
                }
                val instructions = dishJsonObject.getString("instructions")

                val dish = Dish(
                    name,
                    description,
                    imageUrl,
                    ingredients,
                    numberOfServings,
                    caloriesPerServing,
                    instructions
                )
                deserts.add(dish)
            }
            deserts
        }
    }
}

fun getDishesWithIngredient(ingredient: String): List<Dish> {
    val recipes = Dish.dishes + Dish.deserts
    return recipes.filter { dish ->
        dish.ingredients.any { it.first.contains(ingredient, ignoreCase = true) }
    }
}
package put.poznan.cookbook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class TabFragment() : Fragment() {


    private var type: Int? = null
    private lateinit var recipes: List<Dish>
    private var query: String? = null

    constructor(type: Int) : this() {
        this.type = type
    }

    constructor(query: String) : this() {
        this.query = query
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (query != null) {
            recipes = getDishesWithIngredient(query.toString())
        } else {
            recipes = if (type == 0) Dish.dishes else Dish.deserts
        }
        val dishRecycler = inflater.inflate(R.layout.fragment_tab, container, false) as RecyclerView
        val dishNames = recipes.map { it.name }
        val dishUrls = recipes.map { it.imageUrl }
        val adapter = CaptionedImagesAdapter(dishNames, dishUrls)
        dishRecycler.adapter = adapter
        val layoutManager = GridLayoutManager(activity, 2)
        dishRecycler.layoutManager = layoutManager
        adapter.setListener(object : CaptionedImagesAdapter.Listener {
            override fun onClick(position: Int) {
                val fragmentContainer = requireActivity().findViewById<View>(R.id.fragment_container_large)
                var id: Int
                if (query != null) {
                    id = Dish.dishes.indexOf(recipes[position])
                    type = Dish.TYPE_DISH
                    if (id == -1) {
                        id = Dish.deserts.indexOf(recipes[position])
                        type = Dish.TYPE_DESERT
                    }
                } else {
                    id = position
                }
                if (fragmentContainer != null) {
                    val details = DishDetailFragment()
                    val fragmentTransaction = parentFragmentManager.beginTransaction()
                    details.setDishId(id.toLong())
                    details.setDishType(type!!) //the first tab contains info, and category dishes is 0, so we subtract 1
                    fragmentTransaction.replace(R.id.fragment_container_large, details)
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } else {
                    val intent = Intent(activity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DISH_ID, id)
                    intent.putExtra(DetailActivity.EXTRA_DISH_TYPE, type)
                    activity!!.startActivity(intent)
                }
            }
        })
        return dishRecycler
    }

}
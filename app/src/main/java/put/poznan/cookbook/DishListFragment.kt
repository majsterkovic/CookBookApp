package put.poznan.cookbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment

class DishListFragment : ListFragment() {


    interface Listener {
        fun itemClicked(id: Long)
    }

    private lateinit var listener: Listener


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        listener.itemClicked(id)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val names = ArrayList<String>()
        for (d in Dish.dishes) {
            names.add(d.name)
        }
        val adapter = ArrayAdapter(inflater.context, android.R.layout.simple_list_item_1, names)
        listAdapter = adapter
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}
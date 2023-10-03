package put.poznan.cookbook

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class MainActivity : AppCompatActivity(), DishListFragment.Listener,
    NavigationView.OnNavigationItemSelectedListener {

    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        //create listener that will open the menu after clicking hamburger
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        //set listener for selecting drawer menu option
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)


        val pagerAdapter = SectionsPagerAdapter(supportFragmentManager, this)
        val pager = findViewById<ViewPager>(R.id.pager)
        pager.adapter = pagerAdapter
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(pager)
        //add listener to get selected tab position so that we know which category was chosen
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                position = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        val layout = findViewById<LinearLayout>(R.id.search_query_container)
        val viewPager = findViewById<ViewPager>(R.id.pager)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.search_query_container, TabFragment(query))
                transaction.commit()

                layout.isVisible = true
                viewPager.isVisible = false

                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isEmpty()) {
                    layout.isVisible = false
                    viewPager.isVisible = true
                }
                return true
            }

        })



        return super.onCreateOptionsMenu(menu)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        var fragment = findViewById<View>(R.id.fragment_container_large)
        var intent: Intent? = null
        when (id) {
            R.id.drawer_random -> {
                val randomType = (0..1).random()
                val recipes = if (randomType == 0) Dish.dishes else Dish.deserts
                val randomRecipe = (recipes.indices).random()
                if (fragment != null) {
                    val details = DishDetailFragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    details.setDishId(randomRecipe.toLong())
                    details.setDishType(randomType) //the first tab contains info, and category dishes is 0, so we subtract 1
                    fragmentTransaction.replace(R.id.fragment_container_large, details)
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } else {
                    intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DISH_ID, randomRecipe)
                    intent.putExtra(DetailActivity.EXTRA_DISH_TYPE, randomType)
                    startActivity(intent)
                }
            }

        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }

    override fun itemClicked(id: Long) {
        val fragmentContainer = findViewById<View>(R.id.fragment_container_large)
        if (fragmentContainer != null) {
            val details = DishDetailFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            details.setDishId(id)
            details.setDishType(position - 1) //the first tab contains info, and category dishes is 0, so we subtract 1
            fragmentTransaction.replace(R.id.fragment_container_large, details)
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DISH_ID, id.toInt())
            intent.putExtra(DetailActivity.EXTRA_DISH_TYPE, position - 1)
            startActivity(intent)
        }

    }

}
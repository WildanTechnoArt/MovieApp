package com.wildantechnoart.movieapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.wildantechnoart.movieapp.R
import com.wildantechnoart.movieapp.databinding.ActivityMainBinding
import com.wildantechnoart.movieapp.utils.ViewBindingExt.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = getNavController()
        navController?.let { controller ->
            appBarConfiguration = AppBarConfiguration(controller.graph)
            setupActionBarWithNavController(controller, appBarConfiguration)

//            controller.addOnDestinationChangedListener { _, destination, _ ->
//                when (destination.id) {
//                    R.id.genresFragment -> {
//                        binding.appBarLayout.visibility = View.VISIBLE
//                    }
//                    else -> {
//                        binding.appBarLayout.visibility = View.GONE
//                    }
//                }
//            }
        }
    }

    private fun getNavController(): NavController? {
        val fragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home)
        if (fragment !is NavHostFragment) {
            throw IllegalStateException(
                "Activity " + this
                        + " does not have a NavHostFragment"
            )
        }
        return (fragment as NavHostFragment?)?.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        return appBarConfiguration.let { navController.navigateUp(it) } || super.onSupportNavigateUp()
    }
}
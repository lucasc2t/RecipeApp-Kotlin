package my.com.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import my.com.recipeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){

                R.id.nav_home -> nav.navigate(R.id.homeFragment)
                R.id.nav_insert -> nav.navigate(R.id.insertRecipeFragment)
                R.id.nav_category -> nav.navigate(R.id.categoryFragment)
                R.id.nav_profile -> nav.navigate(R.id.userProfileFragment)
            }
            true
        }

    }
//    override fun onBackPressed(){
//        val num = nav.currentDestination?.label
//        when(num){
////            "fragment_home" -> super.finish()
////            "fragment_step" -> super.finish()
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}
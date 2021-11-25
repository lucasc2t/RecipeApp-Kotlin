package my.com.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.recipeapp.R
import my.com.recipeapp.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private val nav by lazy{ findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        btn.visibility = View.VISIBLE

        binding.rltvBreakfast.setOnClickListener{nav.navigate(R.id.categoryDetailFragment, bundleOf("category" to "Breakfast"))}
        binding.rltvLunch.setOnClickListener{nav.navigate(R.id.categoryDetailFragment, bundleOf("category" to "Lunch"))}
        binding.rltvDinner.setOnClickListener{nav.navigate(R.id.categoryDetailFragment, bundleOf("category" to "Dinner"))}
        binding.rltvDesserts.setOnClickListener{nav.navigate(R.id.categoryDetailFragment, bundleOf("category" to "Desserts"))}
        binding.rltvDrinks.setOnClickListener{nav.navigate(R.id.categoryDetailFragment, bundleOf("category" to "Drinks"))}

        return binding.root
    }

}
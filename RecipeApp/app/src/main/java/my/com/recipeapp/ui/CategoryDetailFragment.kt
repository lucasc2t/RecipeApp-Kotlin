package my.com.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.recipeapp.Data.RecipeViewModel
import my.com.recipeapp.R
import my.com.recipeapp.databinding.FragmentCategoryDetailBinding
import my.com.recipeapp.util.IngredientsAdapter
import my.com.recipeapp.util.RecipeAdapter

class CategoryDetailFragment : Fragment() {

    private lateinit var binding : FragmentCategoryDetailBinding
    private val nav by lazy{ findNavController() }
    private val vmR : RecipeViewModel by activityViewModels()
    private val category by lazy { requireArguments().getString("category", "N/A") }

    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentCategoryDetailBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        btn.visibility = View.GONE

        vmR.search("")
        vmR.getAll()
        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vmR.search(name)
                return true
            }
        })

        adapter = RecipeAdapter(){ holder, recipe ->
            holder.root.setOnClickListener { nav.navigate(R.id.recipeDetailFragment, bundleOf("id" to recipe.recipeId)) }
        }
        binding.rv.adapter = adapter

        vmR.getResult().observe(viewLifecycleOwner){list ->

            var recipeArray = list.filter{ r ->
                r.recipeType == category && r.recipeServingNo >= 1
            }
            adapter.submitList(recipeArray)
        }

        return binding.root
    }
}
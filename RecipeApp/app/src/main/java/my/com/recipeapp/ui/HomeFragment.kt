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
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.recipeapp.R
import my.com.recipeapp.Data.RecipeViewModel
import my.com.recipeapp.databinding.FragmentHomeBinding
import my.com.recipeapp.util.RecipeAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val nav by lazy{ findNavController() }
    private val vmR : RecipeViewModel by activityViewModels()
//    private val vmU : UserViewModel by activityViewModels()

    private lateinit var  adapter : RecipeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        vmR.search("")
        vmR.getAll()
        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                vmR.search(name)
                return true
            }
        })

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        btn.visibility = View.VISIBLE

        adapter = RecipeAdapter() { holder,  recipe->
            holder.root.setOnClickListener { nav.navigate(R.id.recipeDetailFragment, bundleOf("id" to recipe.recipeId)) }
        }

        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmR.getResult().observe(viewLifecycleOwner){list ->

            adapter.submitList(list)
        }

        return binding.root
    }

}
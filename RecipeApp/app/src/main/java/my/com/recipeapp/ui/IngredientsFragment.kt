package my.com.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.com.recipeapp.Data.Ingredient
import my.com.recipeapp.Data.IngredientsViewModel
import my.com.recipeapp.Data.RecipeViewModel
import my.com.recipeapp.R
import my.com.recipeapp.databinding.FragmentIngredientsBinding
import my.com.recipeapp.databinding.FragmentInsertRecipeBinding
import my.com.recipeapp.util.*


class IngredientsFragment : Fragment() {

    private lateinit var binding: FragmentIngredientsBinding
    private val nav by lazy{ findNavController() }
    private val vmI : IngredientsViewModel by activityViewModels()
    private val vmR : RecipeViewModel by activityViewModels()
    private val id by lazy { requireArguments().getString("id", "N/A") }

    private lateinit var  adapter : IngredientsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        btn.visibility = View.GONE
        binding.btnToSteps.isClickable = false

        vmI.getAll()
        vmR.getAll()

        binding.btnAdd.setOnClickListener { addIngredients() }

        adapter = IngredientsAdapter() { holder,  ingredient->

            holder.root.setOnClickListener { nav.navigate(R.id.categoryFragment) }
        }

        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmI.getResult().observe(viewLifecycleOwner){list ->

            var ingredientArray = list.filter{ i ->
                i.ingreRecipeId == id
            }
            if(ingredientArray.isNotEmpty()){
                binding.btnToSteps.isClickable = true
                binding.btnToSteps.setOnClickListener { nav.navigate(R.id.stepsFragment, bundleOf("id" to id)) }
            }
            adapter.submitList(ingredientArray)
        }

        return binding.root
    }

    private fun addIngredients() {

        val i = Ingredient(
            ingreRecipeId = id,
            ingredientName = binding.edtIngredientName.editableText.toString().trim(),
            ingredientAmount = binding.edtIngredientAmount.editableText.toString().trim(),
        )
        val err = vmI.validate(i)
        if(err != ""){
            errorDialog(err)
            return
        }else{
            Firebase.firestore.collection("ingredients").document().set(i)
            informationDialog("Ingredient Added")
            hideKeyboard()
            binding.edtIngredientAmount.setText("")
            binding.edtIngredientName.setText("")
            binding.edtIngredientName.requestFocus()
//            nav.navigate(R.id.stepsFragment, bundleOf("id" to id))
        }
    }

}
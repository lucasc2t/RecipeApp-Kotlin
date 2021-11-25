package my.com.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.recipeapp.Data.IngredientsViewModel
import my.com.recipeapp.Data.RecipeViewModel
import my.com.recipeapp.Data.StepViewModel
import my.com.recipeapp.R
import my.com.recipeapp.databinding.FragmentRecipeDetailBinding
import my.com.recipeapp.util.IngredientsAdapter
import my.com.recipeapp.util.StepAdapter
import my.com.recipeapp.util.toBitmap

class RecipeDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecipeDetailBinding
    private val vmR : RecipeViewModel by activityViewModels()
    private val vmS : StepViewModel by activityViewModels()
    private val vmI : IngredientsViewModel by activityViewModels()
    private val nav by lazy{ findNavController() }
    private lateinit var ingreAdapter: IngredientsAdapter
    private lateinit var stepAdapter : StepAdapter

    private val id by lazy { requireArguments().getString("id", "N/A") }
    private var ingre = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        btn.visibility = View.GONE

        recipeDetail()
        val i = vmI.get(id)
        if (i != null) {
            ingre = i.ingredientId
        }
        binding.imgEditRecipe.setOnClickListener { nav.navigate(R.id.updateRecipeFragment, bundleOf("id" to id)) }
        binding.imgEditIngredient.setOnClickListener { nav.navigate(R.id.listIngredientFragment, bundleOf("id" to id, "ingre" to ingre )) }
        binding.imgEditStep.setOnClickListener { nav.navigate(R.id.listStepFragment, bundleOf("id" to id)) }

        // Ingredient RV
        ingreAdapter = IngredientsAdapter(){ holder, ingredient ->
            holder.root.isClickable = false
            holder.root.setOnClickListener {  }

        }
        binding.ingredientRV.adapter = ingreAdapter

        vmI.getResult().observe(viewLifecycleOwner){list ->
            var ingredientArray = list.filter{ i ->
                i.ingreRecipeId == id
            }
            ingreAdapter.submitList(ingredientArray)
        }
        // Step RV
        stepAdapter = StepAdapter(){ holder, step ->
            holder.root.setOnClickListener {  }
        }
        binding.stepRV.adapter = stepAdapter

        vmS.getAll().observe(viewLifecycleOwner){list ->
            var l = list.sortedBy { it.stepNo }

            var stepArray =l.filter{ s ->
                s.stepRecipeId == id
            }
            stepAdapter.submitList(stepArray)
        }
        return binding.root
    }

    private fun recipeDetail() {
        val r = vmR.get(id)

        if(r != null){
            binding.txtRecipeName.setText(r.recipeName).toString()
            binding.txtDescription.setText(r.recipeDescription).toString()
            binding.txtRecipeType.setText(r.recipeType).toString()
            binding.txtPreparationTime.setText(r.recipePreparationTime).toString()
            binding.imgRecipePhoto.setImageBitmap(r.recipeImage.toBitmap())
            binding.txtServingNo.text = r.recipeServingNo.toString()
        }

    }

}


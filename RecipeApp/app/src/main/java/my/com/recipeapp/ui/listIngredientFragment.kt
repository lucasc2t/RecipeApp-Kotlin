package my.com.recipeapp.ui

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import my.com.recipeapp.Data.Ingredient
import my.com.recipeapp.Data.IngredientsViewModel
import my.com.recipeapp.R
import my.com.recipeapp.databinding.FragmentListIngredientBinding
import my.com.recipeapp.util.IngredientsAdapter
import my.com.recipeapp.util.errorDialog
import my.com.recipeapp.util.snackbar

class listIngredientFragment : Fragment() {

    private lateinit var binding: FragmentListIngredientBinding
    private val nav by lazy { findNavController()}
    private val vmI: IngredientsViewModel by activityViewModels()

    private lateinit var adapter: IngredientsAdapter

    private val id by lazy { requireArguments().getString("id", "N/A") }
    private val ingre by lazy { requireArguments().getString("ingre", "N/A") }

    private var ingR = ""
    private var ing = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vmI.getAll()

        binding = FragmentListIngredientBinding.inflate(inflater,container, false)
        binding.btnUpdateIngre.setOnClickListener { update() }
        binding.btnAddIngre.setOnClickListener { add() }
        binding.btnUpdateIngre.isClickable = false

        adapter = IngredientsAdapter(){holder, ingredient ->

            holder.btnDelete.visibility = View.VISIBLE
            holder.btnEdit.visibility = View.VISIBLE

            holder.btnDelete.setOnClickListener { delete(ingredient.ingredientId) }
            holder.btnEdit.setOnClickListener { load(ingredient)}
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmI.getResult().observe(viewLifecycleOwner) { list ->
            var ingredientArray = list.filter{ i ->
                i.ingreRecipeId == id
            }
            adapter.submitList(ingredientArray)
        }

        return binding.root
    }

    private fun delete(id:String){
        vmI.delete(id)
    }

    private fun load(i :Ingredient){
        binding.edtIngredient.setText(i.ingredientName).toString()
        binding.edtUpdateIngredientAmount.setText(i.ingredientAmount).toString()
        ingR = i.ingreRecipeId
        ing = i.ingredientId
        binding.btnUpdateIngre.isClickable = true
    }

    private fun update(){

        val i = Ingredient(
            ingredientId = ing,
            ingreRecipeId = ingR,
            ingredientName = binding.edtIngredient.editableText.toString().trim(),
            ingredientAmount = binding.edtUpdateIngredientAmount.editableText.toString().trim(),
        )
        val err = vmI.validate(i)
        if(err != ""){
            errorDialog(err)
        }else{
            vmI.set(i)
            binding.edtUpdateIngredientAmount.setText("")
            binding.edtIngredient.setText("")
            binding.btnUpdateIngre.isClickable = false
        }
    }

    private fun add(){
        val i = Ingredient(
            ingreRecipeId = id,
            ingredientName = binding.edtIngredient.editableText.toString().trim(),
            ingredientAmount = binding.edtUpdateIngredientAmount.editableText.toString().trim(),

        )
        val err = vmI.validate(i)
        if(err != ""){
            errorDialog(err)
        }else{
            Firebase.firestore.collection("ingredients").document().set(i)
            binding.edtUpdateIngredientAmount.setText("")
            binding.edtIngredient.setText("")
            binding.edtIngredient.requestFocus()
            binding.btnUpdateIngre.isClickable = false
        }

    }

}
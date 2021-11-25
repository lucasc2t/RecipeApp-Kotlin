package my.com.recipeapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.recipeapp.Data.Recipe
import my.com.recipeapp.R
import my.com.recipeapp.Data.RecipeViewModel
import my.com.recipeapp.databinding.FragmentInsertRecipeBinding
import my.com.recipeapp.databinding.FragmentUpdateRecipeBinding
import my.com.recipeapp.util.*

class UpdateRecipeFragment : Fragment() {

    private lateinit var binding: FragmentUpdateRecipeBinding
    private val nav by lazy{ findNavController() }
    private val vmR : RecipeViewModel by activityViewModels()
    private val id by lazy { requireArguments().getString("id","N/A") }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgRecipe.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentUpdateRecipeBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        btn.visibility = View.GONE

        binding.btnServingMinus.isClickable = false

        vmR.getAll()
        binding.btnServingAdd.setOnClickListener {
            var add = binding.txtServingNo.text.toString().toInt() + 1
            binding.txtServingNo.text = add.toString()
        }
        binding.btnServingMinus.setOnClickListener {
            var minus = binding.txtServingNo.text.toString().toInt() - 1
            if(minus >= 1) {
                binding.btnServingMinus.isClickable = true
                binding.txtServingNo.text = minus.toString()
            }

        }
        reset()
        binding.imgRecipe.setOnClickListener { selectImage() }
        binding.btnUpdateRecipe.setOnClickListener { updateRecipe() }

        return binding.root
    }

    private fun reset() {
        val r = vmR.get(id)
        if(r == null){
            nav.navigateUp()
            return
        }
        load(r)
    }

    private fun load(r: Recipe){
        binding.edtRecipeName.setText(r.recipeName).toString()
        binding.edtRecipeDecrip.setText(r.recipeDescription).toString()
        binding.edtPreparationTime.setText(r.recipePreparationTime).toString()
        binding.imgRecipe.setImageBitmap(r.recipeImage.toBitmap())
        binding.txtServingNo.text = r.recipeServingNo.toString()

        val position = check(r.recipeType)
        binding.spnRecipeType.setSelection(position)
    }

    private fun check(recipeType: String): Int {

        if (recipeType == "Breakfast"){
            return 0
        }else if (recipeType == "Lunch") {
            return 1
        }else if (recipeType == "Dinner"){
            return 2
        }else if (recipeType == "Desserts"){
            return 3
        }else {
            return 4
        }
    }


    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun updateRecipe() {
        var type = binding.spnRecipeType.selectedItem.toString()

        val r = Recipe(
            recipeId = id,
            recipeName = binding.edtRecipeName.editableText.toString().trim(),
            recipeDescription = binding.edtRecipeDecrip.editableText.toString().trim(),
            recipePreparationTime = binding.edtPreparationTime.editableText.toString().trim(),
            recipeType = type,
            recipeServingNo = binding.txtServingNo.text.toString().toInt(),
            recipeImage = binding.imgRecipe.cropToBlob(300,300),
        )
            val err = "Update Complete"
            informationDialog(err)
            vmR.set(r)
            nav.navigate(R.id.homeFragment)


    }

}
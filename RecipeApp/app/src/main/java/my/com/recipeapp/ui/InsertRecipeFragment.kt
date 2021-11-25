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
import my.com.recipeapp.util.cropToBlob
import my.com.recipeapp.util.errorDialog
import my.com.recipeapp.util.informationDialog
import my.com.recipeapp.util.snackbar

class InsertRecipeFragment : Fragment() {

    private lateinit var binding: FragmentInsertRecipeBinding
    private val nav by lazy{ findNavController() }
    private val vmR : RecipeViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgRecipe.setImageURI(it.data?.data)
        }
    }

    private var rId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInsertRecipeBinding.inflate(inflater, container, false)

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
        binding.imgRecipe.setOnClickListener { selectImage() }
        binding.btnUpdateRecipe.setOnClickListener { addRecipe() }

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun addRecipe() {
        var type = binding.spnRecipeType.selectedItem.toString()
        val id = "RCP" + (vmR.calSize() + 1).toString()
        var newID = vmR.validID()
        rId = newID

        val r = Recipe(
            recipeId = newID,
            recipeName = binding.edtRecipeName.editableText.toString().trim(),
            recipeDescription = binding.edtRecipeDecrip.editableText.toString().trim(),
            recipePreparationTime = binding.edtPreparationTime.editableText.toString().trim(),
            recipeType = type,
            recipeServingNo = binding.txtServingNo.text.toString().toInt(),
            recipeImage = binding.imgRecipe.cropToBlob(300,300),
        )
        val err = vmR.validate(r)
        if(err != ""){
            errorDialog(err)
            return
        }else{
            val err = "Completed, Add the Ingredients !"
            informationDialog(err)
            vmR.set(r)
            nav.navigate(R.id.ingredientsFragment, bundleOf("id" to rId))
        }

    }

}
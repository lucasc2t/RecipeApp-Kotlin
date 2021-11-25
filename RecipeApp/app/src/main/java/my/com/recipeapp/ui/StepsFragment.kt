package my.com.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.com.recipeapp.Data.*
import my.com.recipeapp.R
import my.com.recipeapp.databinding.FragmentStepsBinding
import my.com.recipeapp.util.StepAdapter
import my.com.recipeapp.util.errorDialog
import my.com.recipeapp.util.hideKeyboard
import my.com.recipeapp.util.informationDialog

class StepsFragment : Fragment() {

    private lateinit var binding: FragmentStepsBinding
    private val nav by lazy{ findNavController() }

    private val vmI : IngredientsViewModel by activityViewModels()
    private val vmR : RecipeViewModel by activityViewModels()
    private val vmS : StepViewModel by activityViewModels()
    private val id by lazy { requireArguments().getString("id", "N/A") }

    private lateinit var  adapter : StepAdapter

    private var stepNum = "0".toInt()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentStepsBinding.inflate(inflater, container, false)

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        btn.visibility = View.GONE

        vmS.getAll()

        binding.btnAddStep.setOnClickListener { addStep() }
        binding.btnComplete.isClickable = false

        adapter = StepAdapter() { holder,  step->
//            holder.root.setOnClickListener { nav.navigate(R.id.categoryFragment) }
//            holder.txtStepno.text = "1"
        }
        binding.rv.adapter = adapter

        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmS.getAll().observe(viewLifecycleOwner){list ->
            var l = list.sortedBy { it.stepNo }

            var stepArray =l.filter{ s ->
                s.stepRecipeId == id
            }
            adapter.submitList(stepArray)

            if(stepArray.isNotEmpty()){
                binding.btnComplete.isClickable = true
                binding.btnComplete.setOnClickListener {
                    informationDialog("New Recipe Added")
                    nav.navigate(R.id.homeFragment) }
            }
            adapter.submitList(stepArray)
        }

        return binding.root
    }

    private fun addStep() {
         stepNum += 1

        val s = Step(
            stepRecipeId = id,
            stepInfo = binding.edtStepDescrip.editableText.toString().trim(),
            stepNo = stepNum
        )
        val err = vmS.validate(s)
        if(err != ""){
            errorDialog(err)
            return
        }else{
            Firebase.firestore.collection("steps").document().set(s)
            hideKeyboard()
            informationDialog("Step Added")
            binding.edtStepDescrip.setText("")
            binding.edtStepDescrip.requestFocus()

        }
    }

}
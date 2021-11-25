package my.com.recipeapp.ui

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.set
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import my.com.recipeapp.Data.Ingredient
import my.com.recipeapp.Data.IngredientsViewModel
import my.com.recipeapp.Data.Step
import my.com.recipeapp.Data.StepViewModel
import my.com.recipeapp.R
import my.com.recipeapp.databinding.FragmentListIngredientBinding
import my.com.recipeapp.databinding.FragmentListStepBinding
import my.com.recipeapp.util.IngredientsAdapter
import my.com.recipeapp.util.StepAdapter
import my.com.recipeapp.util.errorDialog
import my.com.recipeapp.util.snackbar

class listStepFragment : Fragment() {

    private lateinit var binding: FragmentListStepBinding
    private val nav by lazy { findNavController()}
    private val vmS: StepViewModel by activityViewModels()

    private lateinit var adapter: StepAdapter

    private val id by lazy { requireArguments().getString("id", "N/A") }

    private var sId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vmS.getAll()

        binding = FragmentListStepBinding.inflate(inflater,container, false)
        binding.btnUpdateStep.setOnClickListener { update() }
        binding.btnAddStep.setOnClickListener { add() }
        binding.btnUpdateStep.isClickable = false

        adapter = StepAdapter(){ holder, step ->

            holder.btnDelete.visibility = View.VISIBLE
            holder.btnEdit.visibility = View.VISIBLE

            holder.btnDelete.setOnClickListener { delete(step.stepID) }
            holder.btnEdit.setOnClickListener { load(step)}
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vmS.getAll().observe(viewLifecycleOwner) { list ->
            var l = list.sortedBy { it.stepNo }
            var stepArray = l.filter{ s ->
                s.stepRecipeId == id
            }
            adapter.submitList(stepArray)
        }

        return binding.root
    }

    private fun delete(id:String){
        vmS.delete(id)
    }

    private fun load(s :Step){
        binding.edtStepNo.setText(s.stepNo.toString())
        binding.edtStepInfo.setText(s.stepInfo).toString()
        sId = s.stepID

        binding.btnUpdateStep.isClickable = true
    }

    private fun update(){

        val s = Step(
            stepRecipeId = id,
            stepNo = binding.edtStepNo.editableText.toString().toIntOrNull() ?:0,
            stepInfo = binding.edtStepInfo.editableText.toString().trim(),
            stepID = sId,
        )
        val err = vmS.validate(s)
        if(err != ""){
            errorDialog(err)
        }else{
            vmS.set(s)
            binding.edtStepInfo.setText("")
            binding.edtStepNo.setText("")
            binding.btnUpdateStep.isClickable = false
        }
    }

    private fun add(){
        val s = Step(
            stepRecipeId = id,
            stepNo = binding.edtStepNo.editableText.toString().toIntOrNull()?:0,
            stepInfo = binding.edtStepInfo.editableText.toString().trim(),
        )

        if(binding.edtStepNo.editableText.isEmpty() && binding.edtStepInfo.editableText.isEmpty()){
            val err = "Put in Step Number \nPut in Step Information"
            errorDialog(err)
        }else if(binding.edtStepInfo.editableText.isEmpty()){
            val err = "Put in Step Information"
            errorDialog(err)
        }else if(binding.edtStepNo.editableText.isEmpty()){
            val err = "Put in Step Number"
            errorDialog(err)
        }
        else{
            Firebase.firestore.collection("steps").document().set(s)
            binding.edtStepInfo.setText("")
            binding.edtStepNo.setText("")
        }

    }

}
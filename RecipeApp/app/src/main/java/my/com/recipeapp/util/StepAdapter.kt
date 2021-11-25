package my.com.recipeapp.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.recipeapp.Data.Ingredient
import my.com.recipeapp.Data.Step
import my.com.recipeapp.R

class StepAdapter (val fn: (ViewHolder, Step) -> Unit = { _, _ -> })
    :  ListAdapter<Step, StepAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(a: Step, b: Step)    = a.stepID == b.stepID
        override fun areContentsTheSame(a: Step, b: Step) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtStepDescrip : TextView = view.findViewById(R.id.txtStepDescrip)
        val txtStepNo : TextView = view.findViewById(R.id.txtStepNo)
        val btnDelete : Button = view.findViewById(R.id.btnDeleteStep)
        val btnEdit : Button = view.findViewById(R.id.btnEditStep)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_step, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = getItem(position)

        holder.btnDelete.visibility = View.GONE
        holder.btnEdit.visibility = View.GONE
        holder.txtStepDescrip.text = step.stepInfo
        holder.txtStepNo.text = step.stepNo.toString()

        fn(holder, step)
    }

}
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
import my.com.recipeapp.R

class IngredientsAdapter (val fn: (ViewHolder, Ingredient) -> Unit = { _, _ -> })
    :  ListAdapter<Ingredient, IngredientsAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(a: Ingredient, b: Ingredient)    = a.ingredientId == b.ingredientId
        override fun areContentsTheSame(a: Ingredient, b: Ingredient) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val txtIngredientName : TextView = view.findViewById(R.id.txtIngredientName)
        val txtIngredientAmount : TextView = view.findViewById(R.id.txtIngredientAmount)
        val btnDelete : Button = view.findViewById(R.id.btnDeleteIngredient)
        val btnEdit : Button = view.findViewById(R.id.btnEditIngredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_ingredients, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = getItem(position)

        holder.btnDelete.visibility = View.GONE
        holder.btnEdit.visibility = View.GONE
        holder.txtIngredientName.text = ingredient.ingredientName
        holder.txtIngredientAmount.text = ingredient.ingredientAmount

        fn(holder, ingredient)
    }

}
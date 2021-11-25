package my.com.recipeapp.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import my.com.recipeapp.Data.Recipe
import my.com.recipeapp.R

class RecipeAdapter (val fn: (ViewHolder, Recipe) -> Unit = { _, _ -> })
    :  ListAdapter<Recipe, RecipeAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(a: Recipe, b: Recipe)    = a.recipeId == b.recipeId
        override fun areContentsTheSame(a: Recipe, b: Recipe) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto : ImageView = view.findViewById(R.id.imgRecipe)
        val txtRecipeName : TextView = view.findViewById(R.id.txtRecipeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)

        holder.txtRecipeName.text = recipe.recipeName
        holder.imgPhoto.setImageBitmap(recipe.recipeImage.toBitmap())

        fn(holder, recipe)
    }

}
package my.com.recipeapp.Data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import my.com.recipeapp.Data.Recipe

class RecipeViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("recipes")
    private val recipes = MutableLiveData<List<Recipe>>()
    private var searchRecipe = listOf<Recipe>()
    private val results = MutableLiveData<List<Recipe>>()
    private var name = ""

    //init block will always run before the constructor
    init {
        col.addSnapshotListener { snap, _ -> recipes.value = snap?.toObjects()
            searchRecipe = snap!!.toObjects<Recipe>()
            updateResult()
        }
    }

    fun search(name: String){
        this.name = name
        updateResult()
    }

    private fun updateResult() {
        var list = searchRecipe

        list = list.filter {
            it.recipeName.contains(name, true)
        }
        results.value = list
    }

    fun getResult() = results

    fun get(id: String): Recipe? {
        return recipes.value?.find { r -> r.recipeId == id }
    }

    fun getAll() = recipes

    fun delete(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        recipes.value?.forEach { r -> delete(r.recipeId) }
    }

    fun set(r: Recipe) {
        col.document(r.recipeId).set(r)
    }

    fun calSize() = recipes.value?.size?: 0

    //Validation
    //-----------------------------------

    private fun idExists(id: String): Boolean {
        return recipes.value?.any { r -> r.recipeId == id } ?: false
    }

    fun validID(): String {
        var newID: String

        val getLastOrder = recipes.value?.lastOrNull()?.recipeId.toString()
        val num: String = getLastOrder.substringAfterLast("RCP10")
        newID = "RCP10" + (num.toIntOrNull()?.plus(1)).toString()

        if (newID == "RCP1010") {
            newID = "RCP1" + (num.toIntOrNull()?.plus(1)).toString()
            return newID
        }

        return when (calSize()) {
            0 -> {
                newID = "RCP10" + (calSize().plus(1)).toString()
                newID
            }
            in 0..9 -> {
                val getLastOrder = recipes.value?.lastOrNull()?.recipeId.toString()
                val num: String = getLastOrder.substringAfterLast("RCP10")
                newID = "RCP10" + (num.toIntOrNull()?.plus(1)).toString()
                if (newID == "RCP10null") {
                    newID = "RCP111"
                    newID
                } else newID
            }
            else -> {
                val getLastOrder = recipes.value?.lastOrNull()?.recipeId.toString()
                val num: String = getLastOrder.substringAfterLast("RCP1")
                newID = "RCP1" + (num.toIntOrNull()?.plus(1)).toString()
                newID
            }
        }
    }

    private fun nameExists(name: String): Boolean {
        return recipes.value?.any{ r -> r.recipeName == name } ?: false
    }

    fun validate(r: Recipe, insert: Boolean = true): String {

        var e = ""

        //Recipe Name
        e += if (r.recipeName == "") "- Recipe Name is required.\n"
        else if (r.recipeName.length < 3) "- Recipe Name is too short.\n"
        else if (nameExists(r.recipeName)) "- Recipe Name is duplicated.\n"
        else ""

        //Recipe Description
        e += if (r.recipeDescription == "") "- Description is required.\n"
        else if (r.recipeDescription.length < 3) "- Description is to short.\n"
        else ""

        //Serving No
        e += if (r.recipeServingNo == 0) "- Serving No. cannot be 0. \n"
        else ""

        //Preparation Time
        e += if (r.recipePreparationTime == "") "- Preparation Time is required. \n"
        else ""

        //Image
        e += if (r.recipeImage.toBytes().isEmpty()) "- Recipe Image is required.\n"
        else ""

        return e
    }
}
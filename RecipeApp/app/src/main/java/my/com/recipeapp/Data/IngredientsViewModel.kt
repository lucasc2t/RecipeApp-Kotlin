package my.com.recipeapp.Data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class IngredientsViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("ingredients")
    private val ingredients = MutableLiveData<List<Ingredient>>()
    private val result = MutableLiveData<List<Ingredient>>()
    private var searchIngre = listOf<Ingredient>()
    private var name = ""

    //init block will always run before the constructor
    init {
        col.addSnapshotListener { snap, _ -> ingredients.value = snap?.toObjects()
            searchIngre = snap!!.toObjects<Ingredient>()
            updateResult() }
    }

    fun search(name: String){
        this.name = name
        updateResult()
    }

    fun get(id: String): Ingredient? {
        return ingredients.value?.find { i -> i.ingredientId == id }
    }

    fun getAll() = ingredients

    private fun updateResult() {
        var list = searchIngre

        list = list.filter {
            it.ingredientId.contains(name, true)
        }
        result.value = list
    }

    fun getResult() = result

    fun delete(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        ingredients.value?.forEach { i -> delete(i.ingredientId) }
    }

    fun set(i: Ingredient) {
        col.document(i.ingredientId).set(i)
    }

    fun calSize() = ingredients.value?.size?: 0

    //Validation
    //-----------------------------------

    private fun idExists(id: String): Boolean {
        return ingredients.value?.any { i -> i.ingredientId == id } ?: false
    }

    fun validate(i: Ingredient, insert: Boolean = true): String {

        var e = ""

        //Ingredient Name
        e += if (i.ingredientName == "") "- Ingredient Name is required.\n"
        else ""

        //Ingredient Amount
        e += if (i.ingredientAmount == "") "- Ingredient Amount is required.\n"
        else ""

        return e
    }
}
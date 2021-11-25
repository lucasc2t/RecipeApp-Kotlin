package my.com.recipeapp.Data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class StepViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("steps")
    private val steps = MutableLiveData<List<Step>>()

    //init block will always run before the constructor
    init {
        col.addSnapshotListener { snap, _ -> steps.value = snap?.toObjects() }
    }

    fun get(id: String): Step? {
        return steps.value?.find { s -> s.stepID == id }
    }

    fun getAll() = steps

    fun delete(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        steps.value?.forEach { s -> delete(s.stepID) }
    }

    fun set(s: Step) {
        col.document(s.stepID).set(s)
    }

    fun calSize() = steps.value?.size?: 0

    //Validation
    //-----------------------------------

    private fun idExists(id: String): Boolean {
        return steps.value?.any { s -> s.stepID == id } ?: false
    }

    fun validate(s: Step, insert: Boolean = true): String {

        var e = ""

        //Recipe Name
        e += if (s.stepInfo == "") "- Step Info is required.\n"
        else if (s.stepInfo.length < 3) "- Recipe Name is too short.\n"
        else ""

        return e
    }

}
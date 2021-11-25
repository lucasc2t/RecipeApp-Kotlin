package my.com.recipeapp.Data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId

var email1 : String = ""
var username : String = ""
var img : Blob = Blob.fromBytes(ByteArray(0))

data class User(
    @DocumentId
    var userId   : String = "",
    var email    : String = "",
    var password : String = "",
    var userName : String = "",
)

data class Recipe(
    @DocumentId
    var recipeId               : String = "",
    var recipeName             : String = "",
    var recipeDescription      : String = "",
    var recipePreparationTime  : String = "",
    var recipeType             : String = "",
    var recipeServingNo        : Int = 0,
    var recipeImage            : Blob = Blob.fromBytes(ByteArray(0)),
)

data class Ingredient(
    @DocumentId
    var ingredientId     : String = "",
    var ingreRecipeId    : String = "",
    var ingredientName   : String = "",
    var ingredientAmount : String = "",
)

data class  Step(
    @DocumentId
    var stepID: String = "",
    var stepRecipeId: String = "",
    var stepNo: Int = 0,
    var stepInfo: String = "",
)



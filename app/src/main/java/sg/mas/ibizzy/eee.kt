package sg.mas.ibizzy

/**
 * Created by Sergey on 26.02.17.
 */

//fun isOdd(s: String):Boolean = s == "brillig" || s == "slithy" || s == "tove"
fun isOdd(s: String) = s == "brillig" || s == "slithy" || s == "tove"
fun isOdd(x: Int) = x % 2 != 0

val predicate: (String) -> Boolean = ::isOdd
val predicate2: (Int) -> Boolean = ::isOdd
val predicate3: (String) ->  CharArray = String::toCharArray


val predicate4: ( String) -> CharArray =  String::toCharArray


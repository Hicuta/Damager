package hicuta.damager.utils

fun isInt(s: String): Boolean {
    try {
        s.toInt()
    } catch (nfe: NumberFormatException) {
        return false
    }
    return true
}
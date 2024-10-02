package com.example.frontend_android.util.extensions

/**
 * Checks whether the String instance contains a special character
 * @author Reshwan Barhoe
 * @return If the String instance contains a special character
 */
fun String.containsSpecialCharacters(): Boolean {
    val nonAlphanumericPattern = Regex("[^\\p{IsAlphabetic}\\d ]")
    return nonAlphanumericPattern.containsMatchIn(this)
}

/**
 * Removes any extra spacing from the String instance
 * @author Reshwan Barhoe
 * @return the string without extra spacing
 */
fun String.removeExtraSpacing(): String {
    return this.trim().replace("\\s+".toRegex(), " ")
}

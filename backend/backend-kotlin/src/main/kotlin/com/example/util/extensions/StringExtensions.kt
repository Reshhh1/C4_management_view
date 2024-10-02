package com.example.util.extensions

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
 * checks if the length of a string is valid or not
 * @author Ömer Aynaci
 * @param minimumLength the minimum length of a string
 * @param maximumLength the maximum length of a string
 * @return true if the string is in the range of the minimum and maximum length
 */
fun String.isLengthValid(minimumLength: Int, maximumLength: Int): Boolean {
    return this.length in minimumLength..maximumLength
}
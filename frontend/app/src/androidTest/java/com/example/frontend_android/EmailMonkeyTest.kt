package com.example.frontend_android

class EmailMonkeyTest {

    /**
     * Generates random email provider
     * @author Ömer Aynaci
     * @return random providers
     */
    private fun generateRandomProvider(): String {
        val providerNames = listOf("hotmail", "gmail", "outlook", "nl.abnamro")
        return providerNames.random()
    }

    /**
     * Generates random email domain
     * @author Ömer Aynaci
     * @return random domains
     */
    private fun generateDomain(): String {
        val domainNames = listOf(".net", ".nl", ".com")
        return domainNames.random()
    }

    /**
     * Generates random string
     * @author Ömer Aynaci
     * @param length the length of the generated string
     * @return random string
     */
    private fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    /**
     * Generates random email
     * @author Ömer Aynaci
     * @return random email
     */
    fun generateRandomEmail(): String {
        val emailUsername = generateRandomString(8)
        val emailProvider = generateRandomProvider()
        val emailDomain = generateDomain()
        return "$emailUsername@$emailProvider${emailDomain}"
    }
}
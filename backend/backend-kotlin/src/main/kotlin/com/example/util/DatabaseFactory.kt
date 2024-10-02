package com.example.util

import com.example.features.session.data.model.*
import com.example.features.team.data.model.*
import com.example.features.user.data.model.*
import com.typesafe.config.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {

    /**
     * Makes a connection with the database, and it creates the tables in the database
     * @author Reshwan Barhoe & Ömer Aynaci
     */
    fun init() {
        val config = getConfig()
        val database = Database.connect(
            url = config.getString("ktor.database.url"),
            driver = config.getString("ktor.database.driver"),
            user = config.getString("ktor.database.user"),
            password = config.getString("ktor.database.password")
        )
        if(!isTesting()) {
            transaction(database) {
                SchemaUtils.create(User,Sessions, Team, TeamUser)
            }
        }
    }

    /**
     * Gets the correct configuration, depending on the environment variable.
     * @author Reshwan Barhoe
     * @return the correct application configuration
     */
     fun getConfig(): Config {
        return if(System.getenv("environment") == "testing") {
             ConfigFactory.load("testApplication.conf")
        } else {
             ConfigFactory.load("application.conf")
        }
    }

    /**
     * Checks if the environment is running in a testing environment
     * @author Reshwan Barhoe
     * @return true if the application is running in a testing environment
     */
    private fun isTesting(): Boolean {
        return System.getenv("environment") == "testing"
    }

    /**
     * Executes a database query in a suspend transaction
     * @author Reshwan Barhoe
     * @param block The suspend function that represents as the database query
     * @return The result of the database query
     */
    suspend fun<T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mysqlVersion: String by project
val exposed_version: String by project
val h2_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
    kotlin("kapt") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    //Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    //databases
    testImplementation("com.h2database:h2:$h2_version")
    implementation("com.mysql:mysql-connector-j:8.2.0")

    //JWT Authentication
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")

    //Hashing
    implementation("org.mindrot:jbcrypt:0.4")

    //Dependency injection
    implementation("com.google.dagger:dagger:2.51")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.9")
    kapt("com.google.dagger:dagger-compiler:2.51")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("org.mockito:mockito-core:5.8.0")

    //property based testing
    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.8.1")
    testImplementation("io.kotest:kotest-property-jvm:5.8.1")


    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-client-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

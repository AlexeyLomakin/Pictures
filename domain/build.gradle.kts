plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)

    // JUnit 5 for unit tests
    testImplementation(libs.junit.jupiter)

    // Mockito for mocking

    // Kotlin test dependencies
    testImplementation(libs.kotlinx.coroutines.test)
}

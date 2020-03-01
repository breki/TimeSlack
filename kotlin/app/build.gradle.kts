plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")
    defaultConfig {
        applicationId = "net.igorbrejc.timeslack"
        minSdkVersion(15)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
}

dependencies {
    val kotlinVersion: String? by extra

    implementation(
        fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", version = kotlinVersion))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.google.android.material:material:1.1.0")
    // https://mvnrepository.com/artifact/com.google.guava/guava
    implementation(
        group = "com.google.guava", name = "guava", version = "28.2-android")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation(
        group = "com.natpryce", name = "hamkrest", version = "1.7.0.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

// tells all test tasks to use Gradle's built-in JUnit 5 support
tasks.withType<Test> {
    useJUnitPlatform()

    // tells the test runner to display results of all tests,
    // not just failed ones
//    testLogging {
//        events("passed", "skipped", "failed")
//    }
}

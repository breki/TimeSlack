plugins {
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        val kotlinVersion = "1.3.61"
        extra["kotlinVersion"] = kotlinVersion

        classpath("com.android.tools.build:gradle:3.6.0")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

ktlint {
    debug.set(true)
    verbose.set(true)
//    disabledRules.set(setOf("no-wildcard-imports"))
//    additionalEditorconfigFile.set(file(".editorconfig"))
}

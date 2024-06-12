// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlinx.atomicfu.plugin)
    }
}

//TODO https://youtrack.jetbrains.com/issue/KT-49178/Multiplatform-plugin-is-incompatible-with-atomicfu-gradle-plugin
allprojects {
    apply(plugin = "kotlinx-atomicfu")
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.androidLibrary) apply false
}
true // Needed to make the Suppress annotation work for the plugins block
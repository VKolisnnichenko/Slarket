plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.room).apply(false)
    alias(libs.plugins.ksp) apply false
}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.24.2")
    }
}

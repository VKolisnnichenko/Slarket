plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    id("co.touchlab.skie") version "0.8.4"
    id("dev.icerock.mobile.multiplatform-resources")

}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            linkerOpts.add("-lsqlite3")
            export("dev.icerock.moko:resources:0.24.2")
            export("dev.icerock.moko:graphics:0.9.0")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.koin.core)

                implementation(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.sqlite)

                api(libs.resources)


                configurations.implementation{
                    exclude(group = "com.intellij", module = "annotations")
                }

            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.lifecycle.viewmodel.ktx)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            kotlin.srcDir("build/generated/ksp/metadata")
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = "com.vkolisnichenko.slarket"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies{
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
    listOf(
        "kspIosSimulatorArm64",
        "kspIosX64",
        "kspIosArm64"
    ).forEach {
        add(it, libs.androidx.room.compiler)
    }

}

room {
    schemaDirectory("$projectDir/schemas")
}

multiplatformResources {
    resourcesPackage.set("com.vkolisnichenko.slarket.resources")
    resourcesClassName.set("SharedResources")
}

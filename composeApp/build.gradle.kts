import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            // Ktor Android engine
            implementation(libs.ktor.client.android)
            // ✅ Material Icons (FIX)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            //implementation(libs.androidx.lifecycle.viewmodelCompose)
           // implementation(libs.androidx.lifecycle.runtimeCompose)

            // ---------- Compose ----------
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.koin)
            implementation(compose.materialIconsExtended)


            // ---------- Coroutines ----------
            implementation(libs.coroutines.core)

            // ---------- Ktor ----------
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content)
            implementation(libs.ktor.serialization)
            implementation("io.ktor:ktor-client-auth:3.3.3")
            implementation("io.ktor:ktor-client-logging:3.3.3")
            implementation("cafe.adriel.voyager:voyager-tab-navigator:1.0.1")


            // ---------- Serialization ----------
            implementation(libs.serialization.json)

            // ---------- DI ----------
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
           // implementation(libs.koin.compose.mp)

            // Local Storage
//            implementation("com.russhwolf:multiplatform-settings:1.3.0")
            implementation("com.russhwolf:multiplatform-settings-no-arg:1.3.0")



        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.business.cmpproject"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.business.cmpproject"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

/* 🔥 THIS BLOCK IS THE KEY 🔥 */
compose.resources {
    publicResClass = true
  //  packageOfResClass  = "com.business.cmpproject.composeapp.generated"
    packageOfResClass  = "com.business.cmpproject.composeapp.generated.resources"
    generateResClass = auto
}


import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    // AGP 9: el módulo KMP usa el plugin Android-KMP-library, no com.android.library.
    androidLibrary {
        namespace = "com.strawtechberry.yupana.shared"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // Targets iOS (iosX64 eliminado en CMP 1.11.1). El framework lo consume iosApp.
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.mvi)
            implementation(projects.core.supabase)
            implementation(projects.feature.auth)
            implementation(projects.feature.clients)
            implementation(projects.feature.accounts)
            implementation(projects.feature.assignment)
            implementation(projects.feature.dashboard)
            implementation(projects.feature.settings)

            implementation(libs.jetbrains.navigation.compose)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.jetbrains.lifecycle.viewmodel)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
            implementation(libs.kotlinx.coroutines.core)

            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

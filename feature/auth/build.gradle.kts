import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "com.strawtechberry.yupana.feature.auth"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        // Workaround: CMP-9547 (composeResources no se empaqueta en el APK con AGP 9 +
        // com.android.kotlin.multiplatform.library). https://youtrack.jetbrains.com/issue/CMP-9547
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "featureAuth"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.mvi)
            implementation(projects.core.designsystem)
            implementation(projects.core.supabase)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
            implementation(libs.kotlinx.coroutines.core)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.strawtechberry.yupana.feature.auth.generated.resources"
}

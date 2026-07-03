import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    // AGP 9: el módulo KMP usa el plugin Android-KMP-library (igual que :shared).
    androidLibrary {
        namespace = "com.strawtechberry.yupana.core.designsystem"
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
            baseName = "designsystem"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            // Íconos (mail/lock/visibility…) para campos y pantallas. R8 descarta los no usados.
            api(compose.materialIconsExtended)
            api(compose.components.resources)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.strawtechberry.yupana.core.designsystem.generated.resources"
}

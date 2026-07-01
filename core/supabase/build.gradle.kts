import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    // Aísla supabase-kt: factory del cliente + módulo Koin + engine Ktor por plataforma.
    androidLibrary {
        namespace = "com.strawtechberry.yupana.core.supabase"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "supabase"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // BOM alinea las versiones de los módulos supabase-kt.
            api(project.dependencies.platform(libs.supabase.bom))
            api(libs.supabase.auth)
            api(libs.supabase.postgrest)
            api(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.core)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
//    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"

//    id("convention.publication")
}


group = "jato.app.jato_utils"
version = "1.0.0"

kotlin {
//    jvmToolchain(11)

    androidTarget { publishLibraryVariants("release") }
    jvm()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser () }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
//            implementation(compose.runtime)
            implementation(compose.material3)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.desktop.currentOs)
        }

        iosMain.dependencies {
            // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
            // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
            // part of KMP’s default source set hierarchy. Note that this source set depends
            // on common by default and will correctly pull the iOS artifacts of any
            // KMP dependencies declared in commonMain.
        }

        wasmJsMain.dependencies {
        }
    }

    //https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compileTaskProvider.configure {
            compilerOptions {
                freeCompilerArgs.add("-Xexport-kdoc")
            }
        }
    }

}

android {
    namespace = "jato.app.jato_utils"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}
dependencies {
    implementation(libs.androidx.runtime.android)
}

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.shilapi.xcertplay"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.shilapi.xcertplay"
        minSdk = 24
        targetSdk = 37
        versionCode = 1201
        versionName = "1.2.1"

    }

    signingConfigs {
        create("release") {
            storeFile = file(
                providers.environmentVariable("ANDROID_KEYSTORE_PATH")
                    .getOrElse("missing-release-keystore.jks"),
            )
            storePassword = providers.environmentVariable("ANDROID_KEYSTORE_PASSWORD").getOrElse("")
            keyAlias = providers.environmentVariable("ANDROID_KEY_ALIAS").getOrElse("")
            keyPassword = providers.environmentVariable("ANDROID_KEY_PASSWORD").getOrElse("")
        }
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":shared"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.app.automotive)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

// Copy the release APK to build/dist with a distribution-friendly name.
val releaseVersionName = extensions.getByType<ApplicationExtension>().defaultConfig.versionName
androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        val apkDir = variant.artifacts.get(SingleArtifact.APK)
        val apkName = "DiPlay-$releaseVersionName-Jinson.apk"
        val copyReleaseApk = tasks.register<Copy>("copyReleaseApk") {
            from(apkDir) {
                include("*.apk")
            }
            into(layout.buildDirectory.dir("dist"))
            rename { apkName }
        }
        tasks.matching { it.name == "assembleRelease" }.configureEach {
            finalizedBy(copyReleaseApk)
        }
    }
}

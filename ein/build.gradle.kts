plugins {
    id("com.android.library")
    kotlin("multiplatform")
}
android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(28)
        versionCode =1
        versionName ="1.0"
        //multiDexEnabled = true
    }
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}
repositories {
    google()
    jcenter()
    mavenCentral()
}
kotlin {
    jvm()
    js()
    android()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("com.squareup.okhttp3:okhttp:3.14.2")
            }
        }
        val jsMain by getting {
            apply(plugin = "kotlin-dce-js")
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("androidx.core:core-ktx:1.2.0-alpha04")
                implementation("androidx.activity:activity:1.1.0-alpha03")
                implementation("androidx.appcompat:appcompat:1.1.0")
                implementation("com.squareup.okhttp3:okhttp:3.14.2")
            }
        }
        all {
            languageSettings.enableLanguageFeature("InlineClasses")
            languageSettings.progressiveMode = true
        }
    }
}
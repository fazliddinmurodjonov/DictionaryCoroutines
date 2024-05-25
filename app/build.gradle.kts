plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}


android {
    namespace = "com.brightfuture.dictionary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.brightfuture.dictionary"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.1.1"
        multiDexEnabled=true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        //noinspection KaptUsageInsteadOfKsp
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding  = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation ("com.github.kirich1409:viewbindingpropertydelegate-full:1.5.9")
    implementation ("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.9")
    implementation ("com.squareup.retrofit2:converter-scalars:2.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("androidx.room:room-common:2.6.1")
    //noinspection GradleDependency
    //noinspection GradleDependency,GradleDependency,NotInterpolated
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    //noinspection KaptUsageInsteadOfKsp
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-rxjava2:2.6.1")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation ("androidx.room:room-rxjava3:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-guava:2.6.1")
    implementation ("androidx.room:room-paging:2.6.1")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation ("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation ("io.reactivex:rxandroid:1.2.1")
    implementation ("io.reactivex:rxjava:1.3.8")
    implementation("androidx.room:room-rxjava2:2.6.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.11.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.google.code.gson:gson:2.11.0")
    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    implementation ("androidx.core:core-splashscreen:1.0.1")

}
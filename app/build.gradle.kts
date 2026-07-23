plugins {
    id 'com.android.application'
    id 'kotlin-android'
}

android {
    compileSdk 33
    defaultConfig {
        applicationId "com.example.builderapp"
        minSdk 21
        targetSdk 33
        versionCode 1
        versionName "1.0"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = '11'
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.10.0'
    implementation 'androidx.compose.ui:ui:1.4.3'
    implementation 'androidx.compose.material:material:1.4.3'
    implementation 'androidx.compose.ui:ui-tooling-preview:1.4.3'
    debugImplementation 'androidx.compose.ui:ui-tooling:1.4.3'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.4'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.0'
    androidTestImplementation 'androidx.compose.ui:ui-test-junit4:1.4.3'
    debugImplementation 'androidx.compose.ui:ui-test-manifest:1.4.3'
}
plugins {
    id "com.android.application" version "7.2.2" apply false
    id "com.android.library" version "7.2.2" apply false
    id "org.jetbrains.kotlin.android" version "1.7.20" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:7.2.2"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20"
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

subprojects {
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).configureEach {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs += ["-Xskip-metadata-version-check"]
        }
    }
}

project(':app') {
    repositories {
        mavenCentral()
    }
    dependencies {
        implementation 'androidx.core:core-ktx:1.9.0'
        implementation 'androidx.compose.ui:ui:1.3.0'
        implementation 'androidx.compose.material:material:1.3.0'
        implementation 'androidx.compose.ui:ui-tooling-preview:1.3.0'
        implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.5.1'
        implementation 'androidx.activity:activity-compose:1.5.1'
        testImplementation 'junit:junit:4.13.2'
        androidTestImplementation 'androidx.test.ext:junit:1.1.3'
        androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
        androidTestImplementation 'androidx.compose.ui:ui-test-junit4:1.3.0'
        androidTestImplementation 'androidx.compose.material:material:1.3.0'
        debugImplementation 'androidx.compose.ui:ui-tooling:1.3.0'
    }
}
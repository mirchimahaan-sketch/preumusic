plugins {
    id "com.android.application" version "7.3.3" apply false
    id "com.android.library" version "7.3.3" apply false
    id "org.jetbrains.kotlin.android" version "1.8.10" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:7.3.3"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10"
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

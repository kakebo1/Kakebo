// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    //kotlin("script-runtime")
    //kotlin("jvm") apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    //kotlin("android") version "1.9.10" apply false
  // id("com.android.library") version "8.5.2" apply false
}

buildscript{
    repositories{
        google()
        maven("https://jetpack.io")
    }
    dependencies{
        classpath("com.android.tools.build:gradle:8.5.1")
        classpath("com.google.gms:google-services:4.4.2")
    }
}

allprojects{
    repositories{
        // google()
        // mavenCentral()
    }
}

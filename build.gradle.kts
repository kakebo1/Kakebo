// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("script-runtime")
    kotlin("jvm") version "1.8.20" apply false
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
dependencies {

}
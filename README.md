# Elment

[![](https://jitpack.io/v/ZharovVV/Elment.svg)](https://jitpack.io/#ZharovVV/Elment)

Kotlin's implementation of Unidirectional Data Flow

## Using in your project
### Gradle
Add it in your root build.gradle at the end of repositories:
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
```
Add the dependency:
```kotlin
dependencies {
    implementation("com.github.ZharovVV.Elment:elment-core-jvm:1.0.0-RC")
}
```
if you want to use only core-jvm part.

Or add the dependency:
```kotlin
dependencies {
    implementation("com.github.ZharovVV.Elment:elment-android:1.0.0-RC")
}
```
if you want to use core-jvm + android part _(which includes __ElmentViewModel__)_.


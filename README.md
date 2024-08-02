# KExtensions

[![](https://jitpack.io/v/leodan11/KExtensions.svg)](https://jitpack.io/#leodan11/KExtensions)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)


Kotlin Extensions (Android extensions) and Helpers for smoother Android development.

## Installation

#### Gradle

- Step 1. Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
  repositories {
      maven { url 'https://jitpack.io' }
      }
  }
```

- Step 2. Add the dependency

```gradle
dependencies {
    implementation 'com.github.leodan11:KExtensions:{latest_version}'
}
```

#### Kotlin

- Step 1. Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```kotlin
repositories {
  maven(url = "https://jitpack.io")
}
```

- Step 2. Add the dependency

```kotlin
dependencies {
  implementation("com.github.leodan11:KExtensions:${latest_version}")
}
```

#### Moven

- Step 1. Add the JitPack repository

```xml
  <repositories>
    <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
    </repository>
  </repositories>
```

- Step 2. Add the dependency

```xml
  <dependency>
    <groupId>com.github.leodan11</groupId>
      <artifactId>KExtensions</artifactId>
      <version>latest_version</version>
   </dependency>
```

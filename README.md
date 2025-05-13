# EcoLint
Green IT custom AndroidLint rules, based on ecoCode-android Sonar lint rules (https://github.com/green-code-initiative/ecoCode-android/blob/main/README.md). The rules enforce - as warnings - a number of industry standard best practices defined in this repository: https://github.com/cnumr/best-practices-mobile#-android-platform.

### Define library parameters

Add the dependency to your libs.version.toml

```sh
[versions]
ecolint-android = "1.1.0"

[libraries]
ecolint-android = { group = "io.github.russellbecky", name = "ecolint_android", version.ref = "ecolint-android" }
```

### Add as dependency

Add to build.gradle in the module you want to use EcoLint

```sh
dependencies {
    lintChecks(libs.ecolint.android)
}
```

### Run gradle task

```sh
./gradlew lint
```
plugins {
    id("kover-convention")
}

repositories {
    mavenCentral()
}

allprojects {
    repositories {
        mavenCentral()
    }

    version = "0.0.1-SNAPSHOT"
    group = "io.github.airflux"
}

import Configuration.JVM

plugins {
    kotlin("jvm")
    java
    application
}

repositories {
    mavenCentral()
    mavenLocal()
}

val jacksonVersion by extra { "2.12.4" }

dependencies {
    implementation(project(":airflux-core"))
    implementation(project(":airflux-dsl"))
    implementation(project(":airflux-jackson-parser"))

    /* Kotlin */
    implementation(kotlin("stdlib-jdk8"))

    /* Jackson */
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion") {
        exclude(group = "org.jetbrains.kotlin")
    }
}

tasks {

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>()
        .configureEach {
            kotlinOptions {
                jvmTarget = JVM.targetVersion
                suppressWarnings = false
                freeCompilerArgs = listOf(
                    "-Xjsr305=strict",
                    "-Xjvm-default=all"
                )
            }
        }

    val javaMainClass = "io.github.airflux.quickstart.QuickstartKt"

    application {
        mainClass.set(javaMainClass)
    }

    jar {
        manifest {
            attributes["Main-Class"] = javaMainClass
        }

        from(sourceSets.main.get().output)

        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map {
                    if (it.isDirectory) it else zipTree(it)
                }
        })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

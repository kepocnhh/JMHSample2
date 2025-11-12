buildscript {
    repositories.mavenCentral()

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.25")
    }
}

tasks.register<Delete>("clean") {
    delete = setOf("build", "buildSrc/build")
}

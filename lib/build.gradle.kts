import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.time.Duration.Companion.seconds

version = "0.0.1"

repositories.mavenCentral()

plugins {
    id("org.jetbrains.kotlin.jvm")
}

tasks.getByName<JavaCompile>("compileJava") {
    targetCompatibility = "17"
}

val compileKotlinTask = tasks.getByName<KotlinCompile>("compileKotlin") {
    kotlinOptions.jvmTarget = "17"
}

sourceSets.create("jmh") {
    project.kotlin.target.compilations.also {
        it[name].associateWith(it["main"])
    }
}

dependencies {
    "jmhImplementation"("org.openjdk.jmh:jmh-core:1.37")
    "jmhImplementation"("org.openjdk.jmh:jmh-generator-bytecode:1.37")
}

project.kotlin.target.compilations.getByName("jmh") {
    val issuer = name
    val dir = layout.buildDirectory.get().dir("${issuer}Generated")
    val outputSourceDir = dir.file("sources").asFile
    val outputResourceDir = dir.file("resources").asFile
    val outputClassesDir = dir.dir("classes")
    val generatorType = "default"
    val generators = output.classesDirs.map {
        val compiledBytecodePath = it.absolutePath
        // Usage: generator <compiled-bytecode-dir> <output-source-dir> <output-resource-dir> [generator-type]
        tasks.register<JavaExec>("${issuer}RunBytecodeGenerator${compiledBytecodePath.hashCode()}") {
            dependsOn("classes")
            mainClass.set("org.openjdk.jmh.generators.bytecode.JmhBytecodeGenerator")
            classpath = sourceSets[issuer].runtimeClasspath
            args(
                compiledBytecodePath,
                outputSourceDir.absolutePath,
                outputResourceDir.absolutePath,
                generatorType,
            )
        }
    }
    val compileGeneratedTask = tasks.register<JavaCompile>("${issuer}CompileGenerated") {
        dependsOn(generators)
        classpath = sourceSets[issuer].runtimeClasspath
        source(outputSourceDir)
        destinationDirectory.set(outputClassesDir)
    }
    tasks.register<JavaExec>("runBenchmark") {
        val benchmarks: String? by project
        dependsOn(compileGeneratedTask)
        val reports = layout.buildDirectory.get().dir("reports").file("jmh").asFile
        doFirst { reports.mkdirs() }
        mainClass.set("org.openjdk.jmh.Main")
        classpath(
            sourceSets[issuer].runtimeClasspath,
            outputResourceDir,
            outputClassesDir,
        )
        val timeout = 10.seconds
        val format = "text"
        val output = reports.resolve("result.txt")
        args(
            benchmarks.orEmpty(),
            "-to=${timeout.inWholeMilliseconds}ms",
            "-rf=$format",
            "-rff=${output.absolutePath}",
            "-foe=true",
            "-t=max",
        )
    }
}

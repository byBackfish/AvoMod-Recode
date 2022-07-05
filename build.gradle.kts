import net.minecraftforge.gradle.userdev.UserDevExtension
import org.gradle.jvm.tasks.Jar

// gradle.properties
val modGroup: String by extra
val modVersion: String by extra
val modBaseName: String by extra
val forgeVersion: String by extra
val mappingVersion: String by extra

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:4.1.+") {
            isChanging = true
        }
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
    }
}

plugins {
    java
    id("com.github.johnrengelman.shadow") version "4.0.4"

}
apply {
    plugin("net.minecraftforge.gradle")
    plugin("kotlin")
}

version = modVersion
group = modGroup


configure<UserDevExtension> {
    mappings("stable",  "39-1.12")

    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")
        }

        create("server") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")
        }
    }
}

repositories {
    jcenter()
    mavenCentral()
    maven(url="https://jitpack.io")
}

dependencies {
    "minecraft"("net.minecraftforge:forge:1.12.2-14.23.5.2855")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
    implementation("com.github.byBackfish:WynncraftAPIWrapper:-SNAPSHOT")
}

// processResources
val Project.minecraft: UserDevExtension
    get() = extensions.getByName<UserDevExtension>("minecraft")
tasks.withType<Jar> {
    inputs.property("version", project.version)
    baseName = modBaseName
    filesMatching("/mcmod.info") {
        expand(mapOf(
            "version" to project.version,
            "mcversion" to "1.12.2"
        ))
    }
}

val shadowJar = tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("packaged")

    dependencies {
        include(dependency("com.github.byBackfish:WynncraftAPIWrapper:-SNAPSHOT"))
    }


}


//tasks.build.get().dependsOn(shadowJar)
tasks.withType<Jar> {
    finalizedBy("reobfJar")
}

 tasks.create("copyResourceToClasses", Copy::class) {
    tasks.classes.get().dependsOn(this)
    dependsOn(tasks.processResources.get())
    onlyIf { gradle.taskGraph.hasTask(tasks.getByName("prepareRuns")) }
    into("$buildDir/classes/kotlin/main")
    from(tasks.processResources.get().destinationDir)
}

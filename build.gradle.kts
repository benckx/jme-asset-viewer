plugins {
    alias(libs.plugins.versions)
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories {
    maven(url = "https://jitpack.io")
    google()
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)

    // jme
    implementation(libs.jme.core)
    implementation(libs.jme.desktop)
    implementation(libs.jme.lwjgl3)
    implementation(libs.jme.plugins)

    // jme libs
    implementation(libs.ouistiti)
    implementation(libs.chimp.utils.basics)
    implementation(libs.chimp.utils.jme3)

    // logging
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)

    // utils
    implementation(libs.commons.text)
    implementation(libs.kulid)

    // persistence
    implementation(libs.jackson.dataformat.xml)
    implementation(libs.jackson.module.kotlin)
}

application {
    mainClass = "be.encelade.viewer.MainKt"
}

tasks.test {
    failOnNoDiscoveredTests = false
}

tasks.jar {
    archiveBaseName = "jme-viewer"

    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude("org/testng/**")
    exclude("org/opentest4j/**")
    exclude("module-info.class")
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

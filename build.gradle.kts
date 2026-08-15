plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
}

group = "io.github.mxiwbr"
version = "1.0.2"

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    mavenCentral()
}

dependencies {
    
    // bStats
    implementation("org.bstats:bstats-bukkit:3.2.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    // compile only if all features are available in 1.21.5 (plugin works 1.21.5 and newer)
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")

    // string utilities
    implementation("org.apache.commons:commons-text:1.10.0")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Gson implementation for plugin update service
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.shadowJar {

    // Removes the "-all" from the file name
    archiveClassifier.set("")

    configurations = project.configurations.runtimeClasspath.map { setOf(it) }

    // Relocate libraries
    relocate("org.bstats", "${project.group}.libs.bstats")
    relocate("com.google.gson", "${project.group}.libs.gson")
    relocate("org.apache.commons.text", "${project.group}.libs.commonstext")

    // Minimize to reduce file size except GSON implementation
    minimize {

        // keep GSON
        exclude(
            dependency(
                "com.google.code.gson:gson:.*"
            )
        )
    }
}
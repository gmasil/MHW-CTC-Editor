plugins {
    id("java")
    application
    id("org.graalvm.buildtools.native") version "0.9.28"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "de.gmasil"
version = "1.0.1-SNAPSHOT"

application {
    mainClass = "de.gmasil.mhw.ctceditor.CtcEditorStarterWrapper"
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javafx.version))
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-controls:${javafx.version}")
    implementation("org.openjfx:javafx-graphics:${javafx.version}")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("commons-io:commons-io:2.8.0")
    implementation("org.fusesource.jansi:jansi:1.18")
    implementation("com.miglayout:miglayout-swing:5.2")
}

javafx {
    modules("javafx.controls", "javafx.graphics")
    setPlatform("windows")
    version = "21"
}

tasks.jar {
    manifest.attributes["Main-Class"] = "de.gmasil.mhw.ctceditor.CtcEditorStarterWrapper"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

graalvmNative {
    binaries.all {
        resources.autodetect()
        buildArgs.add("-Djava.awt.headless=false")
        //jvmArgs.add("flag")
        //runtimeArgs.add("--help")
    }
    toolchainDetection = false
}

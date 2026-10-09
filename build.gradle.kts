plugins {
    id("java")
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "au.edu.unimelb.swen.oop.mokepon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

javafx {
    version = "25"
    modules = listOf("javafx.controls")
}

application {
    mainModule.set("au.edu.unimelb.swen.oop.mokepon")
    mainClass.set("au.edu.unimelb.swen.oop.mokepon.Main")

    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=javafx.graphics"
    )
}

tasks.named<JavaExec>("run") {
    args("data=mokepon.game")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

val umlDoclet = configurations.create("umlDoclet")

dependencies {
    umlDoclet("nl.talsmasoftware:umldoclet:2.3.2")
}

tasks.register<Javadoc>("umlClassDiagram") {
    description = "Generates a UML class diagram of the main source set using UMLDoclet"
    group = "documentation"

    source = sourceSets.main.get().allJava.matching { exclude("module-info.java") }
    classpath = sourceSets.main.get().compileClasspath
    setDestinationDir(layout.buildDirectory.dir("docs/uml").get().asFile)

    options.doclet = "nl.talsmasoftware.umldoclet.UMLDoclet"
    options.docletpath = umlDoclet.files.toList()
    (options as CoreJavadocOptions).addStringOption("umlImageFormat", "SVG")
    (options as CoreJavadocOptions).addStringOption("-add-modules", "javafx.controls,javafx.graphics,javafx.base")
}
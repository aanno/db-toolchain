/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java project to get you started.
 * For more details take a look at the Java Quickstart chapter in the Gradle
 * user guide available at https://docs.gradle.org/5.0/userguide/tutorial_java_projects.html
 */

plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building an application
    // application
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // This dependency is found on compile classpath of this component and consumers.
    implementation("com.google.guava:guava:26.0-jre")
    // implementation("net.sf.saxon:Saxon-HE:9.9.0-2")
    implementation("org.docbook", "docbook-xslt2", "2.3.8") {
        exclude("org.xmlresolver:xmlresolver")
        exclude("org.apache.xmlgraphics")
        exclude("org.apache.xmlgraphics:fop")
        exclude("org.apache.xmlgraphics:batik-all")
        exclude("org.apache.xmlgraphics:batik-xml")
    }
    implementation("org.apache.xmlgraphics", "fop-pdf-images", "2.3")
    implementation("org.apache.xmlgraphics", "fop", "2.3")
    implementation("org.apache.xmlgraphics", "batik-all", "1.10")
    implementation("com.helger", "ph-schematron", "5.0.8") {
        exclude("com.helger:ph-jaxb")
        exclude("com.helger:ph-jaxb-pom")
        exclude("org.glassfish.jaxb:jaxb-bom")
    }
    implementation("org.xmlresolver", "xmlresolver", "0.14.0")

    implementation("org.asciidoctor", "asciidoctorj", "1.6.0-RC.2")

    // Use TestNG framework, also requires calling test.useTestNG() below
    testImplementation("org.testng:testng:6.14.3")
}

/*
application {
    // Define the main class for the application
    mainClassName = "com.github.aanno.dbtoolchain.App"
}
 */

val test by tasks.getting(Test::class) {
    // Use TestNG for unit tests
    useTestNG()
}

tasks {
    withType<Jar> {
        manifest {
            attributes(
                    mapOf(
                            "Main-Class" to "com.github.aanno.dbtoolchain.App"
                    // "Main-Class" to application.mainClassName
                    // "Class-Path" to configurations.compile.collect { it.getName() }.join(' ')
                    )
            )
        }
        val version = "1.0-SNAPSHOT"

        // archiveName = "${application.applicationName}-$version.jar"
        // from(configurations.compile.getAsMap().map { if (it.isDirectory) it else zipTree(it) })
    }

}

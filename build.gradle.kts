import org.javamodularity.moduleplugin.tasks.ModularJavaExec
import org.javamodularity.moduleplugin.tasks.ModularCreateStartScripts
import org.javamodularity.moduleplugin.tasks.PatchModuleExtension
import org.javamodularity.moduleplugin.tasks.ModuleOptions

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java project to get you started.
 * For more details take a look at the Java Quickstart chapter in the Gradle
 * user guide available at https://docs.gradle.org/5.0/userguide/tutorial_java_projects.html
 */

repositories {
    flatDir {
        dirs(
                "lib/prince-java/lib",
                "submodules/jing-trang/build/libs",
                "submodules/fop/fop/target",
                // lib/ueberjars",
                "lib/stripped",
                "submodules/batik/batik-all/target",
                "submodules/xslt20-stylesheets/build/libs",
                "submodules/asciidoctorj/asciidoctorj-core/build/libs",
                "submodules/asciidoctorj/asciidoctorj-api/build/libs"
        )
    }

    mavenLocal()
    mavenCentral()

    // Use jcenter for resolving your dependenes.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}


ext {
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

plugins {
    // Apply the java plugin to add support for Java
    // java
    `java-library`
    id("org.javamodularity.moduleplugin") version "1.4.0"

    // Apply the application plugin to add support for building an application
    application
    distribution
    idea
}

idea {
    module {
        setDownloadJavadoc(true)
        setDownloadSources(true)
    }
}

class ShowSelection {
    @Mutate
    fun evaluateRule(selection: ComponentSelection) {
        println("id: " + selection.candidate + " meta: " + selection.metadata?.attributes)
    }
}

configurations.all {
    // https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.ResolutionStrategy.html
    resolutionStrategy {
        preferProjectModules()

        // add dependency substitution rules
        dependencySubstitution {
            // fails with 'Project :asciidoctorj not found.'
            // substitute(module("org.asciidoctor:asciidoctorj")).with(project(":asciidoctorj"))

            // substitute project(':util') with module('org.gradle:util:3.0')
        }
        componentSelection {
            // all(ShowSelection())
        }

        // cache dynamic versions for 10 minutes
        cacheDynamicVersionsFor(10*60, "seconds")
        // don't cache changing modules at all
        cacheChangingModulesFor(60, "seconds")

        // https://docs.gradle.org/current/userguide/customizing_dependency_resolution_behavior.html
        resolutionStrategy.eachDependency {
            if (requested.name.startsWith("batik-") && requested.name != "batik-all") {
                useTarget(mapOf(
                        // "group" to requested.group,
                        "group" to "",
                        "name" to "batik-all",
                        // "version" to requested.version
                        "version" to "1.11.0-SNAPSHOT"
                ))
                because("""prefer "batik-all (stripped)" over "${requested.name}"""")
            }
        }
    }
    resolutionStrategy.setForcedModules(
            "net.sf.saxon:Saxon-HE:9.8.0-14"
            , "com.beust:jcommander:1.72"
            , "com.nwalsh:nwalsh-annotations:1.0.1"
            , "commons-codec:commons-codec:1.11"
            , "org.apache.httpcomponents:httpclient:4.5.6"
            , "org.apache.httpcomponents:httpcore:4.4.10"
            , "org.apache.xmlgraphics:fop:2.4.0-SNAPSHOT"
            , "org.apache.xmlgraphics:xmlgraphics-commons:2.4.0-SNAPSHOT"
            // , "xml-apis:xml-apis:1.4.01"
    )
    exclude("javax.servlet", "javax.servlet-api")
    exclude("xml-apis", "xml-apis")
    // exclude("xml-apis", "xml-apis-ext")
    exclude("xalan", "xalan")
    exclude("xerces", "xercesImpl")
    exclude("com.thaiopensource", "jing")
    exclude("", "jing")
    exclude("com.thaiopensource", "trang")
    exclude("", "trang")
    exclude("net.sf.saxon", "saxon")

    // TODO
    exclude("relaxngDatatype", "relaxngDatatype")
    // exclude("org.asciidoctor", "asciidoctorj-api")
    // exclude("org.asciidoctor", "asciidoctorj")
    exclude("commons-logging", "commons-logging")
    exclude("org.apache.avalon.framework", "avalon-framework-impl")
    // exclude("org.apache.avalon.framework", "avalon-framework-api")

    // if I add jnr-unixsocket, the world collapses:
    /*
error: the unnamed module reads package jnr.ffi from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.byref from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.annotations from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi.platform.x86_64.solaris from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi.platform.x86_64.windows from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi.platform.x86_64.darwin from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi.platform.x86_64.openbsd from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi.platform.x86_64.linux from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi.platform.x86_64.freebsd from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi.platform.sparc.solaris from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi.platform.aarch64.linux from both org.jruby and jnr.ffi
error: the unnamed module reads package jnr.ffi.provider.jffi.platform.arm.linux from both org.jruby and jnr.ffi
...
     */
    // exclude("com.github.jnr", "jnr-unixsocket")
    // error: the unnamed module reads package jnr.enxio.channels from both jnr.unixsocket and jnr.enxio
    exclude("com.github.jnr", "jnr-ffi")
    exclude("com.github.jnr", "jnr-enxio")

    exclude("com.xmlcalabash", "xmlcalabash1-gradle")
    exclude("com.xmlcalabash", "xmlcalabash1-print")

    // exclude super jars
    // exclude("org.apache.xmlgraphics", "batik-all")
    exclude("org.apache.xmlgraphics", "batik-ext")
    // depends on rhino (not in java 11 any more)
    exclude("org.apache.xmlgraphics", "batik-script")
    exclude("org.apache.xmlgraphics", "batik-constants")
}

dependencies {
    // taken from prince-java download at 'lib/prince-java/lib'

    api("", "prince", "")
    // compileClasspath("", "prince", "")
    // runtimeClasspath("", "prince", "")

    // asciidocj ueber jar
    // api("", "asciidocj", "")

    // build from submodule 'jing-trang'
    // api("", "jing", "")
    // api("", "trang", "")
    implementation("", "jingtrang", "")

    // api("", "xml-apis-stripped", "")
    api("", "asciidoctorj", "1.7.0-SNAPSHOT") {
        // exclude("org.asciidoctor", "asciidoctorj-api")
    }
    api("", "asciidoctorj-api", "1.7.0-SNAPSHOT")

    // java.lang.module.ResolutionException:
    // Modules jruby.complete and org.jruby export package org.jruby.runtime.backtrace to module nailgun.server
    api("org.asciidoctor", "asciidoctorj-pdf", "1.5.0-alpha.16") {
        exclude("org.jruby", "jruby")
        exclude("org.jruby", "jruby-complete")
    }

    // dependency of asciidocj and asciidocj-api
    api("org.jruby", "jruby", "9.2.5.0")
    api("com.github.jnr", "jnr-unixsocket", "0.2.1")
    // missing dep from jruby -> joni
    api("org.ow2.asm", "asm", "7.0")

    // This dependency is found on compile classpath of this component and consumers.
    // api("com.google.guava:guava:26.0-jre")

    /*
    api("org.docbook", "docbook-xslt2", "2.3.9") {
        exclude("org.xmlresolver", "xmlresolver")
        exclude("org.apache.xmlgraphics", "fop")
        // exclude("org.apache.xmlgraphics", "batik-all")
        // exclude("org.apache.xmlgraphics", "batik-xml")
        exclude("net.sf.saxon", "saxon")
        exclude("net.sf.saxon", "Saxon-HE")
        exclude("com.thaiopensource", "jing")
    }
     */
    api("", "docbook-xslt2", "2.3.9") {
        exclude("org.xmlresolver", "xmlresolver")
        exclude("org.apache.xmlgraphics", "fop")
        // exclude("org.apache.xmlgraphics", "batik-all")
        // exclude("org.apache.xmlgraphics", "batik-xml")
        exclude("net.sf.saxon", "saxon")
        exclude("net.sf.saxon", "Saxon-HE")
        exclude("com.thaiopensource", "jing")
    }
    api("com.xmlcalabash", "xmlcalabash", "1.1.23-98")
    api("commons-cli", "commons-cli", "1.4")

    api("net.sf.saxon", "Saxon-HE", "9.8.0-14")
    api("org.apache.xmlgraphics", "fop-pdf-images", "2.3") {
        exclude("xml-apis", "xml-apis")
        // exclude("xml-apis", "xml-apis-ext")
    }
    api("org.apache.xmlgraphics", "fop", "2.4.0-SNAPSHOT") {
        exclude("xml-apis", "xml-apis")
        // exclude("xml-apis", "xml-apis-ext")
    }
    api("org.apache.xmlgraphics", "xmlgraphics-commons", "2.4.0-SNAPSHOT")
    // pull in all deps (but batik-all will be excuded)
    api("", "batik-all", "1.11.0-SNAPSHOT")
    api("xml-apis", "xml-apis-ext", "1.3.04")

    api("com.helger", "ph-schematron", "5.0.8") {
        exclude("com.helger", "ph-jaxb")
        exclude("com.helger", "ph-jaxb-pom")
        exclude("org.glassfish.jaxb", "jaxb-bom")
    }
    api("org.xmlresolver", "xmlresolver", "0.14.0")
    api("net.sf.xslthl", "xslthl", "2.1.3")

    api("info.picocli", "picocli", "3.9.1")

    // Use TestNG framework, also requires calling test.useTestNG() below
    testImplementation("org.testng:testng:6.14.3")
}

/*
sourceSets {
    main {
        java {
            srcDirs 'src'
            srcDirs 'src1'
            srcDirs 'src2'
        }
    }
}
 */

application {
    // Define the main class for the application
    mainClassName = "com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.App"
}

val test by tasks.getting(Test::class) {
    // Use TestNG for unit tests
    useTestNG()
}

var spec2File: Map<String, File> = emptyMap()
configurations.forEach({c -> println(c)})
// TODO: get name of configuration (gradle dependencies)
configurations.compileClasspath {
    val s2f: MutableMap<ResolvedModuleVersion, File> = mutableMapOf()
    // https://discuss.gradle.org/t/map-dependency-instances-to-file-s-when-iterating-through-a-configuration/7158
    resolvedConfiguration.resolvedArtifacts.forEach({ ra: ResolvedArtifact ->
        // println(ra.moduleVersion.toString() + " -> " + ra.file)
        s2f.put(ra.moduleVersion, ra.file)
    })
    spec2File = s2f.mapKeys({"${it.key.id.group}:${it.key.id.name}"})
    spec2File.keys.sorted().forEach({ it -> println(it.toString() + " -> " + spec2File.get(it))})
}

/*
val patchModule = listOf(
        "--patch-module", "commons.logging=" +
        spec2File["org.slf4j:jcl-over-slf4j"].toString(),

        "--patch-module", "org.apache.commons.logging=" +
        spec2File["org.slf4j:jcl-over-slf4j"].toString(),

        "--patch-module", "jcl.over.slf4j=" +
        spec2File["commons-logging:commons-logging"].toString(),

        "--patch-module", "asciidoctorj.api=" +
        spec2File["org.asciidoctor:asciidoctorj"].toString(),

        "--patch-module", "asciidoctorj=" +
        spec2File["org.asciidoctor:asciidoctorj-api"].toString(),

        "--patch-module", "xmlcalabash=" +
        spec2File["com.xmlcalabash:xmlcalabash"].toString() +
        ":" + spec2File["com.xmlcalabash:xmlcalabash1-gradle"].toString(),

        "--patch-module", "xmlcalabash1.print=" +
        spec2File["com.xmlcalabash:xmlcalabash"].toString() +
        ":" + spec2File["com.xmlcalabash:xmlcalabash1-gradle"].toString(),

        "--patch-module", "jnr.enxio=" +
        spec2File["com.github.jnr:jnr-unixsocket"].toString() +
        ":" + spec2File["com.github.jnr:jnr-enxio"].toString(),

        "--patch-module", "jnr.unixsocket=" +
        spec2File["com.github.jnr:jnr-enxio"].toString() +
        ":" + spec2File["com.github.jnr:jnr-unixsocket"].toString(),

        "--patch-module", "avalon.framework.impl=" +
        spec2File["org.apache.avalon.framework:avalon-framework-api"].toString(),

        "--patch-module", "avalon.framework.api=" +
        spec2File["org.apache.avalon.framework:avalon-framework-impl"].toString()
)
*/
patchModules.config = listOf(
            "commons.logging=" + spec2File["org.slf4j:jcl-over-slf4j"].toString()
            // , "jing=" + spec2File[":trang"].toString()
            , "jnr.unixsocket=jnr-enxio-0.19.jar"
)
println("\npatchModules.config:\n")
patchModules.config.forEach({it -> println(it)})

tasks {

    withType<JavaCompile> {

            doFirst {
                options.compilerArgs.addAll(listOf(
                        "--release", "11"
                        // , "-cp", "jnr-enxio-0.19.jar"
                        // , "--add-modules", "ALL-MODULE-PATH",
                        // , "--module-path", classpath.asPath
                ) /*+ patchModule */)
                println("Args for for ${name} are ${options.allCompilerArgs}")
            }

            // classpath.forEach({it -> println(it)})
    }

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

    // https://stackoverflow.com/questions/51810254/execute-javaexec-task-using-gradle-kotlin-dsl
    // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html
    // https://stackoverflow.com/questions/11696521/how-to-pass-arguments-from-command-line-to-gradle
    task("runApp1", ModularJavaExec::class) {
        main = "com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.App"
        // classpath = sourceSets["main"].runtimeClasspath
    }

    val ad = task("runAsciidoctor", ModularJavaExec::class) {
        main = "com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.AsciidoctorJ"
        // classpath = sourceSets["main"].runtimeClasspath
    }

    val adStart = task("createStartScriptAsciidoctor", ModularCreateStartScripts::class) {
        runTask = ad
        applicationName = "ad"
    }

    task("runAdSvgTest", ModularJavaExec::class) {
        main = "com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.AdSvgTest"
        // classpath = sourceSets["main"].runtimeClasspath
    }

    task("runDbValidation", ModularJavaExec::class) {
        main = "com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.DbValidation"
        // classpath = sourceSets["main"].runtimeClasspath
    }

    // https://stackoverflow.com/questions/52596968/build-source-jar-with-gradle-kotlin-dsl
    val sourcesJar by registering(Jar::class) {
        classifier = "sources"
        from(sourceSets.main.get().allSource)
        dependsOn(classes)
    }

    val javadocJar by registering(Jar::class) {
        classifier = "javadoc"
        from(javadoc.get().destinationDir)
        dependsOn(classes)
    }

    artifacts {
        add("archives", sourcesJar)
        add("archives", javadocJar)
    }

    installDist {
        finalizedBy(adStart)
    }

    /*
    build {
        dependsOn(gradle.includedBuild("jingtrang").task(":jingtrang"))
    }
     */

}

import org.javamodularity.moduleplugin.tasks.ModularCreateStartScripts
import org.javamodularity.moduleplugin.tasks.ModularJavaExec
import kotlin.text.Regex

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java project to get you started.
 * For more details take a look at the Java Quickstart chapter in the Gradle
 * user guide available at https://docs.gradle.org/5.0/userguide/tutorial_java_projects.html
 */

plugins {
    // Apply the java plugin to add support for Java
    // java
    `java-library`
    id("org.javamodularity.moduleplugin") version "1.6.0"
    id("com.github.ben-manes.versions") version "0.27.0"
    id("com.github.jruby-gradle.base") version "2.0.0"

    // Apply the application plugin to add support for building an application
    application
    distribution
    idea
}

jruby {
    setDefaultRepositories(false)
}

apply {
    // NOT recommended
    // see https://kotlinlang.org/docs/reference/using-gradle.html#using-gradle-kotlin-dsl
    plugin("com.github.jruby-gradle.base")
}

repositories {
    flatDir {
        dirs(
                "lib/prince-java/lib",
                "submodules/jing-trang/build/libs",
                // "submodules/fop/fop/target",
                "lib/ueberjars",
                "build/libs",
                "lib/stripped",
                "lib",
                // "submodules/batik/batik-all/target",
                "submodules/xslt20-stylesheets/build/libs",
                "submodules/ph-schematron/ph-schematron/target",
                "submodules/asciidoctorj/asciidoctorj-core/build/libs",
                "submodules/asciidoctorj/asciidoctorj-api/build/libs"
        )
    }

    mavenLocal()
    mavenCentral()

    // Use jcenter for resolving your dependenes.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    // for use with https://github.com/jruby/rubygems-servlets
    // (as rubygems("https://rubygems.org") does NOT work)
    /*
    maven {
        url = uri("http://localhost:8989/caching/maven/releases")
    }
     */
    // see http://jruby-gradle.org/base/ (but not working)
    // rubygems("https://rubygems.org")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

idea {
    module {
        setDownloadJavadoc(true)
        setDownloadSources(true)
    }
}

val xercesVersion = "2.12.0"
val debugModulePath = true
val moduleJvmArgs = listOf(
        "--add-exports=java.xml/com.sun.org.apache.xerces.internal.parsers=com.github.aanno.dbtoolchain"
)

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
        cacheDynamicVersionsFor(10 * 60, "seconds")
        // don't cache changing modules at all
        cacheChangingModulesFor(60, "seconds")

        // https://docs.gradle.org/current/userguide/customizing_dependency_resolution_behavior.html
        resolutionStrategy.eachDependency {
            if (requested.name.startsWith("batik-") && requested.name != "batik-all") {
                useTarget(mapOf(
                        // "group" to requested.group,
                        "group" to "org.apache.xmlgraphics",
                        "name" to "batik-all",
                        // "version" to requested.version
                        "version" to "1.12"
                ))
                because("""prefer "batik-all (stripped)" over "${requested.name}"""")
            }
            if (requested.name.startsWith("fop-") && requested.name != "fop") {
                useTarget(mapOf(
                        // "group" to requested.group,
                        "group" to "org.apache.xmlgraphics",
                        "name" to "fop",
                        // "version" to requested.version
                        "version" to "2.4"
                ))
                because("""prefer "fop (all, stripped)" over "${requested.name}"""")
            }
        }
    }
    resolutionStrategy.setForcedModules(
            "net.sf.saxon:Saxon-HE:9.9.1-6"
            , "com.nwalsh:nwalsh-annotations:1.0.1"
            , "commons-codec:commons-codec:1.14"
            , "org.apache.httpcomponents:httpclient:4.5.11"
            , "org.apache.httpcomponents:httpcore:4.4.13"
            , "org.apache.xmlgraphics:fop:2.4"
            , "org.apache.xmlgraphics:xmlgraphics-commons:2.4"
            // , "xml-apis:xml-apis:1.4.01"
    )
    exclude("javax.servlet", "javax.servlet-api")
    exclude("xml-apis", "xml-apis")
    // exclude("xml-apis", "xml-apis-ext")
    exclude("xalan", "xalan")
    exclude("xalan", "serializer")
    // exclude("xerces", "xercesImpl")
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
    exclude("org.apache.avalon.framework", "avalon-framework-api")

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

    if (!name.equals("ueberjars")) {
        exclude("com.github.jnr", "jnr-enxio")
        exclude("com.github.jnr", "jnr-unixsocket")
        exclude("xerces", "xercesImpl")
    }

    exclude("org.jruby", "jruby-complete")

    exclude("com.xmlcalabash", "xmlcalabash1-gradle")
    exclude("com.xmlcalabash", "xmlcalabash1-print")
    // don't do!!!
    // exclude("com.xmlcalabash", "xmlcalabash1-mathml-to-svg")
    // exclude("com.xmlcalabash", "xmlcalabash1-xslthl")

    // exclude super jars
    // exclude("org.apache.xmlgraphics", "batik-all")
    exclude("org.apache.xmlgraphics", "batik-ext")
    // depends on rhino (not in java 11 any more)
    exclude("org.apache.xmlgraphics", "batik-script")
    exclude("org.apache.xmlgraphics", "batik-constants")

    exclude("org.apache.logging.log4j", "log4j-slf4j-impl")
    exclude("org.restlet.jee", "org.restlet.ext.slf4j")
    // exclude("org.slf4j", "jcl-over-slf4j")
    exclude("org.apache.logging.log4j", "log4j")
    exclude("org.apache.logging.log4j", "log4j-core")

    // exclude old resolvers (use java 11 resolver)
    exclude("xml-resolver", "xml-resolver")
    // can't be excluded because it is used by xmlcalabash
    // exclude("org.xmlresolver", "xmlresolver")

    // from xmlcalabash
    exclude("net.java.dev.msv", "msv-core")
    exclude("com.ibm.icu", "icu4j")
    exclude("org.apache.ant", "ant")
    exclude("org.ccil.cowan.tagsoup", "tagsoup")
    exclude("org.restlet.jee")
    exclude("commons-fileupload", "commons-fileupload")
    exclude("com.atlassian.commonmark", "commonmark")

    // exclude this for new jeuclid fork
    exclude("net.sourceforge.jeuclid", "jeuclid-core")
    // TODO tp: Is this needed?
    exclude("net.sourceforge.jeuclid", "jeuclid-fop")
}
val ueberjars = configurations.create("ueberjars")

dependencies {
    // taken from prince-java download at 'lib/prince-java/lib'

    api("", "prince", "")

    // TODO: This is hacky as it trashes the first build after clean
    if (file("build/libs/xerces-stripped.jar").exists()) {
        api("", "xerces-stripped", "")
    }

    // compileClasspath("", "prince", "")
    // runtimeClasspath("", "prince", "")

    // asciidocj ueber jar
    // api("", "asciidocj", "")

    // build from submodule 'jing-trang'
    // api("", "jing", "")
    // api("", "trang", "")
    implementation("", "jingtrang", "")

    // api("", "xml-apis-stripped", "")
    api("", "asciidoctorj", "2.3.0-SNAPSHOT") {
        // exclude("org.asciidoctor", "asciidoctorj-api")
    }
    api("", "asciidoctorj-api", "2.3.0-SNAPSHOT")

    // java.lang.module.ResolutionException:
    // Modules jruby.complete and org.jruby export package org.jruby.runtime.backtrace to module nailgun.server
    api("org.asciidoctor", "asciidoctorj-pdf", "1.5.0") {
        exclude("org.jruby", "jruby")
        exclude("org.jruby", "jruby-complete")
    }

    // dependency of asciidocj and asciidocj-api
    api("org.jruby", "jruby", "9.2.9.0")
    api("com.github.jnr", "jnr-unixsocket", "0.26")
    api("com.github.jnr", "jnr-enxio", "0.24")
    implementation("", "jnrchannels", "")
    implementation("", "xmlcalabash-extensions", "")
    // missing dep from jruby -> joni
    api("org.ow2.asm", "asm", "7.3.1")

    // This dependency is found on compile classpath of this component and consumers.
    // api("com.google.guava:guava:26.0-jre")

    /*
    api("org.docbook", "docbook-xslt2", "2.3.10") {
        exclude("org.xmlresolver", "xmlresolver")
        exclude("org.apache.xmlgraphics", "fop")
        // exclude("org.apache.xmlgraphics", "batik-all")
        // exclude("org.apache.xmlgraphics", "batik-xml")
        exclude("net.sf.saxon", "saxon")
        exclude("net.sf.saxon", "Saxon-HE")
        exclude("com.thaiopensource", "jing")
    }
     */
    api("", "docbook-xslt2", "2.4.3") {
        exclude("org.xmlresolver", "xmlresolver")
        exclude("org.apache.xmlgraphics", "fop")
        // exclude("org.apache.xmlgraphics", "batik-all")
        // exclude("org.apache.xmlgraphics", "batik-xml")
        exclude("net.sf.saxon", "saxon")
        exclude("net.sf.saxon", "Saxon-HE")
        exclude("com.thaiopensource", "jing")
    }
    api("com.xmlcalabash", "xmlcalabash", "1.1.30-99") {
        exclude("junit", "junit")
        exclude("nu.validator.htmlparser", "htmlparser")
    }

    // Needed for docbook-xslt20 Main.main (tp)
    api("commons-cli", "commons-cli", "1.4")

    // Needed for xmlcalabash (and original docbook-xslt20 Main.main) (tp)
    api("org.xmlresolver", "xmlresolver", "1.0.6")

    api("net.sf.saxon", "Saxon-HE", "9.9.1-6")
    api("org.apache.xmlgraphics", "fop-pdf-images", "2.4") {
        exclude("xml-apis", "xml-apis")
        // exclude("xml-apis", "xml-apis-ext")
    }
    api("org.apache.xmlgraphics", "fop-core", "2.4") {
        exclude("xml-apis", "xml-apis")
        // exclude("xml-apis", "xml-apis-ext")
    }
    api("org.apache.xmlgraphics", "xmlgraphics-commons", "2.4.0")
    // pull in all deps (but batik-all will be excuded)
    api("org.apache.xmlgraphics", "batik-all", "1.12")
    api("xml-apis", "xml-apis-ext", "1.3.04")
    api("xerces", "xercesImpl", xercesVersion)

    api("com.helger", "ph-schematron", "5.4.1-SNAPSHOT") {
        exclude("com.helger", "ph-jaxb")
        exclude("com.helger", "ph-jaxb-pom")
        exclude("org.glassfish.jaxb", "jaxb-bom")
        exclude("com.google.code.findbugs", "jsr305")
    }
    api("com.helger", "ph-commons", "9.3.9") {
        exclude("com.google.code.findbugs", "jsr305")
    }
    api("com.helger", "ph-xml", "9.3.9") {
        exclude("com.google.code.findbugs", "jsr305")
    }
    // api("xml-resolver", "xml-resolver", "1.2")

    api("info.picocli", "picocli", "4.2.0")

    implementation("org.slf4j", "slf4j-simple", "1.7.30")

    ueberjars("com.github.jnr", "jnr-enxio", "0.24")
    ueberjars("com.github.jnr", "jnr-unixsocket", "0.26")
    ueberjars("xerces", "xercesImpl", xercesVersion)

    ueberjars("com.xmlcalabash", "xmlcalabash1-mathml-to-svg", "1.1.3")
    api("de.rototor.jeuclid:jeuclid-core:3.1.14")
    // TODO tp: Is this needed?
    api("de.rototor.jeuclid:jeuclid-fop:3.1.14")

    ueberjars("com.xmlcalabash", "xmlcalabash1-xslthl", "1.0.0")
    api("net.sf.xslthl", "xslthl", "2.1.3")

    // needed to find xmlcalabash extension steps _in our code_
    api("org.atteo.classindex", "classindex", "3.4")
    annotationProcessor("org.atteo.classindex", "classindex", "3.4")

    // Use TestNG framework, also requires calling test.useTestNG() below
    // testImplementation("org.testng:testng:7.1.0")
    testImplementation("org.testng:testng:6.14.0")

    // TODO tp: Not in use
    gems("rubygems:asciimath:1.0.9")
    gems("rubygems:asciidoctor-epub3:1.5.0.alpha.13")
    gems("rubygems:asciidoctor-diagram:2.0.1")
    gems("rubygems:asciidoctor-latex:1.5.0.17.dev")
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
    jvmArgs(moduleJvmArgs)
}

var spec2File: Map<String, File> = emptyMap()
configurations.forEach({ c -> println(c) })
// TODO: get name of configuration (gradle dependencies)
configurations.compileClasspath {
    val s2f: MutableMap<ResolvedModuleVersion, File> = mutableMapOf()
    // https://discuss.gradle.org/t/map-dependency-instances-to-file-s-when-iterating-through-a-configuration/7158
    resolvedConfiguration.resolvedArtifacts.forEach({ ra: ResolvedArtifact ->
        // println(ra.moduleVersion.toString() + " -> " + ra.file)
        s2f.put(ra.moduleVersion, ra.file)
    })
    spec2File = s2f.mapKeys({ "${it.key.id.group}:${it.key.id.name}" })
    spec2File.keys.sorted().forEach({ it -> println(it.toString() + " -> " + spec2File.get(it)) })
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
        // , "jnr.unixsocket=jnr-enxio-0.19.jar"
)
println("\npatchModules.config:\n")
patchModules.config.forEach({ it -> println(it) })

tasks {

    withType<JavaCompile> {

        doFirst {
            options.compilerArgs.addAll(listOf(
                    // "--release", "11"
                    "--add-exports=java.xml/com.sun.org.apache.xerces.internal.parsers=com.github.aanno.dbtoolchain"
                    // , "--add-modules jnr.enxio"
                    // , "-cp", "jnr-enxio-0.19.jar"
                    // , "--add-modules", "ALL-MODULE-PATH",
                    // , "--module-path", classpath.asPath
            ) + moduleJvmArgs /*+ patchModule */)
            println("Args for for ${name} are ${options.allCompilerArgs}")
        }
        // HACK: adding 'submodules/jing-trang' as composite results in
        //       java 11 module resolution error for 'requires jingtrang;'
        classpath += layout.files("submodules/jing-trang/build/libs/jingtrang.jar")

        // classpath.forEach({it -> println(it)})

        doLast {
            if (debugModulePath) {
                println("Args for for ${name} are ${options.allCompilerArgs}")
            }
        }
    }

    withType<ModularJavaExec> {
        doFirst {
            val myArgs = listOf(
                    "-Duser.country=US", "-Duser.language=en",
                    "--show-module-resolution", "--add-opens java.base/sun.nio.ch=org.jruby.core"
            )
            jvmArgs!!.addAll(myArgs)
        }
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

    task("moreClean", Delete::class) {
        delete("lib/tmp/")
        doLast {
            File("lib/tmp").mkdirs()
            // File("build/libs").mkdirs()
            // File("build/libs/xerces-stripped.jar").createNewFile()
        }
    }

    // https://stackoverflow.com/questions/51810254/execute-javaexec-task-using-gradle-kotlin-dsl
    // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.JavaExec.html
    // https://stackoverflow.com/questions/11696521/how-to-pass-arguments-from-command-line-to-gradle
    task("runApp1", ModularJavaExec::class) {
        doFirst {
            // TODO tp: all this jvmArgs are ignored - but why?
            // val oldArgs: List<String> = (if (jvmArgs != null) jvmArgs else emptyList()) as List<String>
            jvmArgs("""--illegal-access=warn
--show-module-resolution 
--add-opens java.base/sun.nio.ch=org.jruby.core
--add-opens java.base/sun.nio.ch=backport9
""".split(Regex("[ \n\t]+"))
                    )
            if (debugModulePath) {
                println("${name}: jmvArgs: ${jvmArgs}\nargs: ${args}")
            }
        }
        main = "com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.App"
        args("transform -d . -w submodules/asciidoctorj/asciidoctorj-documentation --pipeline ad -of PDF -i submodules/asciidoctorj/asciidoctorj-documentation/src/main/asciidoc/integrator-guide.adoc"
                .split(Regex("[ \n\t]+"))
        )
        // classpath = sourceSets["main"].runtimeClasspath

        doLast {
            if (debugModulePath) {
                println("${name}: jmvArgs: ${jvmArgs}\nargs: ${args}")
            }
        }

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

    val copyJarsForUeberJars = task("copyJarsForUeberJars", Copy::class) {
        val ueberBaseFiles = configurations.get("ueberjars").resolvedConfiguration.files
        println("ueberBaseFiles: " + ueberBaseFiles)
        from(ueberBaseFiles) {
            rename("([a-zA-Z_]+)-([\\d\\.]+(.*)).jar", "$1.jar")
        }
        into("./lib/tmp")
    }

    val unzipXerces = task("unzipXerces", Copy::class) {
        from(zipTree(file("lib/tmp/xercesImpl.jar"))) {
            exclude("org/w3c/**/*")
        }
        into("./lib/tmp/xercesImpl")
        dependsOn(copyJarsForUeberJars)
    }

    val rezipStrippedXerces = task("rezipStrippedXerces", Jar::class) {
        baseName = "xerces-stripped"
        from(files("./lib/tmp/xercesImpl")) {
        }
        dependsOn(unzipXerces)
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

}

/*
build {
    dependsOn(gradle.includedBuild("jingtrang").task(":jingtrang"))
}
 */
// https://docs.gradle.org/current/userguide/kotlin_dsl.html#using_the_container_api
tasks.named("build") {
    dependsOn(":copyJarsForUeberJars")
}

tasks.named("clean") {
    dependsOn(":moreClean")
}

tasks.named("compileJava") {
    dependsOn(gradle.includedBuild("jingtrang").task(":build"))
    dependsOn(":rezipStrippedXerces")
}

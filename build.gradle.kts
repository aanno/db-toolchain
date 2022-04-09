import org.javamodularity.moduleplugin.tasks.ModularCreateStartScripts
import org.javamodularity.moduleplugin.tasks.ModularJavaExec
import kotlin.text.Regex

val batik_version: String by project
val fop_version: String by project
val xmlgraphics_common_version: String by project
val xerces_version: String by project
val jffi_version: String by project
val saxon_version: String by project
val nwalsh_annotations_version: String by project
val commons_codec_version: String by project
val commons_cli_version: String by project
val httpclient_version: String by project
val httpcore_version: String by project
val testng_version: String by project
val snakeyaml_version: String by project
val jruby_version: String by project
val asm_version: String by project
val jnr_unixsocket_version: String by project
val jnr_enxio_version: String by project
val picocli_version: String by project
val slf4j_version: String by project
val jeuclid_version: String by project
val xslthl_version: String by project
val classindex_version: String by project

val asciidoctorj_version: String by project
val asciidoctorj_pdf_version: String by project
val docbook_xslt2_version: String by project
val xmlcalabash_version: String by project
val xmlresolver_version: String by project
val xml_apis_ext_version: String by project

val ph_schematron_version: String by project
val ph_commons_version: String by project

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
    id("org.javamodularity.moduleplugin") version "1.8.10"
    id("com.github.ben-manes.versions") version "0.42.0"
    id("com.github.jruby-gradle.base") version "2.1.0-alpha.2"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"

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
                "submodules/jing-trang/build/libs",
                // "submodules/fop/fop/target",
                // "lib/ueberjars",
                "build/libs",
                // "lib/stripped",
                // TODO tp: docbook-xslt2-2.4.3.jar
                "lib",
                // "submodules/batik/batik-all/target",
                "submodules/xslt20-stylesheets/build/libs",
                "submodules/ph-schematron/ph-schematron-pure/target",
                "submodules/ph-schematron/ph-schematron-api/target",
                "submodules/ph-commons/ph-commons/target",
                "submodules/ph-commons/ph-xml/target"
                // "submodules/asciidoctorj/asciidoctorj-core/build/libs",
                // "submodules/asciidoctorj/asciidoctorj-api/build/libs"
        )
    }

    mavenLocal()
    mavenCentral()

    // Use jcenter for resolving your dependenes.
    // You can declare any Maven/Ivy/file repository here.
    // jcenter()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    /*
    maven {
        url = uri("https://jitpack.io")
    }
     */
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

group = "com.github.aanno"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

idea {
    module {
        setDownloadJavadoc(true)
        setDownloadSources(true)
    }
}

evaluationDependsOnChildren()

val xercesVersion = xerces_version
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

// HACK: force access to gradle plugins from within idea
val gradlePlugins = configurations.create("gradlePlugins")

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
                        "version" to batik_version
                ))
                because("""prefer "batik-all (stripped)" over "${requested.name}"""")
            }
            if (requested.name.startsWith("fop-") && requested.name != "fop") {
                useTarget(mapOf(
                        // "group" to requested.group,
                        "group" to "org.apache.xmlgraphics",
                        "name" to "fop",
                        // "version" to requested.version
                        "version" to fop_version
                ))
                because("""prefer "fop (all, stripped)" over "${requested.name}"""")
            }
            /*
            if (requested.group.equals("com.github.jnr") && requested.name.startsWith("jffi-")) {
                useTarget(mapOf(
                        // "group" to requested.group,
                        "group" to requested.group,
                        "name" to "jffi",
                        // "version" to requested.version
                        "version" to jffi_version,
                        "classifier" to "native"
                ))
                because("""prefer jffi native over jffi""")
            }
             */
        }
    }
    resolutionStrategy.setForcedModules(
            "net.sf.saxon:Saxon-HE:${saxon_version}"
            , "com.nwalsh:nwalsh-annotations:${nwalsh_annotations_version}"
            , "commons-codec:commons-codec:${commons_codec_version}"
            , "org.apache.httpcomponents:httpclient:${httpclient_version}"
            , "org.apache.httpcomponents:httpcore:${httpcore_version}"
            , "org.apache.xmlgraphics:fop:${fop_version}"
            , "org.apache.xmlgraphics:xmlgraphics-commons:${xmlgraphics_common_version}"
            // testng changed modulename: testng -> org.testng in 7.3.x (tp)
            , "org.testng:testng:${testng_version}"
            , "org.yaml:snakeyaml:${snakeyaml_version}"
            , "org.xmlresolver:xmlresolver:${xmlresolver_version}"
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
    exclude("com.github.jnr:jffi")

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
    // exclude("com.github.jnr", "jnr-ffi")

    exclude("com.github.jnr", "jffi")

    exclude("com.github.jnr", "jnr-enxio")
    exclude("com.github.jnr", "jnr-unixsocket")
    exclude("xerces", "xercesImpl")

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
    // use splitjar xmlresolver
    // can't really be excluded because it is used by xmlcalabash
    exclude("org.xmlresolver", "xmlresolver")

    // from xmlcalabash
    exclude("net.java.dev.msv", "msv-core")
    exclude("com.ibm.icu", "icu4j")
    exclude("org.apache.ant", "ant")
    exclude("org.ccil.cowan.tagsoup", "tagsoup")
    exclude("org.restlet.jee")
    exclude("commons-fileupload", "commons-fileupload")
    exclude("com.atlassian.commonmark", "commonmark")

    // split with javax.annotations (tp)
    exclude("com.google.code.findbugs", "jsr305")

    // exclude this for new jeuclid fork
    exclude("net.sourceforge.jeuclid", "jeuclid-core")
    // TODO tp: Is this needed?
    exclude("net.sourceforge.jeuclid", "jeuclid-fop")
}

dependencies {
    gradlePlugins("org.javamodularity.moduleplugin", "org.javamodularity.moduleplugin.gradle.plugin", "1.8.10")
    gradlePlugins("com.github.ben-manes.versions", "com.github.ben-manes.versions.gradle.plugin", "0.42.0")
    gradlePlugins("se.patrikerdes.use-latest-versions", "se.patrikerdes.use-latest-versions.gradle.plugin", "0.2.18")
    gradlePlugins("com.github.jruby-gradle.base", "com.github.jruby-gradle.base.gradle.plugin", "2.1.0-alpha.2")

    // taken from prince-java download at 'lib/prince-java/lib'

    api("com.princexml", "prince-java-wrapper", "1.2.0")
    api(project("splitjars", "xerces"))

    // TODO: This is hacky as it trashes the first build after clean
    /*
    if (file("build/libs/xerces-stripped.jar").exists()) {
        api("", "xerces-stripped", "")
    }
     */

    // compileClasspath("", "prince", "")
    // runtimeClasspath("", "prince", "")

    // asciidocj ueber jar
    // api("", "asciidocj", "")

    // build from submodule 'jing-trang'
    // api("", "jing", "")
    // api("", "trang", "")
    implementation("", "jingtrang", "")

    // api("", "xml-apis-stripped", "")
    api("org.asciidoctor", "asciidoctorj", "${asciidoctorj_version}") {
        // exclude("org.asciidoctor", "asciidoctorj-api")
    }
    api("org.asciidoctor", "asciidoctorj-api", "${asciidoctorj_version}")

    // java.lang.module.ResolutionException:
    // Modules jruby.complete and org.jruby export package org.jruby.runtime.backtrace to module nailgun.server
    api("org.asciidoctor", "asciidoctorj-pdf", "${asciidoctorj_pdf_version}") {
        exclude("org.jruby", "jruby")
        exclude("org.jruby", "jruby-complete")
    }

    // dependency of asciidocj and asciidocj-api
    api("org.jruby", "jruby", "${jruby_version}")
    // needed for jruby >=9.3 (?)
    // implementation("com.headius:options:1.6")
    api("com.github.jnr:jffi:${jffi_version}") {
        artifact {
            setName("jffi")
            // setGroup("com.github.jnr")
            // setExtension("jar")
            setType("jar")
            setClassifier("native")
        }
    }
    api("com.github.jnr", "jnr-unixsocket", "${jnr_unixsocket_version}")
    api("com.github.jnr", "jnr-enxio", "${jnr_enxio_version}")
    api(project("splitjars", "jnrchannels"))
    // implementation("", "jnrchannels", "")
    // implementation("", "xmlcalabash-extensions", "")
    api(project("splitjars", "calabashExt"))
    // missing dep from jruby -> joni
    api("org.ow2.asm", "asm", "${asm_version}")

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
    api("", "docbook-xslt2", "${docbook_xslt2_version}") {
        exclude("org.xmlresolver", "xmlresolver")
        exclude("org.apache.xmlgraphics", "fop")
        // exclude("org.apache.xmlgraphics", "batik-all")
        // exclude("org.apache.xmlgraphics", "batik-xml")
        exclude("net.sf.saxon", "saxon")
        exclude("net.sf.saxon", "Saxon-HE")
        exclude("com.thaiopensource", "jing")
    }
    api("com.xmlcalabash", "xmlcalabash", "${xmlcalabash_version}") {
        exclude("junit", "junit")
        exclude("nu.validator.htmlparser", "htmlparser")
    }

    // Needed for docbook-xslt20 Main.main (tp)
    api("commons-cli", "commons-cli", "${commons_cli_version}")

    // Needed for xmlcalabash (and original docbook-xslt20 Main.main) (tp)
    // api("org.xmlresolver", "xmlresolver", "${xmlresolver_version}")
    // use splitjar version
    api(project("splitjars", "xmlresolver"))

    // use splitjar version
    api(project("splitjars", "jffi"))

    api("net.sf.saxon", "Saxon-HE", "${saxon_version}")
    api("org.apache.xmlgraphics", "fop-pdf-images", "${fop_version}") {
        exclude("xml-apis", "xml-apis")
        // exclude("xml-apis", "xml-apis-ext")
    }
    api("org.apache.xmlgraphics", "fop-core", "${fop_version}") {
        exclude("xml-apis", "xml-apis")
        // exclude("xml-apis", "xml-apis-ext")
    }
    api("org.apache.xmlgraphics", "xmlgraphics-commons", "${xmlgraphics_common_version}")
    // pull in all deps (but batik-all will be excuded)
    api("org.apache.xmlgraphics", "batik-all", "${batik_version}")
    api("xml-apis", "xml-apis-ext", "${xml_apis_ext_version}")
    api("xerces", "xercesImpl", xercesVersion)

    api("com.helger.schematron", "ph-schematron-pure", "${ph_schematron_version}") {
        exclude("com.helger.commons", "ph-jaxb")
        exclude("com.helger.commons", "ph-jaxb-pom")
        exclude("org.glassfish.jaxb", "jaxb-bom")
        exclude("com.google.code.findbugs", "jsr305")
    }
    api("com.helger.schematron", "ph-schematron-api", "${ph_schematron_version}") {
        exclude("com.helger.commons", "ph-jaxb")
        exclude("com.helger.commons", "ph-jaxb-pom")
        exclude("org.glassfish.jaxb", "jaxb-bom")
        exclude("com.google.code.findbugs", "jsr305")
    }
    api("com.helger.commons", "ph-commons", "${ph_commons_version}") {
        exclude("com.google.code.findbugs", "jsr305")
    }
    api("com.helger.commons", "ph-xml", "${ph_commons_version}") {
        exclude("com.google.code.findbugs", "jsr305")
    }
    // api("xml-resolver", "xml-resolver", "1.2")

    api("info.picocli", "picocli", "${picocli_version}")

    implementation("org.slf4j", "slf4j-simple", "${slf4j_version}")

    // ueberjars("com.xmlcalabash", "xmlcalabash1-mathml-to-svg", "1.1.3")
    api("de.rototor.jeuclid:jeuclid-core:${jeuclid_version}")
    // TODO tp: Is this needed?
    api("de.rototor.jeuclid:jeuclid-fop:${jeuclid_version}")

    // ueberjars("com.xmlcalabash", "xmlcalabash1-xslthl", "1.0.0")
    api("net.sf.xslthl", "xslthl", "${xslthl_version}")

    // needed to find xmlcalabash extension steps _in our code_
    api("org.atteo.classindex", "classindex", "${classindex_version}")
    annotationProcessor("org.atteo.classindex", "classindex", "${classindex_version}")

    implementation("org.docbook:docbook-xslTNG:1.6.2")

    // Use TestNG framework, also requires calling test.useTestNG() below
    // testImplementation("org.testng:testng:7.1.0")
    // implementation("org.testng:testng:${testng_version}")
    testImplementation("org.testng:testng:${testng_version}")

    // TODO tp: Not in use - use https://rubygems.org/ for versions
    gems("rubygems:asciimath:2.0.2")
    gems("rubygems:asciidoctor-epub3:1.5.0.alpha.19")
    gems("rubygems:asciidoctor-diagram:2.1.0")
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

    wrapper {
        distributionType = Wrapper.DistributionType.ALL
        version = "7.4.2"
    }

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
        val version = "1.0.0-SNAPSHOT"

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

    /*
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
     */

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
    dependsOn(":splitjars:copyJarsForUeberJars")
}

tasks.named("clean") {
    dependsOn(":moreClean")
}

tasks.named("compileJava") {
    dependsOn(gradle.includedBuild("jing-trang").task(":build"))
    dependsOn(":splitjars:build")
}

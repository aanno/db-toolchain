
val xerces_version: String by project
val jnr_unixsocket_version: String by project
val jnr_enxio_version: String by project
val xmlresolver_version: String by project

val ueberjars = configurations.create("ueberjars")
val xerces = configurations.create("xerces")
val jnrchannels = configurations.create("jnrchannels")
val calabashExt = configurations.create("calabashExt")
val xmlresolver = configurations.create("xmlresolver")

plugins {
    `java`
}

repositories {
    mavenLocal()
    mavenCentral()

    // Use jcenter for resolving your dependenes.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    /*
    repositories {
        maven {
            url = uri("https://repo.boundlessgeo.com/main/")
        }
    }
     */
}

dependencies {
    ueberjars("com.github.jnr", "jnr-enxio", jnr_enxio_version)
    ueberjars("com.github.jnr", "jnr-unixsocket", jnr_unixsocket_version)
    ueberjars("xerces", "xercesImpl", xerces_version)
    ueberjars("com.xmlcalabash", "xmlcalabash1-mathml-to-svg", "1.2.0")
    ueberjars("com.xmlcalabash", "xmlcalabash1-xslthl", "1.2.0")
    ueberjars("org.xmlresolver", "xmlresolver", xmlresolver_version)
    ueberjars("org.xmlresolver:xmlresolver:${xmlresolver_version}:data@jar")
}

tasks {
    val copyJarsForUeberJars = task("copyJarsForUeberJars", Copy::class) {
        val ueberBaseFiles = configurations.get("ueberjars").resolvedConfiguration.files
        println("ueberBaseFiles: " + ueberBaseFiles)
        from(ueberBaseFiles) {
            // $1$3: primary as support for mvn classifiers
            rename("([a-zA-Z_]+)-([\\d\\.]+(.*)).jar", "$1$3.jar")
        }
        into("./lib/tmp")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE;
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

    val unzipJnr = task("unzipJnr", Copy::class) {
        from(zipTree(file("lib/tmp/jnr-enxio.jar"))) {
        }
        from(zipTree(file("lib/tmp/jnr-unixsocket.jar"))) {
        }
        into("./lib/tmp/jnrchannels")
        dependsOn(copyJarsForUeberJars)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE;
    }

    val rezipStrippedJnr = task("rezipStrippedJnr", Jar::class) {
        baseName = "jnrchannels"
        from(files("./lib/tmp/jnrchannels")) {
        }
        dependsOn(unzipJnr)
    }

    val unzipCalabashExt = task("unzipCalabashExt", Copy::class) {
        from(zipTree(file("lib/tmp/xmlcalabash1-mathml-to-svg.jar"))) {
        }
        from(zipTree(file("lib/tmp/xmlcalabash1-xslthl.jar"))) {
        }
        into("./lib/tmp/xmlcalabash-extensions")
        dependsOn(copyJarsForUeberJars)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE;
    }

    val rezipStrippedCalabashExt = task("rezipStrippedCalabashExt", Jar::class) {
        baseName = "xmlcalabash-extensions"
        from(files("./lib/tmp/xmlcalabash-extensions")) {
            exclude("com/xmlcalabash/extensions/*.class")
        }
        dependsOn(unzipCalabashExt)
    }

    val unzipXmlresolver = task("unzipXmlresolver", Copy::class) {
        from(zipTree(file("lib/tmp/xmlresolver-data.jar"))) {
        }
        from(zipTree(file("lib/tmp/xmlresolver.jar"))) {
        }
        into("./lib/tmp/xmlresolver")
        dependsOn(copyJarsForUeberJars)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE;
    }

    val rezipStrippedXmlresolver = task("rezipStrippedXmlresolver", Jar::class) {
        baseName = "xmlresolver"
        from(files("./lib/tmp/xmlresolver")) {
            // exclude("com/xmlcalabash/extensions/*.class")
        }
        dependsOn(unzipXmlresolver)
    }

    val jar by register("jar1", Jar::class) {
        archiveName = "foo.jar"
        into("META-INF") {
            from("bar")
        }
    }

}

artifacts {
    val xerces = tasks.named("rezipStrippedXerces")
    val jnrchannels = tasks.named("rezipStrippedJnr")
    val calabashExt = tasks.named("rezipStrippedCalabashExt")
    val xmlresolver = tasks.named("rezipStrippedXmlresolver")

    add("archives", xerces)
    add("archives", jnrchannels)
    add("archives", calabashExt)
    add("archives", xmlresolver)

    add("default", xerces)
    add("default", jnrchannels)
    add("default", calabashExt)
    add("default", xmlresolver)

    add("xerces", xerces)
    add("jnrchannels", jnrchannels)
    add("calabashExt", calabashExt)
    add("xmlresolver", xmlresolver)
}

tasks.named("build") {
    dependsOn("rezipStrippedXerces", "rezipStrippedJnr", "rezipStrippedCalabashExt", "rezipStrippedXmlresolver")
}

defaultTasks("build")

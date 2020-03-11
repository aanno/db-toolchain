val xercesVersion = "2.12.0"

val ueberjars = configurations.create("ueberjars")
val xerces = configurations.create("xerces")
val jnrchannels = configurations.create("jnrchannels")

plugins {
    `java`
}

repositories {
    mavenLocal()
    mavenCentral()

    // Use jcenter for resolving your dependenes.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    ueberjars("com.github.jnr", "jnr-enxio", "0.24")
    ueberjars("com.github.jnr", "jnr-unixsocket", "0.26")
    ueberjars("xerces", "xercesImpl", xercesVersion)
}

tasks {
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

    val unzipJnr = task("unzipJnr", Copy::class) {
        from(zipTree(file("lib/tmp/jnr-enxio.jar"))) {
        }
        from(zipTree(file("lib/tmp/jnr-unixsocket.jar"))) {
        }
        into("./lib/tmp/jnrchannels")
        dependsOn(copyJarsForUeberJars)
    }

    val rezipStrippedJnr = task("rezipStrippedJnr", Jar::class) {
        baseName = "jnrchannels"
        from(files("./lib/tmp/jnrchannels")) {
        }
        dependsOn(unzipJnr)
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

    add("archives", xerces)
    add("archives", jnrchannels)

    add("default", xerces)
    add("default", jnrchannels)

    add("xerces", xerces)
    add("jnrchannels", jnrchannels)
}

tasks.named("build") {
    dependsOn("rezipStrippedXerces", "rezipStrippedJnr")
}

defaultTasks("build")

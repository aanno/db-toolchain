val xercesVersion = "2.11.0"
val ueberjars = configurations.create("ueberjars")
val xerces = configurations.create("xerces")

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
    ueberjars("com.github.jnr", "jnr-enxio", "0.1.9")
    ueberjars("com.github.jnr", "jnr-unixsocket", "0.21")
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

    val jar by register("jar1", Jar::class) {
        archiveName = "foo.jar"
        into("META-INF") {
            from("bar")
        }
    }

}

artifacts {
    add("archives", tasks.named("rezipStrippedXerces"))
    add("default", tasks.named("rezipStrippedXerces"))
    add("xerces", tasks.named("rezipStrippedXerces"))
}

tasks.named("build") {
    dependsOn("rezipStrippedXerces")
}

defaultTasks("build")

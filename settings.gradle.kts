rootProject.name = "db-toolchain"

include(":splitjars")
/*
I would like to include asciidoctorj as composite project, however this fails with:
Could not resolve all dependencies for configuration ':compileClasspath'.
> Could not resolve org.asciidoctor:asciidoctorj:1.6.0-SNAPSHOT.
Required by:
project :
> Module version 'org.asciidoctor:asciidoctorj:1.6.0-SNAPSHOT' is not unique in composite:
can be provided by [project :asciidoctorj, project :asciidoctorj:asciidoctorj].
 */
includeBuild("submodules/asciidoctorj")
// includeBuild("submodules/asciidoctorj/asciidoctorj-api")
// includeBuild("submodules/asciidoctorj/asciidoctorj-core")

includeBuild("submodules/xslt20-stylesheets")
includeBuild("submodules/xslt20-resources")
includeBuild("submodules/asciidoctor-fopub")

// if jing-trang is a composite, merged jar resolution now works (tp)
// https://docs.gradle.org/current/userguide/composite_builds.html
includeBuild("submodules/jing-trang")
/*
includeBuild("submodules/jing-trang") {
    dependencySubstitution {
        substitute(module("com.thaiopensource:jingtrang")).with(project(":"))
    }
}
 */

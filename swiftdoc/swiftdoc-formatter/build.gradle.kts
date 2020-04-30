plugins {
    id("de.undercouch.download")
}

apply(from = "${rootProject.rootDir}/gradle/groups/formatters.gradle")
apply(from = "${rootProject.rootDir}/gradle/actions/shadow.gradle")

dependencies {
    "compile"(project(":common:common-formatter"))
    "compile"(project(":swiftdoc:swiftdoc-models"))
}

val updateSourceKitten by tasks.registering() {
    doLast {
        val version = project.property("sourceKittenVersion")
        val tarFile = file("${project.buildDir}/sourcekitten/sourcekitten-${version}.mojave.bottle.tar.gz")
        val tarFileUnpacked = file("${project.buildDir}/sourcekitten/unpacked")
        val unpackedBinary = file("${project.buildDir}/sourcekitten/unpacked/sourcekitten/${version}/bin/sourcekitten")
        val destinationBinaryFolder = file("${project.projectDir}/src/main/resources/bin")

        download.configure(
            delegateClosureOf<de.undercouch.gradle.tasks.download.DownloadAction> {
                src("https://homebrew.bintray.com/bottles/sourcekitten-${version}.mojave.bottle.tar.gz")
                dest(tarFile)
                overwrite(true)
            }
        )

        copy {
            from(tarTree(resources.gzip(tarFile)))
            into(tarFileUnpacked)
        }

        copy {
            from(unpackedBinary)
            into(destinationBinaryFolder)
            fileMode = 755
        }
    }
}

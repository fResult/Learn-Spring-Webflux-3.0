rootProject.name = "kotlin"

includeBuild("03-bootstrap")
includeBuild("04-input-and-output")
includeBuild("05-reactor")
includeBuild("06-data-access")
includeBuild("07-http")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}

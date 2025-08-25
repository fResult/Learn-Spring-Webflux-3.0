rootProject.name = "03-bootstrap"

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

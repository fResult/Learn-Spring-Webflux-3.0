rootProject.name = "kotlin-06-data-access"

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

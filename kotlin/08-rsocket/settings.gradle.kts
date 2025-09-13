rootProject.name = "08-rsocket"

include("common")
include("raw-rsocket")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

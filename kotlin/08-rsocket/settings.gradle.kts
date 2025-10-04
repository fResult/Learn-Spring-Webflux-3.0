rootProject.name = "08-rsocket"

include("common")
include("raw-rsocket")
include("spring-rsocket")
include("security")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

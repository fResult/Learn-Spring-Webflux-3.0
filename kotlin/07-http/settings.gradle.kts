rootProject.name = "07-http"

include("servlets")
include("webflux")
include("webclient")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

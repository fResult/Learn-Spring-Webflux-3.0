rootProject.name = "09-service-orchestration"

include("eureka-service")
include("profile-service")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

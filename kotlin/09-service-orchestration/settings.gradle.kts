rootProject.name = "09-service-orchestration"

include("eureka-service")
include("customer-service")
include("profile-service")
include("order-service")
include("slow-service")
include("error-service")
include("client")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

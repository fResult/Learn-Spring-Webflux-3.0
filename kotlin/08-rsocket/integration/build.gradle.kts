dependencies {
  implementation(project(":common"))
  implementation("org.springframework.boot:spring-boot-starter-integration")
  implementation("org.springframework.integration:spring-integration-rsocket")

  testImplementation("org.springframework.integration:spring-integration-test")
}

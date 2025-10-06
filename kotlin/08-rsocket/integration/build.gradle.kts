import org.springframework.boot.gradle.tasks.run.BootRun

dependencies {
  implementation(project(":common"))
  implementation("org.springframework.boot:spring-boot-starter-integration")
  implementation("org.springframework.integration:spring-integration-rsocket")
  implementation("org.springframework.integration:spring-integration-file")

  testImplementation("org.springframework.integration:spring-integration-test")
}

springBoot {
  mainClass.set("com.fResult.rsocket.service.IntegrationApplicationKt")
}

tasks.register<BootRun>("bootServiceRun") {
  group = "application"
  description = "Run the application with the 'service' Spring profile"
  mainClass.set("com.fResult.rsocket.service.IntegrationApplicationKt")
  classpath = sourceSets["main"].runtimeClasspath
  systemProperty("spring.profiles.active", "service")
}

tasks.register<BootRun>("bootIntegrationRun") {
  group = "application"
  description = "Run the application for the integration package"
  mainClass.set("com.fResult.rsocket.integration.IntegrationApplicationKt")
  classpath = sourceSets["main"].runtimeClasspath
}

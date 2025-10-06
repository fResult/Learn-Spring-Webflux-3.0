import org.springframework.boot.gradle.tasks.run.BootRun

dependencies {
  implementation(project(":common"))
  implementation(libs.spring.boot.starter.security)
  implementation(libs.spring.security.messaging)
  implementation(libs.spring.security.rsocket)

  testImplementation(libs.spring.security.test)
}

springBoot {
  mainClass.set("com.fResult.security.rsocket.SecurityApplicationKt")
}

tasks.register<BootRun>("bootServiceRun") {
  group = "application"
  description = "Run the application with the 'service' Spring profile"
  mainClass.set("com.fResult.rsocket.service.SecurityApplicationKt")
  classpath = sourceSets["main"].runtimeClasspath
  systemProperty("spring.profiles.active", "service")
}

tasks.register<BootRun>("bootClientRun") {
  group = "application"
  description = "Run the application for the client side"
  mainClass.set("com.fResult.rsocket.client.SecurityApplicationKt")
  classpath = sourceSets["main"].runtimeClasspath
}

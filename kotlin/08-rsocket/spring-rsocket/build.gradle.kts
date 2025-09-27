import org.springframework.boot.gradle.tasks.run.BootRun

dependencies {
  implementation(project(":common"))

  // MacOS ARM (Apple Silicon) DNS resolver
  runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.2.6.Final")
}

springBoot {
  mainClass = "com.fResult.rsocket.requestResponse.service.RequestResponseApplicationKt"
}

/* ================================ *
 * ======= Request Response ======= *
 * ================================ */
tasks.register<BootRun>("bootRequestResponseService") {
  group = "application"
  description = "Run the RSocket RequestResponse Server"
  mainClass = "com.fResult.rsocket.requestResponse.service.RequestResponseApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootRequestResponseClient") {
  group = "application"
  description = "Run the RSocket RequestResponse Client"
  mainClass = "com.fResult.rsocket.requestResponse.client.RequestResponseApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

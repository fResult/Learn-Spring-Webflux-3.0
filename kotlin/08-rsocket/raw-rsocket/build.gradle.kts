import org.springframework.boot.gradle.tasks.run.BootRun

dependencies {
  implementation(project(":common"))
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

/* ================================ *
 * ======== Fire And Forget ======= *
 * ================================ */
tasks.register<BootRun>("bootFireAndForgetService") {
  group = "application"
  description = "Run the RSocket FireAndForget Server"
  mainClass = "com.fResult.rsocket.fireAndForget.service.FireAndForgetApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootFireAndForgetClient") {
  group = "application"
  description = "Run the RSocket FireAndForget Client"
  mainClass = "com.fResult.rsocket.fireAndForget.client.FireAndForgetApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

/* ================================ *
 * =========== Channel ============ *
 * ================================ */
tasks.register<BootRun>("bootChannelService") {
  group = "application"
  description = "Run the RSocket Channel Server"
  mainClass = "com.fResult.rsocket.channel.service.ChannelApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootChannelClient") {
  group = "application"
  description = "Run the RSocket Channel Client"
  mainClass = "com.fResult.rsocket.channel.client.ChannelApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

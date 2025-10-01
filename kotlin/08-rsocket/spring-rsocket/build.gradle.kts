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

/* ================================ *
 * ======= Channel (Stream) ======= *
 * ================================ */
tasks.register<BootRun>("bootChannelService") {
  group = "application"
  description = "Run the RSocket Channel (Stream) Server"
  mainClass = "com.fResult.rsocket.channel.service.ChannelApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootChannelClient") {
  group = "application"
  description = "Run the RSocket Channel (Stream) Client"
  mainClass = "com.fResult.rsocket.channel.client.ChannelApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

/* =============================== *
 * ======= Fire and Forget ======= *
 * =============================== */
tasks.register<BootRun>("bootFireAndForgetService") {
  group = "application"
  description = "Run the RSocket Fire and Forget Server"
  mainClass = "com.fResult.rsocket.fireAndForget.service.FireAndForgetApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootFireAndForgetClient") {
  group = "application"
  description = "Run the RSocket Fire and Forget Client"
  mainClass = "com.fResult.rsocket.fireAndForget.client.FireAndForgetApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

/* =============================== *
 * ======== Bidirectional ======== *
 * =============================== */
tasks.register<BootRun>("bootBidirectionalService") {
  group = "application"
  description = "Run the RSocket Bidirectional Server"
  mainClass = "com.fResult.rsocket.bidirectional.service.BidirectionalApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootBidirectionalClient") {
  group = "application"
  description = "Run the RSocket Bidirectional Client"
  mainClass = "com.fResult.rsocket.bidirectional.client.BidirectionalApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

/* =============================== *
 * ====== Setup Connections ====== *
 * =============================== */
tasks.register<BootRun>("bootSetupConnectionsService") {
  group = "application"
  description = "Run the RSocket Setup Server"
  mainClass = "com.fResult.rsocket.setup.service.SetupApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootSetupConnectionsClient") {
  group = "application"
  description = "Run the RSocket Setup Client"
  mainClass = "com.fResult.rsocket.setup.client.SetupApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

/* =============================== *
 * ====== Routing Endpoints ====== *
 * =============================== */
tasks.register<BootRun>("bootRoutingService") {
  group = "application"
  description = "Run the RSocket Routing Server"
  mainClass = "com.fResult.rsocket.routing.service.RoutingApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootRoutingClient") {
  group = "application"
  description = "Run the RSocket Routing Client"
  mainClass = "com.fResult.rsocket.routing.client.RoutingApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

/* =============================== *
 * ======== Data Encodings ======= *
 * =============================== */
tasks.register<BootRun>("bootEncodingService") {
  group = "application"
  description = "Run the RSocket Encoding Server"
  mainClass = "com.fResult.rsocket.encoding.service.EncodingApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootEncodingClient") {
  group = "application"
  description = "Run the RSocket Encoding Client"
  mainClass = "com.fResult.rsocket.encoding.client.EncodingApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

/* =============================== *
 * ======== Metadata Push ======== *
 * =============================== */
tasks.register<BootRun>("bootMetadataService") {
  group = "application"
  description = "Run the RSocket Metadata Push Server"
  mainClass = "com.fResult.rsocket.metadata.service.MetadataApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootMetadataClient") {
  group = "application"
  description = "Run the RSocket Metadata Push Client"
  mainClass = "com.fResult.rsocket.metadata.client.MetadataApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

/* =============================== *
 * ======== Error Handling ======= *
 * =============================== */
tasks.register<BootRun>("bootErrorHandlingService") {
  group = "application"
  description = "Run the RSocket Error Handling Server"
  mainClass = "com.fResult.rsocket.errorHandling.service.ErrorHandlingApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootErrorHandlingClient") {
  group = "application"
  description = "Run the RSocket Error Handling Client"
  mainClass = "com.fResult.rsocket.errorHandling.client.ErrorHandlingApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

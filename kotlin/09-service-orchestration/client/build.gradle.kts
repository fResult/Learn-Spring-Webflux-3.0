import org.springframework.boot.gradle.tasks.run.BootRun

val resilience4jVersion  = "2.3.0"

dependencies {
  implementation(enforcedPlatform(libs.spring.cloud.dependencies.bom))
  implementation(libs.spring.cloud.starter.netflix.eureka.client)
  implementation(libs.spring.cloud.starter.loadbalancer)

  implementation(libs.spring.boot.starter.data.redis.reactive)
  implementation(libs.spring.boot.starter.security)
  implementation(libs.spring.cloud.starter.gateway.server.webflux)
  implementation(libs.resilience4j.ratelimiter)
  implementation(libs.resilience4j.circuitbreaker)
  implementation(libs.resilience4j.retry)
  implementation(libs.resilience4j.bulkhead)
  implementation(libs.resilience4j.reactor)
}

springBoot {
  mainClass.set("com.fResult.orchestration.BasicApplicationKt")
}

/* ============================== *
 * ======= Scatter Gather ======= *
 * ============================== */
tasks.register<BootRun>("bootScatterGatherClient") {
  group = "application"
  description = "Run the Scatter Gather Client"
  mainClass.set("com.fResult.orchestration.scatterGather.ScatterGatherApplicationKt")
  classpath = sourceSets["main"].runtimeClasspath
}

/* ============================== *
 * ======= Reactor Basic  ======= *
 * ============================== */
tasks.register<BootRun>("bootReactorBasicClient") {
  group = "application"
  description = "Run the Reactor Basic Client"
  mainClass.set("com.fResult.orchestration.reactor.BasicApplicationKt")
  classpath = sourceSets["main"].runtimeClasspath
}

/* ============================= *
 * ======= Resilience4j  ======= *
 * ============================= */
tasks.register<BootRun>("bootResilience4jClient") {
  group = "application"
  description = "Run the Resilience4j Client"
  mainClass.set("com.fResult.orchestration.resilience4j.ResilientClientApplicationKt")
  classpath = sourceSets["main"].runtimeClasspath
}

/* ============================= *
 * ========== Hedging ========== *
 * ============================= */
tasks.register<BootRun>("bootHedgingClient") {
  group = "application"
  description = "Run the Hedging Client"
  mainClass.set("com.fResult.orchestration.hedging.HedgingApplicationKt")
  classpath = sourceSets["main"].runtimeClasspath
}

/* ============================= *
 * ========= API Gateway ======= *
 * ============================= */
tasks.register<BootRun>("bootApiGatewayClient") {
  group = "application"
  description = "Run the API Gateway"
  mainClass.set("com.fResult.orchestration.gateway.ApiGatewayApplicationKt")
  classpath = sourceSets["main"].runtimeClasspath
}

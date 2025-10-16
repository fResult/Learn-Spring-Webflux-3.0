val resilience4jVersion  = "2.3.0"

dependencies {
  implementation(enforcedPlatform(libs.spring.cloud.dependencies.bom))
  implementation(libs.spring.cloud.starter.netflix.eureka.client)
  implementation(libs.spring.cloud.starter.loadbalancer)

  implementation("io.github.resilience4j:resilience4j-ratelimiter:$resilience4jVersion")
  implementation("io.github.resilience4j:resilience4j-circuitbreaker:$resilience4jVersion")
  implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")
  implementation("io.github.resilience4j:resilience4j-bulkhead:$resilience4jVersion")
  implementation("io.github.resilience4j:resilience4j-reactor:$resilience4jVersion")
}

springBoot {
  mainClass.set("com.fResult.orchestration.BasicApplicationKt")
}

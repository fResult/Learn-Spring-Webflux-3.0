dependencies {
  implementation(enforcedPlatform(libs.spring.cloud.dependencies.bom))
  implementation(libs.spring.cloud.starter.netflix.eureka.client)
  implementation(libs.spring.cloud.starter.loadbalancer)
}

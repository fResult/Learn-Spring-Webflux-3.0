dependencies {
  implementation(project(":common"))
  implementation(libs.spring.boot.starter.security)
  implementation(libs.spring.security.messaging)
  implementation(libs.spring.security.rsocket)

  testImplementation(libs.spring.security.test)
}

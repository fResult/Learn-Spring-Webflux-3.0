import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
  kotlin("jvm")
  kotlin("plugin.spring")
  id("org.springframework.boot")
  id("io.spring.dependency-management")
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation(libs.spring.boot.starter.security)
  implementation(libs.spring.boot.starter.webflux)
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.register<BootRun>("bootService") {
  group = "application"
  description = "Run the HttpServiceApplication"
  mainClass = "com.fresult.service.HttpServiceApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<BootRun>("bootClient") {
  group = "application"
  description = "Run the HttpClientApplication"
  mainClass = "com.fresult.client.HttpClientApplicationKt"
  classpath = sourceSets["main"].runtimeClasspath
}

plugins {
  kotlin("jvm") version "2.2.0"
  kotlin("plugin.spring") version "2.2.0"
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

group = "com.fResult"
version = "0.0.1"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

val log4jVersion = "2.24.3"

dependencies {
  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.starter.webflux)
  implementation(libs.spring.boot.starter.data.r2dbc)
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.apache.logging.log4j:log4j-bom:$log4jVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

  developmentOnly(libs.spring.boot.devtools)

  runtimeOnly("org.postgresql:postgresql")
  runtimeOnly("org.postgresql:r2dbc-postgresql")

  testImplementation(libs.spring.boot.starter.test)
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  testImplementation("org.testcontainers:junit-jupiter")
  testImplementation("org.testcontainers:postgresql")
  testImplementation("org.testcontainers:r2dbc")

  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

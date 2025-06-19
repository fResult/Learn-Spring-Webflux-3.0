plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
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
val blockHoundVersion = "1.0.13.RELEASE"

dependencies {
  implementation(libs.spring.boot.starter.webflux)
  developmentOnly(libs.spring.boot.devtools)
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.apache.logging.log4j:log4j-bom:$log4jVersion")
  testImplementation(libs.spring.boot.starter.test)
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("io.projectreactor.tools:blockhound:$blockHoundVersion")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.withType<Test>().all {
  useJUnitPlatform()
  if (JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_13)) {
    jvmArgs = listOf("-XX:+AllowRedefinitionToAddDeleteMethods")
  }
}

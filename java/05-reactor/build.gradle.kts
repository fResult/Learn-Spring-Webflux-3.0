plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

group = "com.fResult"
version = "0.0.1"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(24)
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
  compileOnly("org.projectlombok:lombok")
  implementation("org.apache.logging.log4j:log4j-bom:$log4jVersion")
  developmentOnly(libs.spring.boot.devtools)
  annotationProcessor("org.projectlombok:lombok")
  testImplementation(libs.spring.boot.starter.test)
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("io.projectreactor.tools:blockhound:$blockHoundVersion")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
  useJUnitPlatform()
}

plugins {
  java
  id("org.springframework.boot") version libs.versions.springboot.get()
  id("io.spring.dependency-management") version libs.versions.springDependencyManagement.get()
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

dependencies {
  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.starter.jdbc)
  implementation(libs.spring.boot.starter.web)
  compileOnly("org.projectlombok:lombok")
  developmentOnly(libs.spring.boot.devtools)
  runtimeOnly("com.h2database:h2")
  annotationProcessor("org.projectlombok:lombok")
  testImplementation(libs.spring.boot.starter.test)
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

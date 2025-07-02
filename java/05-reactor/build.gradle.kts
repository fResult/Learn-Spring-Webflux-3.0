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

val blockHoundVersion = "1.0.13.RELEASE"

dependencies {
  implementation(libs.spring.boot.starter.webflux)
  developmentOnly(libs.spring.boot.devtools)
  testImplementation(libs.spring.boot.starter.test)
  testImplementation("io.projectreactor:reactor-test")
  testCompileOnly("org.projectlombok:lombok")
  testAnnotationProcessor("org.projectlombok:lombok")
  testImplementation("io.projectreactor.tools:blockhound:$blockHoundVersion")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().all {
  useJUnitPlatform()
  if (JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_13)) {
    jvmArgs = listOf("-XX:+AllowRedefinitionToAddDeleteMethods")
  }
}

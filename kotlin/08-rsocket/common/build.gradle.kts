import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  kotlin("jvm")
  kotlin("plugin.spring")
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  annotationProcessor(libs.spring.boot.configuration.processor)
}

tasks.withType<Jar> {
  enabled = true
}

tasks.withType<BootJar> {
  enabled = false
}

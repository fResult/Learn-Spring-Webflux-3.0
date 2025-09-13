import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.plugin.spring)
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

import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  kotlin("jvm")
  kotlin("plugin.spring")
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
}

tasks.withType<Jar> {
  enabled = true
}

tasks.withType<BootJar> {
  enabled = false
}

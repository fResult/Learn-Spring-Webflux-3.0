import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  kotlin("jvm")
  kotlin("plugin.spring")
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
}

tasks.getByName<Jar>(JavaPlugin.JAR_TASK_NAME) {
  enabled = true
}

tasks.getByName<BootJar>(SpringBootPlugin.BOOT_JAR_TASK_NAME) {
  enabled = false
}

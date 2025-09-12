plugins {
  kotlin("jvm") version "2.2.0" apply false
  kotlin("plugin.spring") version "2.2.0" apply false
  alias(libs.plugins.spring.boot) apply false
  alias(libs.plugins.spring.dependency.management) apply false
}

allprojects {
  group = "com.fResult"
  version = "0.0.1"
  description = "07-http"

  repositories {
    mavenCentral()
  }

  plugins.withType<JavaPlugin> {
    extensions.configure<JavaPluginExtension> {
      toolchain {
        languageVersion = JavaLanguageVersion.of(21)
      }
    }
  }
}

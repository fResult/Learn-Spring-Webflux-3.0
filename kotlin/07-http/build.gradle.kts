plugins {
  kotlin("jvm") version "2.2.0" apply false
  kotlin("plugin.spring") version "2.2.0" apply false
  id("org.springframework.boot") version "3.5.5" apply false
  id("io.spring.dependency-management") version "1.1.7" apply false
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

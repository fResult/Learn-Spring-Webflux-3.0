plugins {
  kotlin("jvm") version "2.2.0"
  kotlin("plugin.spring") version "2.2.0"
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

allprojects {
  group = "com.fResult"
  version = "0.0.1"
}

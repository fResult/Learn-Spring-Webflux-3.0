import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
  kotlin("jvm") version "2.2.0" apply false
  kotlin("plugin.spring") version "2.2.0" apply false
  alias(libs.plugins.spring.boot) apply false
  alias(libs.plugins.spring.dependency.management) apply false
}

subprojects {
  // Type-safe apply
  apply<KotlinPluginWrapper>()        // kotlin("jvm")
  apply<SpringBootPlugin>()           // id("org.springframework.boot")
  apply<DependencyManagementPlugin>() // id("io.spring.dependency-management")

  // Untyped apply for kotlin("plugin.spring")
  apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  group = "com.fResult"
  version = "0.0.1"
  description = "09-service-orchestration"

  repositories {
    mavenCentral()
  }

  afterEvaluate {
    dependencies {
      "implementation"("org.springframework.boot:spring-boot-starter")
      "implementation"("org.jetbrains.kotlin:kotlin-reflect")
      "testImplementation"(libs.spring.boot.starter.test)
      "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit5")
      "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }
  }

  plugins.withType<JavaPlugin> {
    extensions.configure<JavaPluginExtension> {
      toolchain {
        languageVersion = JavaLanguageVersion.of(21)
      }
    }
  }

  extensions.configure<KotlinJvmProjectExtension> {
    compilerOptions {
      freeCompilerArgs.addAll(
        "-Xjsr305=strict",
        "-Xannotation-default-target=param-property",
      )
    }
  }


  tasks.withType<Test> {
    useJUnitPlatform()
  }
}
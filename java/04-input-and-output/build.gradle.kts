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

dependencies {
	implementation(libs.spring.boot.starter.webflux)
	compileOnly("org.projectlombok:lombok")
	developmentOnly(libs.spring.boot.devtools)
	annotationProcessor("org.projectlombok:lombok")
	testImplementation(libs.spring.boot.starter.test)
	testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.register<JavaExec>("runSync") {
	group = "application"
	description = "Run the Synchronous file I/O example"
	mainClass.set("com.fResult.io.files.Synchronous")
	classpath = sourceSets["main"].runtimeClasspath
}

tasks.withType<Test> {
	useJUnitPlatform()
}

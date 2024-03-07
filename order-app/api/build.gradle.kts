import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "1.9.21"
	kotlin("plugin.jpa") version "1.9.21"
}

tasks.getByName<BootJar>("bootJar") {
	enabled = false
}

tasks.getByName<Jar>("jar") {
	enabled = true
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

object Versions {
	val KOTEST = "4.6.0"
}

dependencies {
	implementation(project(":order-domain:rds"))
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.springframework.security:spring-security-core:5.5.0")
	implementation("com.google.code.gson:gson:2.8.8")
	testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.0")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:${Versions.KOTEST}")
	testImplementation("io.kotest:kotest-assertions-core:${Versions.KOTEST}")
	testImplementation("io.mockk:mockk:1.13.9")
	testImplementation("com.appmattus.fixture:fixture:1.2.0")
}



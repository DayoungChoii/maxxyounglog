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
	implementation(project(":order-domain:redis"))
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.google.code.gson:gson:2.8.8")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	//jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")


	runtimeOnly("com.h2database:h2")

	testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.0")
	testImplementation ("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:${Versions.KOTEST}")
	testImplementation("io.kotest:kotest-assertions-core:${Versions.KOTEST}")
	testImplementation("io.mockk:mockk:1.13.9")
	testImplementation("com.appmattus.fixture:fixture:1.2.0")
}



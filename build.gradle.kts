import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.1" apply false
	id("io.spring.dependency-management") version "1.1.4" apply false
	kotlin("jvm") version "1.9.21" apply false
	kotlin("plugin.spring") version "1.9.21" apply false
	kotlin("plugin.jpa") version "1.9.21" apply false
}

allprojects{
	group = "com"
	version = "0.0.1-SNAPSHOT"

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs += "-Xjsr305=strict"
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

subprojects {
	repositories {
		mavenCentral()
	}
}


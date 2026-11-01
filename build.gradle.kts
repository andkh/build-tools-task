import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  	id("org.springframework.boot") version "3.1.0"
  	id("io.spring.dependency-management") version "1.1.0"
  	kotlin("jvm") version "1.8.21"
  	kotlin("plugin.spring") version "1.8.21"
	kotlin("plugin.serialization") version "1.8.21"
}

group = "academy.softserve"
version = "0.0.1-SNAPSHOT"
java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}


// TODO: your code starts here

// Define a task named npmInstall of type Exec that will set the working directory to ui and run the command npm i.
tasks.register<Exec>("npmInstall") {
	workingDir(file("ui"))
	commandLine("npm", "install")
}

// Define a task named compileUi of type Exec, setting the working directory to ui, and execute npm run build. Ensure this task is dependent on the npmInstall task.
tasks.register<Exec>("compileUi") {
	dependsOn("npmInstall")
	workingDir(file("ui"))
	commandLine("npm", "run", "build")
}

// Create a task named copyUi of type Copy that copies ui/dist/index.html, ui/dist/app.js file and ui/dist/css folder into src/main/resources directory. This task should depend on the compileUi task
tasks.register<Copy>("copyUi") {
	dependsOn("compileUi")
	from("ui/dist") {
		include("index.html", "app.js", "css/**")
	}
	into("src/main/resources")
}

// From previous build we just want to makea install process clear for engine
tasks.named("processResources") {
	dependsOn("copyUi")
}



// Ensure that all tasks of type org.springframework.boot.gradle.tasks.run.BootRun depend on the copyUi task before executing.
tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
	dependsOn("copyUi")
}

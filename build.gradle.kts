import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val junitVersion = "5.4.0"
val ktorVersion = "1.1.2"

plugins {
    kotlin("jvm") version "1.3.21"
    application
}

group = "com.example.coroutines"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xuse-experimental=kotlin.Experimental")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

plugins {
    kotlin("jvm") version "2.1.20"
}

group = "com.warningimhack3r"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

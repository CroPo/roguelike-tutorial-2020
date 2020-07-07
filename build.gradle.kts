plugins {
    kotlin("jvm") version "1.3.72"
}

group = "com.cropo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.hexworks.zircon:zircon.core-jvm:2020.1.6-HOTFIX")
    implementation("org.hexworks.zircon:zircon.jvm.swing:2020.1.6-HOTFIX")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

tasks {
    test {
        useJUnitPlatform()
    }
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}
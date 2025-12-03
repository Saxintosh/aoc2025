plugins {
    kotlin("jvm") version "2.2.21"
	kotlin("plugin.allopen") version "2.2.21"
}

dependencies {
	implementation("io.ktor:ktor-client-core:3.3.3")
	implementation("io.ktor:ktor-client-cio:3.3.3")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}

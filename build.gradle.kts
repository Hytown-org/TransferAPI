
plugins {
    id("java")
    id("maven-publish")
}

group = "dev.hytalemodding"
version = "0.1.2"

var packageName = "transfer-api"
var fullPackageName = "$group.$packageName"

repositories {
    maven("https://maven.hytale.com/release") { name = "hytale-release" }
    mavenCentral()
}

dependencies {
    compileOnly("com.hypixel.hytale:Server:+")

    testCompileOnly("com.hypixel.hytale:Server:+") {
        exclude("*netty*")
    }
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.jar {
    from(project.projectDir) {
        include("LICENSE", "LICENSE-FABRIC", "NOTICE")
        into("META-INF/licensing/$fullPackageName")
    }
}

tasks.test {
    useJUnitPlatform()
}

// Use default javadoc doclint settings (strict) so we can surface missing tags and improve docs.

// Register common custom Javadoc tags so we can use tags such as @apiNote and @implSpec
tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addStringOption("tag", "apiNote:a:API Note:")
    (options as StandardJavadocDocletOptions).addStringOption("tag", "implNote:a:Implementation Note:")
    (options as StandardJavadocDocletOptions).addStringOption("tag", "implSpec:a:Implementation Specification:")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = packageName
        }
    }

    repositories {
        // Publish to local maven repository by default; users can add other repositories later.
        mavenLocal()
        maven {
            url = uri(property("artifactFeedUrl") as String)
            name = "potionlabs"
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}
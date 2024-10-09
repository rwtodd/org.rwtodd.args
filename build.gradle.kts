plugins {
    `java-library`
    `jvm-test-suite`

    // upload to Maven
    id("maven-publish")
    id("signing")
}

version = "2.0.1"
group = "org.rwtodd"

base {
  archivesName = "org.rwtodd.args"
}

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile>().configureEach {
   options.release = 21
}

tasks.withType<Test>().configureEach {
    testLogging {
         events("skipped", "failed")
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

java { 
  withJavadocJar()
  withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "org.rwtodd.args"
            from(components["java"])
            pom {
                name = "org.rwtodd.args"
                description = "A commandline-parsing library"
                url = "https://github.com/rwtodd/org.rwtodd.args"
                //properties = [ ]
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                developers {
                    developer {
                        id = "rwtodd"
                        name = "Richard Todd"
                        email = "rwtodd@users.noreply.github.com"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/rwtodd/org.rwtodd.args.git"
                    developerConnection = "scm:git:https://github.com/rwtodd/org.rwtodd.args.git"
                    url = "https://github.com/rwtodd/org.rwtodd.args"
                }
            }
        }
    }
    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = project.findProperty("ossrhUsername") as String? ?: ""
                password = project.findProperty("ossrhPassword") as String? ?: ""
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}


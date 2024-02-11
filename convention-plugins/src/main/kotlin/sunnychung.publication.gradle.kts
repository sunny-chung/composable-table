import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing
import java.util.*

plugins {
    `maven-publish`
    signing
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

val githubRepoName = ext["github-repo-name"]?.toString() ?: throw RuntimeException("Missing gradle property -- github-repo-name")
listOf("artifact-name", "artifact-description").forEach {
    ext[it] ?: throw RuntimeException("Missing property `$it`")
}
println("githubRepoName = $githubRepoName")

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

/**
 * Using this "common" javadocJar creates error.
 *
 * Ref:
 * - https://github.com/gradle/gradle/issues/26091
 * - https://youtrack.jetbrains.com/issue/KT-46466
 */
//val javadocJar by tasks.registering(Jar::class) {
//    archiveClassifier.set("javadoc")
//}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        val javadocJar = tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveBaseName.set("${archiveBaseName.get()}-$name")
        }
        artifact(javadocJar)

        // Provide artifacts information requited by Maven Central
        pom {
            name.set(getExtraString("artifact-name"))
            description.set(getExtraString("artifact-description"))
            url.set("https://github.com/sunny-chung/$githubRepoName")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("sunny-chung")
                    name.set("Sunny Chung")
                    email.set("sunnychung@live.hk")
                }
            }
            scm {
                url.set("https://github.com/sunny-chung/$githubRepoName")
            }
        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used
signing {
    sign(publishing.publications)
}

import java.nio.file.Paths

plugins {
	id("net.fabricmc.fabric-loom")
	`maven-publish`
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("rebinder") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.getByName("client"))
		}
	}
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
	
	implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	implementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")
	
}

tasks.processResources {
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	inputs.property("projectName", project.name)

	from("LICENSE") {
		rename { "${it}_${project.name}" }
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}

// === Local Deploy Task - Copy mod to your Minecraft mods folder ===
// special task to copy the built jar to a local prism launcher instance,
// but it's only copied there if it's not already in the folder, or if the copy there is older
// than the one that has been built here

val localMinecraftModsDir: File =
	Paths.get(System.getProperty("user.home"),
		"Library",
		"Application Support",
		"PrismLauncher",
		"instances",
		"26.1.1",
		"minecraft",
		"mods").toFile()

val deployToLocal by tasks.registering(Copy::class) {
	group = "fabric"
	description = "Copies the built mod jar to your local Minecraft mods folder (only if newer)"

	from(tasks.named("jar")) // Source = the final mod jar
	into(localMinecraftModsDir)

	// Only copy if the source is newer or target doesn't exist
	duplicatesStrategy = DuplicatesStrategy.INCLUDE

	// Custom filter to skip if target is newer
	eachFile {
		val targetFile = localMinecraftModsDir.resolve(this.name)
		if (targetFile.exists() && targetFile.lastModified() >= this.file.lastModified()) {
			this.exclude()   // Skip this file
			println("→ Skipping ${this.name} (local copy is up to date)")
		} else if (targetFile.exists()) {
			println("→ Updating ${this.name} in local mods folder")
		} else {
			println("→ Deploying ${this.name} to local mods folder")
		}
	}
}
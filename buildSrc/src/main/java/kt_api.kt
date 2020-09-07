import org.gradle.api.Project

fun Project.kt_api(
    packageName: String, // TODO: manifest placeholder for package
    dependencies: NamedDependency = emptyDependency(),
    buildConfiguration: BuildConfiguration = BuildConfiguration(),
    consumerMinificationFiles: Set<String> = emptySet(),
    manifestPlaceholders: Map<String, Any> = emptyMap()
) {
    api(
        dependencies,
        buildConfiguration,
        consumerMinificationFiles,
        manifestPlaceholders,
        Forma.configuration
    )
}
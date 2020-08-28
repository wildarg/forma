import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.*
import org.gradle.api.artifacts.Dependency as GradleDependency
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.artifacts.dsl.DependencyConstraintHandler
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo
import org.gradle.kotlin.dsl.accessors.runtime.addExternalModuleDependencyTo
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.funktionale.either.Either
import org.funktionale.option.Option

/**
 * Dependency wrapper
 * TODO: inline class
 * TODO: Annotation processor flag
 */
typealias DepSpec = Option<Either<List<String>, List<Project>>>

sealed class FormaDependency(val dependency: DepSpec)

object EmptyDependency : FormaDependency(Option.None)

data class NamedDependency(val names: List<String> = emptyList())
    : FormaDependency(Option.Some(Either.left(names)))

data class ProjectDependency(val projects: List<Project> = emptyList())
    : FormaDependency(Option.Some(Either.right(projects)))

inline fun <reified T : FormaDependency> emptyDependency(): T = when {
    T::class == FormaDependency::class -> EmptyDependency as T
    T::class == NamedDependency::class -> NamedDependency() as T
    T::class == ProjectDependency::class -> ProjectDependency() as T
    else -> throw IllegalArgumentException("Illegal Empty dependency, expected ${T::class.simpleName}")
}

internal fun FormaDependency.forEach(
    nameAction: (String) -> Unit,
    projectAction: (Project) -> Unit
) {
    dependency.forEach { dependency ->
        with(dependency) {
            left().forEach { it.forEach(nameAction) }
            right().forEach { it.forEach(projectAction) }
        }
    }
}

internal fun NamedDependency.forEach(action: (String) -> Unit) = forEach(action, {})
internal fun ProjectDependency.forEach(action: (Project) -> Unit) = forEach({}, action)

fun dependencies(vararg names: String): NamedDependency
        = NamedDependency(names.toList())

fun dependencies(vararg projects: Project): ProjectDependency
        = ProjectDependency(projects.toList())

fun dependencies(vararg dependencies: NamedDependency): NamedDependency
        = dependencies.flatMap { it.names }.let(::NamedDependency)

fun dependencies(vararg dependencies: ProjectDependency): ProjectDependency
        = dependencies.flatMap { it.projects }.let(::ProjectDependency)

fun Project.applyDependencies(
    dependencies: NamedDependency = emptyDependency(),
    projectDependencies: ProjectDependency = emptyDependency(),
    testDependencies: FormaDependency = emptyDependency(),
    androidTestDependencies: FormaDependency = emptyDependency(),
    transitive: Boolean = true
) {
    val configuration: (ExternalModuleDependency).() -> Unit = { isTransitive = transitive }
    dependencies {
        dependencies.forEach { implementation(it, configuration) }
        projectDependencies.forEach { implementation(it) }
        testDependencies.forEach(
            { testImplementation(it, configuration) },
            { testImplementation(it) }
        )
        androidTestDependencies.forEach(
            { testImplementation(it, configuration) },
            { testImplementation(it) }
        )
    }
}


/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.implementation(dependencyNotation: Any): GradleDependency? =
    add("implementation", dependencyNotation)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.implementation(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "implementation", dependencyNotation, dependencyConfiguration
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.implementation(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this,
    "implementation",
    group,
    name,
    version,
    configuration,
    classifier,
    ext,
    dependencyConfiguration
)

/**
 * Adds a dependency to the 'implementation' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun <T : ModuleDependency> DependencyHandler.implementation(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("implementation", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'implementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
fun DependencyConstraintHandler.implementation(constraintNotation: Any): DependencyConstraint? =
    add("implementation", constraintNotation)

/**
 * Adds a dependency constraint to the 'implementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
fun DependencyConstraintHandler.implementation(
    constraintNotation: Any,
    block: DependencyConstraint.() -> Unit
): DependencyConstraint? =
    add("implementation", constraintNotation, block)

/**
 * Adds an artifact to the 'implementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
fun ArtifactHandler.implementation(artifactNotation: Any): PublishArtifact =
    add("implementation", artifactNotation)

/**
 * Adds an artifact to the 'implementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
fun ArtifactHandler.implementation(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("implementation", artifactNotation, configureAction)

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.testImplementation(dependencyNotation: Any): org.gradle.api.artifacts.Dependency? =
    add("testImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.testImplementation(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "testImplementation", dependencyNotation, dependencyConfiguration
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.testImplementation(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this,
    "testImplementation",
    group,
    name,
    version,
    configuration,
    classifier,
    ext,
    dependencyConfiguration
)

/**
 * Adds a dependency to the 'testImplementation' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun <T : ModuleDependency> DependencyHandler.testImplementation(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("testImplementation", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'testImplementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
fun DependencyConstraintHandler.testImplementation(constraintNotation: Any): DependencyConstraint? =
    add("testImplementation", constraintNotation)

/**
 * Adds a dependency constraint to the 'testImplementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
fun DependencyConstraintHandler.testImplementation(
    constraintNotation: Any,
    block: DependencyConstraint.() -> Unit
): DependencyConstraint? =
    add("testImplementation", constraintNotation, block)

/**
 * Adds an artifact to the 'testImplementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
fun ArtifactHandler.testImplementation(artifactNotation: Any): PublishArtifact =
    add("testImplementation", artifactNotation)

/**
 * Adds an artifact to the 'testImplementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
fun ArtifactHandler.testImplementation(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("testImplementation", artifactNotation, configureAction)


/**
 * Adds a dependency to the 'androidTestImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): org.gradle.api.artifacts.Dependency? =
    add("androidTestImplementation", dependencyNotation)

/**
 * Adds a dependency to the 'androidTestImplementation' configuration.
 *
 * @param dependencyNotation notation for the dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.androidTestImplementation(
    dependencyNotation: String,
    dependencyConfiguration: Action<ExternalModuleDependency>
): ExternalModuleDependency = addDependencyTo(
    this, "androidTestImplementation", dependencyNotation, dependencyConfiguration
) as ExternalModuleDependency

/**
 * Adds a dependency to the 'androidTestImplementation' configuration.
 *
 * @param group the group of the module to be added as a dependency.
 * @param name the name of the module to be added as a dependency.
 * @param version the optional version of the module to be added as a dependency.
 * @param configuration the optional configuration of the module to be added as a dependency.
 * @param classifier the optional classifier of the module artifact to be added as a dependency.
 * @param ext the optional extension of the module artifact to be added as a dependency.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.create]
 * @see [DependencyHandler.add]
 */
internal fun DependencyHandler.androidTestImplementation(
    group: String,
    name: String,
    version: String? = null,
    configuration: String? = null,
    classifier: String? = null,
    ext: String? = null,
    dependencyConfiguration: Action<ExternalModuleDependency>? = null
): ExternalModuleDependency = addExternalModuleDependencyTo(
    this,
    "androidTestImplementation",
    group,
    name,
    version,
    configuration,
    classifier,
    ext,
    dependencyConfiguration
)

/**
 * Adds a dependency to the 'androidTestImplementation' configuration.
 *
 * @param dependency dependency to be added.
 * @param dependencyConfiguration expression to use to configure the dependency.
 * @return The dependency.
 *
 * @see [DependencyHandler.add]
 */
fun <T : ModuleDependency> DependencyHandler.androidTestImplementation(
    dependency: T,
    dependencyConfiguration: T.() -> Unit
): T = add("androidTestImplementation", dependency, dependencyConfiguration)

/**
 * Adds a dependency constraint to the 'androidTestImplementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
fun DependencyConstraintHandler.androidTestImplementation(constraintNotation: Any): DependencyConstraint? =
    add("androidTestImplementation", constraintNotation)

/**
 * Adds a dependency constraint to the 'androidTestImplementation' configuration.
 *
 * @param constraintNotation the dependency constraint notation
 * @param block the block to use to configure the dependency constraint
 *
 * @return the added dependency constraint
 *
 * @see [DependencyConstraintHandler.add]
 */
fun DependencyConstraintHandler.androidTestImplementation(
    constraintNotation: Any,
    block: DependencyConstraint.() -> Unit
): DependencyConstraint? =
    add("androidTestImplementation", constraintNotation, block)

/**
 * Adds an artifact to the 'androidTestImplementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
fun ArtifactHandler.androidTestImplementation(artifactNotation: Any): PublishArtifact =
    add("androidTestImplementation", artifactNotation)

/**
 * Adds an artifact to the 'androidTestImplementation' configuration.
 *
 * @param artifactNotation the group of the module to be added as a dependency.
 * @param configureAction The action to execute to configure the artifact.
 * @return The artifact.
 *
 * @see [ArtifactHandler.add]
 */
fun ArtifactHandler.androidTestImplementation(
    artifactNotation: Any,
    configureAction: ConfigurablePublishArtifact.() -> Unit
): PublishArtifact =
    add("androidTestImplementation", artifactNotation, configureAction)

import com.stepango.forma.FormaConfiguration
import com.stepango.forma.Library
import com.stepango.forma.Validator
import com.stepango.forma.throwProjectValidationError
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

fun Project.android_library(
    dependencies: NamedDependency = emptyDependency(),
    testDependencies: NamedDependency = emptyDependency(),
    buildConfiguration: BuildConfiguration = BuildConfiguration(),
    testInstrumentationRunner: String = androidJunitRunner,
    consumerMinificationFiles: Set<String> = emptySet(),
    androidTestDependencies: NamedDependency,
    manifestPlaceholders: Map<String, Any> = emptyMap()
) = android_library(
    dependencies,
    testDependencies,
    buildConfiguration,
    testInstrumentationRunner,
    consumerMinificationFiles,
    manifestPlaceholders,
    androidTestDependencies,
    Forma.configuration
)

@Suppress("UnstableApiUsage")
internal fun Project.android_library(
    dependencies: NamedDependency,
    testDependencies: FormaDependency,
    buildConfiguration: BuildConfiguration,
    testInstrumentationRunner: String,
    consumerMinificationFiles: Set<String>,
    manifestPlaceholders: Map<String, Any>,
    androidTestDependencies: NamedDependency,
    formaConfiguration: FormaConfiguration,
    validator: Validator = LibraryValidator
) {
    apply(plugin = "com.android.library")
    applyLibraryConfiguration(formaConfiguration, buildConfiguration, testInstrumentationRunner, consumerMinificationFiles, manifestPlaceholders)
    applyDependencies(
        formaConfiguration = formaConfiguration,
        dependencies = dependencies,
        testDependencies = testDependencies,
        androidTestDependencies = androidTestDependencies
    )
    validator.validate(this)
}

object LibraryValidator: Validator {
    override fun validate(project: Project) {
        if (!Library.validate(project)) {
            throwProjectValidationError(project, Library)
        }
    }
}
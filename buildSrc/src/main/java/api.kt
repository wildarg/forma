import com.stepango.forma.FormaConfiguration
import com.stepango.forma.Api
import com.stepango.forma.Validator
import com.stepango.forma.throwProjectValidationError
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

fun Project.api(
    dependencies: NamedDependency = emptyDependency(),
    buildConfiguration: BuildConfiguration = emptyDependency(),
    consumerMinificationFiles: Set<String> = emptySet(),
    manifestPlaceholders: Map<String, Any> = emptyMap()
) = api(
    dependencies,
    buildConfiguration,
    consumerMinificationFiles,
    manifestPlaceholders,
    Forma.configuration
)

@Suppress("UnstableApiUsage")
internal fun Project.api(
    dependencies: NamedDependency,
    buildConfiguration: BuildConfiguration,
    consumerMinificationFiles: Set<String>,
    manifestPlaceholders: Map<String, Any>,
    formaConfiguration: FormaConfiguration,
    validator: Validator = ApiValidator
) {
    apply(plugin = "com.android.library")
    apply(plugin = "kotlin-android")
    applyApiConfiguration(
        formaConfiguration,
        buildConfiguration,
        consumerMinificationFiles,
        manifestPlaceholders
    )
    applyDependencies(
        formaConfiguration = formaConfiguration,
        dependencies = dependencies
    )
    validator.validate(this)
}

object ApiValidator: Validator {
    override fun validate(project: Project) {
        if (!Api.validate(project)) {
            throwProjectValidationError(project, Api)
        }
    }
}
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.customImplementation(dependencies: List<Dependency>) {
    dependencies.forEach {
        configuration(
            when (it) {
                is Dependency.Kapt -> {
                    println("kapt: ${it.full()}")
                    "kapt"
                }
                is Dependency.AnnotationProcessor -> {
                    println("annotationProcessor: ${it.full()}")
                    "annotationProcessor"
                }
                else -> {
                    println("implementation: ${it.full()}")
                    "implementation"
                }
            },
            it
        )
    }
}

private fun DependencyHandler.configuration(name: String, dependency: Dependency) {
    add(name, dependency.full())
}
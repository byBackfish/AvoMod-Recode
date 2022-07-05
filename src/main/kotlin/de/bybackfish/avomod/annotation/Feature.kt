package de.bybackfish.avomod.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Feature(
    val name: String,
    val description: String,
    val default: Boolean = false,
    val hidden: Boolean = false,
    val displayName: String,
    val isDrawing: Boolean = false,
    val defaultX: Int = 100,
    val defaultY: Int = 100,
)


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OnEnable

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OnInit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OnDisable

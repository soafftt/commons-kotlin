package soft.r2dbc.core.enums

enum class R2dbcImplementation {
    SPRING_JPA,
    EXPOSED,
    ;

    companion object {
        const val USE_EXPOSED = "EXPOSED"
        const val USE_SPRING_JPA = "SPRING_JPA"
    }
}
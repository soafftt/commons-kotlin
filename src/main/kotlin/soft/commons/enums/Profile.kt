package soft.commons.enums

enum class Profile {
    UNKNOWN, LOCAL, TEST, PRE, LIVE;

    companion object {
        private val codeMap: Map<String, Profile> =
            values().associateBy { it.name }

        fun lookup(profile: String): Profile =
            codeMap.getOrDefault(profile, UNKNOWN)
    }
}
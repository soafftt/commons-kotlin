package soft.common.jwt.annotation

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ClaimsEncrypt(
    val isEncrypted: Boolean = false,
    val encryptKey: String = "",
    val decryptKey: String = "",
)
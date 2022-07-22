package soft.commons.jwt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kotlinx.serialization.SerialName

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
data class CipherPayload (
    @SerialName("name")
    var cipherText: String
)
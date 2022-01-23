package soft.commons.jwt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CipherPayload @JsonCreator constructor(
    @JsonProperty("name")
    var cipherText: String
)
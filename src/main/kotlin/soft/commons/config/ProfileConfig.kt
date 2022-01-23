package soft.commons.config

import soft.commons.enums.Profile
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class ProfileConfig {

    @Bean
    fun springActivateProfile(environment: Environment): Profile =
        Profile.lookup(environment.activeProfiles.getOrElse(0) { Profile.UNKNOWN.name })
}
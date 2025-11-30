package soft.valkey

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import soft.soft.valkey.config.GlideStandAloneRegister

@Configuration
@Import(GlideStandAloneRegister::class)
class ValKeyConfig
package soft.r2dbc.write

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import soft.r2dbc.core.enums.R2dbcImplementation

@DependsOn("writerConnectionFactoryConfig")
@Configuration
@ConditionalOnProperty(value = ["r2dbc.implementation"], havingValue = R2dbcImplementation.USE_SPRING_JPA)
@EnableR2dbcRepositories(
    basePackages = ["\${r2dbc.jpa-scan-packages.writer:}"],
    entityOperationsRef = "r2dbcEntityOperations"
)
@EnableR2dbcAuditing
@EnableTransactionManagement
class WriterSpringJpaConfig
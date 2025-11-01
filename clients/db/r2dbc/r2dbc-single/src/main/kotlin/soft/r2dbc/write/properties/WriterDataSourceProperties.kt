package soft.r2dbc.write.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import soft.r2dbc.core.properties.DataSourceProperties

@ConfigurationProperties(prefix = "r2dbc.datasource.writer")
class WriterDataSourceProperties : DataSourceProperties()
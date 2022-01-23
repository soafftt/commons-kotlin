package soft.commons.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

@Configuration
class ReactSchedulerConfig {
    companion object {
        private const val BOUNDED_ELASTIC_SCHEDULER_NAME = "securityCenter.boundedElasticScheduler"
        private const val LETTUCE_PUBLISH_SCHEDULER_NAME = "securityCenter.lettucePublishScheduler";
        private const val WEBCLIENT_PUBLISH_SCHEDULER_NAME = "securityCenter.webClientPublishScheduler";
    }

    @Bean
    fun boundedElasticScheduler(): Scheduler = Schedulers.newBoundedElastic(
        Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE,
        Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE,
        BOUNDED_ELASTIC_SCHEDULER_NAME
    );

    @Bean
    fun lettucePublishScheduler(): Scheduler = Schedulers.newBoundedElastic(
        Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE,
        Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE,
        LETTUCE_PUBLISH_SCHEDULER_NAME
    );

    @Bean
    fun webClientPublishScheduler(): Scheduler = Schedulers.newBoundedElastic(
        Schedulers.DEFAULT_BOUNDED_ELASTIC_SIZE,
        Schedulers.DEFAULT_BOUNDED_ELASTIC_QUEUESIZE,
        WEBCLIENT_PUBLISH_SCHEDULER_NAME
    );
}
package team.react.imagecontainer.global.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfiguration {
    @Bean("imageUploadTaskExecutor")
    fun imageExecutor() : Executor {
        val executor: ThreadPoolTaskExecutor = ThreadPoolTaskExecutor()

        executor.corePoolSize = 5
        executor.maxPoolSize = 10
        executor.queueCapacity = 100
        executor.initialize()

        return executor
    }
}
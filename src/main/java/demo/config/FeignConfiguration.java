package demo.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.api.RateClient;
import feign.Retryer;

@Configuration
@EnableFeignClients(basePackageClasses = RateClient.class)
public class FeignConfiguration {
    
    @Bean
    Retryer feignRetryer() {
        return new Retryer.Default();
    }
    
}

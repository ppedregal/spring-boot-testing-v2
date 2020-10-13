package demo;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import demo.api.RateClient;

@SpringBootApplication
@RestController
public class App {

    public static void main(String...args) {
        SpringApplication.run(App.class, args);
    }
    
    @Bean
    @Profile("!parallel")
    RateProvider sequentialRateProvider(final RateClient[] apis) {
        return new SequentialRateProvider(apis);
    }

    @Bean
    @Profile("parallel")
    RateProvider parallelRateProvider(final RateClient[] apis) {
        return new ParallelRateProvider(apis);
    }

    @Autowired
    RateProvider rates;

    @GetMapping("/convert/{source}/{target}/{amount}")
    public Number convert(@PathVariable("source") final String source,
            @PathVariable("target") final String target,
            @PathVariable("amount") final Long amount) {
        return rates.findRate(source, target).multiply(new BigDecimal(amount));
    }

}

package demo;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class App {

    public static void main(String...args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    RateApi api_frankfurter_app(){
        return new RateApi("https://api.frankfurter.app/latest");
    }

    @Bean
    RateApi api_ratesapi_io(){
        return new RateApi("https://api.ratesapi.io/api/latest");
    }

    @Bean
    RateApi api_exchangeratesapi_io(){
        return new RateApi("https://api.exchangeratesapi.io/latest");
    }

    // @Bean
    // @Profile("!parallel")
    RateProvider sequentialRateProvider(RateApi[] apis){
        return new SequentialRateProvider(apis);
    }

    @Bean
    // @Profile("parallel")
    RateProvider parallelRateProvider(RateApi[] apis){
        return new ParallelRateProvider(apis);
    }

    @Autowired
    RateProvider rates;

    @GetMapping("/convert/{source}/{target}/{amount}")
    public Mono<Number> convert(@PathVariable("source") String source, @PathVariable("target") String target, @PathVariable("amount") Long amount){
        return rates.findRate(source,target).map(rate->rate.multiply(BigDecimal.valueOf(amount)));
    }

}

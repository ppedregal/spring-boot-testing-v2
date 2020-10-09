package demo;

import java.math.BigDecimal;

import reactor.core.publisher.Mono;

public interface RateProvider {

	default Mono<BigDecimal> findRate(String source, String target) {
        return Mono.just(BigDecimal.ONE); 
    }
    
}

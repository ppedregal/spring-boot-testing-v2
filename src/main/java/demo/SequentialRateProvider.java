package demo;

import java.math.BigDecimal;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class SequentialRateProvider implements RateProvider {

    private RateApi[] apis;

    @Override
    public Mono<BigDecimal> findRate(String source, String target) {

        return Mono.just(Stream.of(apis)
            .map(api->api.findRate(source, target).block())
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO));
    }
    
}

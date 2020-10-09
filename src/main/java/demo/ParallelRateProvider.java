package demo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@AllArgsConstructor
public class ParallelRateProvider implements RateProvider {

    RateApi[] apis;
    
    @Override
    public Mono<BigDecimal> findRate(String source, String target) {
        List<Mono<BigDecimal>> monos = Stream.of(apis).map(api->api.findRate(source, target)).collect(Collectors.toList());
        return Flux.merge(monos)
            .parallel(apis.length)
            .runOn(Schedulers.elastic())
            .collectSortedList(Collections.reverseOrder(BigDecimal::compareTo),apis.length)
            .map(l->l.get(0));
    }

}

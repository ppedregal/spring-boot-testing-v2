package demo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.netflix.hystrix.HystrixCommand;

import demo.api.RateClient;
import demo.api.RateClient.RateResponse;
import lombok.AllArgsConstructor;
import rx.Observable;

@AllArgsConstructor
public class ParallelRateProvider implements RateProvider {

    private final RateClient[] apis;

    @Override
    public BigDecimal findRate(final String source, final String target) {
        
        final RateFinder finder = new RateFinder(source, target);
        List<Observable<RateResponse>> observables = Stream.of(apis)
            .map(RateClient::latest)
            .map(HystrixCommand::toObservable)
            .collect(Collectors.toList());

        Observable<BigDecimal> obs = Observable.zip(observables, arr -> {
            return Stream.of(arr)
                    .map(item -> (RateResponse) item)
                    .map(finder::findRate)
                    .filter(Objects::nonNull)
                    .max(BigDecimal::compareTo)
                    .orElse(null);
        });
        return obs.toBlocking().first();

    }

}

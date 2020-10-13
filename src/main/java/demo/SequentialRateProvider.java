package demo;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

import com.netflix.hystrix.HystrixCommand;

import demo.api.RateClient;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SequentialRateProvider implements RateProvider {

    private final RateClient[] apis;

    @Override
    public BigDecimal findRate(final String source, final String target) {
        final RateFinder finder = new RateFinder(source, target);
        return Stream.of(apis)
            .map(RateClient::latest)
            .map(HystrixCommand::execute)
            .map(finder::findRate)
            .filter(Objects::nonNull)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
    }

}

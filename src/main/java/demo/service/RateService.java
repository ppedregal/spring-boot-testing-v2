package demo.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RateService {

    private FindRateStrategy strategy;

    @HystrixCommand(fallbackMethod = "getLastRate")
    public BigDecimal findRate(final String source, final String target) {
        BigDecimal[] ratesFound = strategy.find(source, target);
        BigDecimal found = Stream.of(ratesFound)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(null);

        if (found != null) {
            setLastRate(source, target, found);
        } else {
            throw new RateNotFoundException(source, target);
        }
        return found;
    }
    
    public BigDecimal getLastRate(final String source, final String target) {
        return lastRate.get(rateKey(source, target));
    }
    
    public void setLastRate(final String source,final String target,BigDecimal value) {
        lastRate.put(rateKey(source,target),value);
    }
    
    private final Map<String, BigDecimal> lastRate = new HashMap<>();

    private String rateKey(final String source, final String target) {
        return source.toUpperCase() + "-" + target.toUpperCase();
    }

}

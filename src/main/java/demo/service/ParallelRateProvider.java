package demo.service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import demo.api.RateApi;
import demo.api.RateApi.RateResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Profile("parallel")
@Slf4j
public class ParallelRateProvider extends FindRateStrategy {

    final RateApi[] apis;

    @Override
    public BigDecimal[] find(String source, String target) {

        log.info("Ejecucion paralela de apis");

        CompletableFuture<RateResponse>[] completables = Stream.of(apis)
            .map(RateApi::latest)
            .toArray(CompletableFuture[]::new);

        try {
            CompletableFuture.allOf(completables).join();
        } catch (CompletionException e) {
            log.error("Error completando respuesta", e);
        }

        final RateFinder finder = new RateFinder(source, target);

        return Stream.of(completables)
            .map(t -> {
                try {
                    return t.get();
                } catch (Exception e) {
                    log.error("Error recuperando respuesta", e);
                    return null;
                }
            })
            .map(finder::findRate)
            .filter(Objects::nonNull)
            .toArray(BigDecimal[]::new);

    }

}

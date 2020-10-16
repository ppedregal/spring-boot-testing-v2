package demo.service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import demo.api.RateApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Profile("!parallel")
@Slf4j
public class SequentialRateProvider extends FindRateStrategy {

    final RateApi[] apis;

    @Override
    public BigDecimal[] find(final String source, final String target) {

        log.info("Ejecucion secuencial de apis");

        final RateFinder finder = new RateFinder(source, target);

        return Stream.of(apis)
            .map(RateApi::latest)
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

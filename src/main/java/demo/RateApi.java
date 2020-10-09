package demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class RateApi {

    private final String url;
    private final int numDecimals = 4;

    public Mono<BigDecimal> findRate(String source, String target) {
        Mono<BigDecimal> mono = WebClient.create()
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(ResponseDto.class)
            .map(dto->{
                BigDecimal amountDto = Optional.ofNullable(dto.amount).orElse(BigDecimal.ONE);
                BigDecimal sourceDto = dto.rates.getOrDefault(source.toUpperCase(), BigDecimal.ONE);
                BigDecimal targetDto = dto.rates.getOrDefault(target.toUpperCase(), BigDecimal.ONE);
                String baseDto = Optional.ofNullable(dto.base).orElse(source);
                if (source.equals(target)){
                    // 1
                    return BigDecimal.ONE;
                } else if (source.equalsIgnoreCase(baseDto)){
                    // (targetDto/amountDto)
                    return targetDto.divide(amountDto,numDecimals, RoundingMode.HALF_UP);
                } else if (target.equalsIgnoreCase(baseDto)) {
                     // (amountDto/sourceDto)
                    return amountDto.divide(sourceDto,numDecimals, RoundingMode.HALF_UP);
                } else {
                    // (amountDto/sourceDto)*(targetDto/amountDto) => (targetDto/sourceDto)
                    return targetDto.divide(sourceDto,numDecimals, RoundingMode.HALF_UP);
                }
            });
        return mono;
    }

    @Data
    static class ResponseDto {
        BigDecimal amount;
        String base;
        LocalDate date;
        Map<String,BigDecimal> rates;
    }

}

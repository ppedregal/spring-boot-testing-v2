package demo.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import lombok.Data;

public interface RateApi {

    CompletableFuture<RateResponse> latest();

    @Data
    class RateResponse {
        BigDecimal amount;
        String base;
        LocalDate date;
        Map<String,BigDecimal> rates;
    }

}

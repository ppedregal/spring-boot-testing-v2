package demo.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.cloud.openfeign.support.FallbackCommand;

import com.netflix.hystrix.HystrixCommand;

import lombok.Data;

public interface RateClient {

    HystrixCommand<RateResponse> latest();

    @Data
    class RateResponse {
        BigDecimal amount;
        String base;
        LocalDate date;
        Map<String,BigDecimal> rates;
    }

    class Fallback implements RateClient {

        @Override
        public HystrixCommand<RateResponse> latest() {
            return new FallbackCommand<RateResponse>(null);
        }

    }

}

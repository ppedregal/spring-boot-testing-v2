package demo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ConvertService {

    private final RateService rates;
    
    public BigDecimal convert(String source, String target, BigDecimal amount) {
        String sourceCurrency = source.toUpperCase();
        String targetCurrency = target.toUpperCase();
        BigDecimal rate = rates.findRate(sourceCurrency, targetCurrency);
        if (rate != null) {
            return rate.multiply(amount);
        }
        return null;
    }

}

package demo;

import java.math.BigDecimal;

public interface RateProvider {
    
    default BigDecimal findRate(String source,String target) {
        return BigDecimal.ZERO;
    }

}

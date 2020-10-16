package demo.service;

import java.math.BigDecimal;

public abstract class FindRateStrategy {

    abstract BigDecimal[] find(String source, String target);

}

package demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import demo.api.RateClient.RateResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RateFinder {

    /**
     * Numero de decimales a utilizar en los posibles redondeos.
     */
    private static final int NUM_DECIMALS = 4;

    /**
     * Codigo iso moneda origen.
     */
    private final String source;

    /**
     * Codigo iso moneda destino.
     */
    private final String target;

    /**
     * Constructor.
     * @param src moneda origen
     * @param tgt moneda destino
     */
    public RateFinder(final String src, final String tgt) {
        this.source = src.toUpperCase();
        this.target = tgt.toUpperCase();
    }

    /**
     * Obtiene ratio conversion entre source y target.
     * @param dto respuesta servicio rates
     * @return ratio obtenido del servicio
     */
    public final BigDecimal findRate(final RateResponse dto) {

        BigDecimal found = null;
        if (dto != null) {

            BigDecimal amountDto = Optional.ofNullable(dto.getAmount())
                    .orElse(BigDecimal.ONE);
            BigDecimal sourceDto = dto.getRates()
                    .getOrDefault(source, BigDecimal.ONE);
            BigDecimal targetDto = dto.getRates()
                    .getOrDefault(target, BigDecimal.ONE);
            String baseDto = Optional.ofNullable(dto.getBase())
                    .orElse(source);

            if (source.equals(target)) {
                // 1
                found = BigDecimal.ONE;
            } else if (source.equalsIgnoreCase(baseDto)) {
                // (targetDto/amountDto)
                found = targetDto
                        .divide(amountDto, NUM_DECIMALS, RoundingMode.HALF_UP);
            } else if (target.equalsIgnoreCase(baseDto)) {
                 // (amountDto/sourceDto)
                found = amountDto
                        .divide(sourceDto, NUM_DECIMALS, RoundingMode.HALF_UP);
            } else {
                // (amountDto/sourceDto) * (targetDto/amountDto)
                // (targetDto/sourceDto)
                found = targetDto
                        .divide(sourceDto, NUM_DECIMALS, RoundingMode.HALF_UP);
            }

            log.debug("{}-{}:{}", source, target, found);

        }
        return found;

    }

}

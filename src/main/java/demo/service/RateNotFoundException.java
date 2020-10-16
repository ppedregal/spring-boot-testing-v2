package demo.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RateNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String source;
    private final String target;

    public RateNotFoundException(final String source, final String target) {
        super("Rate not found: " + source + "-" + target);
        this.source=source;
        this.target=target;
    }

}

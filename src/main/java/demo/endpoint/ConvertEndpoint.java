package demo.endpoint;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import demo.service.ConvertService;

@RestController
public class ConvertEndpoint {

    @Autowired
    ConvertService conversions;

    @GetMapping("/convert/{source}/{target}/{amount}")
    public ResponseEntity<Number> convert(
            @PathVariable("source") final String source,
            @PathVariable("target") final String target,
            @PathVariable("amount") final Long amount) {
        Number value = conversions.convert(source, target, new BigDecimal(amount));
        return Optional.ofNullable(value).map(ResponseEntity::ok)
            .orElseGet(()->ResponseEntity.notFound().build());
    }

}

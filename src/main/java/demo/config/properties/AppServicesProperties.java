package demo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("app.services")
@Data
public class AppServicesProperties {

    String exchangeratesapi = "https://api.exchangeratesapi.io";
    String frankfurter = "https://api.frankfurter.app";
    String ratesapi = "https://api.ratesapi.io/api";

}

package demo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

@ConfigurationProperties
@Data
public class AppRetrofitProperties {
    
    long timeout = 5000;
    HttpLoggingInterceptor.Level logging = Level.BASIC; 

}

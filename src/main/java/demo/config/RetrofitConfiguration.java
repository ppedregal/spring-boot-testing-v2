package demo.config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import demo.api.retrofit.ExchangeratesapiRateClient;
import demo.api.retrofit.FrankfurterRateClient;
import demo.api.retrofit.RatesapiRateClient;
import demo.config.properties.AppRetrofitProperties;
import demo.config.properties.AppServicesProperties;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@ConditionalOnClass(value = Retrofit.class)
@EnableConfigurationProperties({
    AppServicesProperties.class,
    AppRetrofitProperties.class
})
public class RetrofitConfiguration {

    @Bean
    public Converter.Factory jacksonConverterFactory(
            final Jackson2ObjectMapperBuilder jackson) {
        return JacksonConverterFactory.create(jackson.build());
    }

    @Bean
    public HttpLoggingInterceptor httpLoggingInterceptor(
            final AppRetrofitProperties properties) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(properties.getLogging());
        return interceptor;
    }

    @Bean
    public OkHttpClient okHttpClient(
            final AppRetrofitProperties properties,
            final List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .callTimeout(properties.getTimeout(), TimeUnit.MILLISECONDS);
        interceptors.forEach(builder::addInterceptor);
        return builder.build();
    }

    @Bean
    public ExchangeratesapiRateClient retrofitExchangeratesapiRateClient(
            final AppServicesProperties properties,
            final List<Converter.Factory> converters,
            final OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(properties.getExchangeratesapi())
                .client(client);
        converters.forEach(builder::addConverterFactory);
        return builder.build().create(ExchangeratesapiRateClient.class);
    }

    @Bean
    public FrankfurterRateClient retrofitFrankfurterRateClient(
            final AppServicesProperties properties,
            final List<Converter.Factory> converters,
            final OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(properties.getFrankfurter())
                .client(client);
        converters.forEach(builder::addConverterFactory);
        return builder.build().create(FrankfurterRateClient.class);
    }

    @Bean
    public RatesapiRateClient retrofitRatesapiRateClient(
            final AppServicesProperties properties,
            final List<Converter.Factory> converters,
            final OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(properties.getRatesapi())
                .client(client);
        converters.forEach(builder::addConverterFactory);
        return builder.build().create(RatesapiRateClient.class);
    }

}

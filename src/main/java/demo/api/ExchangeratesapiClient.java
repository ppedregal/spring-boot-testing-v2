package demo.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import com.netflix.hystrix.HystrixCommand;

import demo.api.ExchangeratesapiClient.ExchangeratesapiClientFallback;

@FeignClient(name = "exchangerates",
    url = "https://api.exchangeratesapi.io",
    fallback = ExchangeratesapiClientFallback.class)
public interface ExchangeratesapiClient extends RateClient {

    @GetMapping("/latest")
    HystrixCommand<RateResponse> latest();

    @Component
    class ExchangeratesapiClientFallback extends Fallback
    implements ExchangeratesapiClient { }


}

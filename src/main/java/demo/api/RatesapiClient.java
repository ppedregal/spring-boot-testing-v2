package demo.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import com.netflix.hystrix.HystrixCommand;

import demo.api.RatesapiClient.RatesapiClientFallback;

@FeignClient(name = "ratesapi",
    url = "${app.services.ratesapi}",
    fallback = RatesapiClientFallback.class)
public interface RatesapiClient extends RateClient {

    @GetMapping("/latest")
    HystrixCommand<RateResponse> latest();

    @Component
    class RatesapiClientFallback extends Fallback
    implements RatesapiClient { }

}

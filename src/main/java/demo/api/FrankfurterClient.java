package demo.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import com.netflix.hystrix.HystrixCommand;

import demo.api.FrankfurterClient.FrankfurterClientFallback;

@FeignClient(name = "frankfurter",
    url = "${app.services.frankfurter}",
    fallback = FrankfurterClientFallback.class)
public interface FrankfurterClient extends RateClient {

    @GetMapping("/latest")
    HystrixCommand<RateResponse> latest();

    @Component
    class FrankfurterClientFallback extends Fallback
    implements FrankfurterClient { }

}

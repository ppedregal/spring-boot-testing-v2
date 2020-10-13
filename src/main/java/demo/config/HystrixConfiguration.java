package demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Configuration
@EnableCircuitBreaker
@EnableHystrixDashboard
public class HystrixConfiguration {

    @Controller
    static class HomeController {

        @Value("http://localhost:${server.port:8080}/actuator/hystrix.stream")
        private String stream;

        @RequestMapping("/")
        String home() {
            return "redirect:/hystrix/monitor?stream=" + stream;
        }
    }

}

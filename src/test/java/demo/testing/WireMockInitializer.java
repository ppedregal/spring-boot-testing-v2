package demo.testing;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        
        WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
        wireMockServer.start();
     
        configurableApplicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);
     
        configurableApplicationContext.addApplicationListener(applicationEvent -> {
          if (applicationEvent instanceof ContextClosedEvent) {
            wireMockServer.stop();
          }
        });
        
        String serverUrl = "http://localhost:"+wireMockServer.port()+"/wiremock";
        
        new TestPropertyValuesBuilder()
            .with("app.services.exchangeratesapi",serverUrl+"/exchangeratesapi/")
            .with("app.services.frankfurter",serverUrl+"/frankfurter/")
            .with("app.services.ratesapi",serverUrl+"/ratesapi/")
            .build()
            .applyTo(configurableApplicationContext);
        
    }
    
}

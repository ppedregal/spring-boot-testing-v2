package demo;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.github.tomakehurst.wiremock.WireMockServer;

import demo.service.RateService;
import demo.testing.WireMockInitializer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {
       WireMockInitializer.class
})
class SimpleTests {
    
    @Autowired ApplicationContext context;    
    @Autowired WireMockServer wiremock;
    @Autowired RateService rateService;
    @LocalServerPort int port;
    String baseUrl;
    RestTemplate restTemplate;
    
    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:"+port));
    }
    
    @AfterEach
    public void afterEach() {
      wiremock.resetMappings();      
    }    
    
    @Test
    void contestLoads() {
        assertThat(context).isNotNull();
    }
    
    @Test
    void given_when_allbackendservicesaredown_and_nofallback_then_failwithnotfound() {
        
        rateService.setLastRate("eur","usd",null);
                
        assertThatThrownBy(()->restTemplate.getForEntity("/convert/eur/usd/1", BigDecimal.class)).isInstanceOfSatisfying(HttpClientErrorException.class,(ex)->{
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        });
    }
    
    @Test
    void given_when_allbackendservicesaredown_and_fallback_then_successwithfallbackrate() {
        
        rateService.setLastRate("eur","usd",BigDecimal.ONE);
                
        assertThat(restTemplate.getForEntity("/convert/eur/usd/1", BigDecimal.class))
            .isNotNull()
            .matches(e->HttpStatus.OK==e.getStatusCode())
            .extracting(ResponseEntity::getBody)
            .isEqualTo(BigDecimal.ONE)
            ;
    }
        
    
    @Test
    void given_when_allareservicesareup_then_retrieverate() {
                
        wiremock.stubFor(get(urlPathMatching("/wiremock/exchangeratesapi/latest"))
                .atPriority(1)
                .willReturn(aResponse()   
                .withStatus(200)
                .withBody("{\"rates\":{\"CAD\":1.5453,\"HKD\":9.066,\"ISK\":163.2,\"PHP\":57.056,\"DKK\":7.4429,\"HUF\":364.58,\"CZK\":27.34,\"AUD\":1.6554,\"RON\":4.8765,\"SEK\":10.378,\"IDR\":17278.41,\"INR\":85.951,\"BRL\":6.5942,\"RUB\":91.4345,\"HRK\":7.5818,\"JPY\":123.15,\"THB\":36.574,\"CHF\":1.0697,\"SGD\":1.5935,\"PLN\":4.5518,\"BGN\":1.9558,\"TRY\":9.2868,\"CNY\":7.8756,\"NOK\":10.9678,\"NZD\":1.7762,\"ZAR\":19.5329,\"USD\":1.1698,\"MXN\":25.1559,\"ILS\":3.9734,\"GBP\":0.90535,\"KRW\":1344.08,\"MYR\":4.8599},\"base\":\"EUR\",\"date\":\"2020-10-15\"}")));
        
        wiremock.stubFor(get(urlPathMatching("/wiremock/frankfurter/latest"))
                .atPriority(1)
                .willReturn(aResponse()                
                .withStatus(200)
                .withBody("{\"rates\":{\"CAD\":1.5453,\"HKD\":9.066,\"ISK\":163.2,\"PHP\":57.056,\"DKK\":7.4429,\"HUF\":364.58,\"CZK\":27.34,\"AUD\":1.6554,\"RON\":4.8765,\"SEK\":10.378,\"IDR\":17278.41,\"INR\":85.951,\"BRL\":6.5942,\"RUB\":91.4345,\"HRK\":7.5818,\"JPY\":123.15,\"THB\":36.574,\"CHF\":1.0697,\"SGD\":1.5935,\"PLN\":4.5518,\"BGN\":1.9558,\"TRY\":9.2868,\"CNY\":7.8756,\"NOK\":10.9678,\"NZD\":1.7762,\"ZAR\":19.5329,\"USD\":1.1698,\"MXN\":25.1559,\"ILS\":3.9734,\"GBP\":0.90535,\"KRW\":1344.08,\"MYR\":4.8599},\"base\":\"EUR\",\"date\":\"2020-10-15\"}")));
        
        wiremock.stubFor(get(urlPathMatching("/wiremock/ratesapi/api/latest"))
                .atPriority(1)
                .willReturn(aResponse()                
                .withStatus(200)
                .withBody("{\"rates\":{\"CAD\":1.5453,\"HKD\":9.066,\"ISK\":163.2,\"PHP\":57.056,\"DKK\":7.4429,\"HUF\":364.58,\"CZK\":27.34,\"AUD\":1.6554,\"RON\":4.8765,\"SEK\":10.378,\"IDR\":17278.41,\"INR\":85.951,\"BRL\":6.5942,\"RUB\":91.4345,\"HRK\":7.5818,\"JPY\":123.15,\"THB\":36.574,\"CHF\":1.0697,\"SGD\":1.5935,\"PLN\":4.5518,\"BGN\":1.9558,\"TRY\":9.2868,\"CNY\":7.8756,\"NOK\":10.9678,\"NZD\":1.7762,\"ZAR\":19.5329,\"USD\":1.1698,\"MXN\":25.1559,\"ILS\":3.9734,\"GBP\":0.90535,\"KRW\":1344.08,\"MYR\":4.8599},\"base\":\"EUR\",\"date\":\"2020-10-15\"}")));
        
        assertThat(restTemplate.getForEntity("/convert/eur/usd/1", BigDecimal.class))
            .isNotNull()
            .matches(e->HttpStatus.OK==e.getStatusCode())
            .extracting(ResponseEntity::getBody)
            .isNotNull()
            ;
        
    }
    
}

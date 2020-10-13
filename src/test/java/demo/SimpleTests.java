package demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import demo.testing.WireMockInitializer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {
       WireMockInitializer.class
})
class SimpleTests {
    
    @Autowired ApplicationContext context;    
    @LocalServerPort int port;
    String baseUrl;
    RestTemplate restTemplate;
    
    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:"+port));
    }
    
    @Test
    void contestLoads() {
        assertThat(context).isNotNull();
    }
    
    @Test
    void testConvertNoyFound() {
        assertThatThrownBy(()->{
            restTemplate.getForObject("/convert/eur/usd/1", BigDecimal.class);
        }).isInstanceOfSatisfying(HttpClientErrorException.class,(ex)->{
            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        });
    }
    
}

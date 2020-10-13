package demo.testing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.util.TestPropertyValues;

public class TestPropertyValuesBuilder {
    
    List<String> pairs = new ArrayList<>();
    
    TestPropertyValuesBuilder with(String key,Object value) {
        pairs.add(key+"="+value);
        return this;
    }
    
    TestPropertyValues build() {
        return TestPropertyValues.of(pairs);
    }
    
}

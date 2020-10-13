# spring-boot-testing-v2

## Build
![Java CI](https://github.com/ppedregal/spring-boot-testing-v2/workflows/Java%20CI/badge.svg)

## Problem

```gherkin
Feature: EUR-USD Conversor using the best Rate 
Background: The provider has an agreement with few REST API
Scenario: Consume the APIs in a Happy path scenario
    Given an amount of Euros as input
    When call endpoint GET /convert/eur/usd/{amount}
    Then ask rate for: https://api.frankfurter.app/latest 
    And  ask rate for: https://api.ratesapi.io/api/latest 
    And  ask rate for: https://api.exchangeratesapi.io/latest 
    And  use the best rate for USD  
    Then return {amount}*rate
```

## Solution
RateProvider interface has exposes the logic for retrieving the best rate, two different 
implementations are available: 
* SequentialRateProvider: invokes all rate api services available one after the other
* ParallelRateProvider: invokes all rate api services available at once
Rest client with feign 
Timeout with feign
Retry with feign
Circuit breaker with hystrix

## Usage

Run with sequential strategy
```
./mvnw spring-boot:run -Dspring-boot.run.profiles=sequential
```
or just
```
./mvnw spring-boot:run
```

Run with parallel strategy
```
./mvnw spring-boot:run -Dspring-boot.run.profiles=parallel
```


Reports
```
./mvnw site
```

## References:

https://cucumber.io/docs/gherkin/reference/


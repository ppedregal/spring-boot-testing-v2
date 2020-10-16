package demo.api.retrofit;

import java.util.concurrent.CompletableFuture;

import demo.api.RateApi;
import retrofit2.http.GET;

public interface ExchangeratesapiRateClient extends RateApi {

    @GET("latest")
    CompletableFuture<RateResponse> latest();

}

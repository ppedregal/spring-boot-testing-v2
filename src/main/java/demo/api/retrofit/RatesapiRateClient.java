package demo.api.retrofit;

import java.util.concurrent.CompletableFuture;

import demo.api.RateApi;
import retrofit2.http.GET;

public interface RatesapiRateClient extends RateApi {

    @GET("api/latest")
    CompletableFuture<RateResponse> latest();

}

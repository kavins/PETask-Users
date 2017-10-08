package com.petask.petask_users.networking;

import com.petask.petask_users.models.response.UserList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Class which contains ans takes care of every thing related to network connection and api calling
 */
public class NetworkService {

    private final String BASE_URL =  "https://reqres.in/api/";

    /**
     * Interface in which all the APIs used in app are declared
     */
    public interface NetworkCalls {
        @GET("users")
        Call<UserList> getUsers(@Query("page") int pageNumber);
    }

    /**
     * Methods which returns a Retrofit instance. Base Url, Gson Converter factory to parse response and the API interface are all configured to the Retrofit
     * @return Retrofit instance
     */
    public NetworkCalls getNetworkCalls(){
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(NetworkCalls.class);
    }
}

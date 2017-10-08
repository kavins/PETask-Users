package com.petask.petask_users.networking;

import com.petask.petask_users.models.response.UserList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NetworkService {

    private final String BASE_URL =  "https://reqres.in/api/";

    public interface NetworkCalls {
        @GET("users")
        Call<UserList> getUsers(@Query("page") int pageNumber);
    }

    public NetworkCalls getNetworkCalls(){
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(NetworkCalls.class);
    }

}

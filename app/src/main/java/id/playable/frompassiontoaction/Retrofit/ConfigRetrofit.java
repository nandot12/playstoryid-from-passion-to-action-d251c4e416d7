package id.playable.frompassiontoaction.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import id.playable.frompassiontoaction.components.Constants;

/**
 * Created by Playable on 10/13/2017.
 */

public class ConfigRetrofit {



    public static Retrofit getRetrofit(){


        return new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getInstanceRetrofit(){

        return getRetrofit().create(ApiService.class);
    }
}

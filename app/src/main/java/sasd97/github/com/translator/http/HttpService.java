package sasd97.github.com.translator.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sasd97.github.com.translator.constants.YandexAPIConstants;

/**
 * Created by alexander on 07.04.17.
 */

public class HttpService {

    private static Retrofit retrofit;
    private static YandexAPI yandexAPI;

    private HttpService() {}

    public static void init() {

        retrofit = new Retrofit
                .Builder()
                .baseUrl(YandexAPIConstants.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        yandexAPI = retrofit.create(YandexAPI.class);
    }

    public static YandexAPI yandexAPI() {
        return yandexAPI;
    }
}

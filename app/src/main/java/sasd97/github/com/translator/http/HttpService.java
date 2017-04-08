package sasd97.github.com.translator.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sasd97.github.com.translator.constants.YandexAPIConstants;

/**
 * Created by alexander on 07.04.17.
 */

public class HttpService {

    private static Retrofit retrofit;

    private static YandexTranslatorAPI yandexTranslatorAPI;
    private static YandexDictionaryAPI yandexDictionaryAPI;

    private HttpService() {}

    public static void init() {

        retrofit = new Retrofit
                .Builder()
                .baseUrl(YandexAPIConstants.SERVER_URL_TRANSLATOR)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        yandexTranslatorAPI = retrofit.create(YandexTranslatorAPI.class);
        yandexDictionaryAPI = retrofit.create(YandexDictionaryAPI.class);
    }

    public static YandexTranslatorAPI translatorAPI() {
        return yandexTranslatorAPI;
    }

    public static YandexDictionaryAPI dictionaryAPI() {
        return yandexDictionaryAPI;
    }
}

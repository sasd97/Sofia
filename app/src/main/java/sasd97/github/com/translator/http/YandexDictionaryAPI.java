package sasd97.github.com.translator.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sasd97.github.com.translator.models.Dictionary.DictionaryModel;

/**
 * Created by alexander on 08.04.17.
 */

public interface YandexDictionaryAPI {

    @GET("lookup")
    Call<DictionaryModel> lookup(@Query("key") String apiKey,
                                 @Query("text") String text,
                                 @Query("lang") String lang);
}

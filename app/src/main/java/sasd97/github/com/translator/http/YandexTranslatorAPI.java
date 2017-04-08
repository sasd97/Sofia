package sasd97.github.com.translator.http;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sasd97.github.com.translator.models.LanguagesModel;
import sasd97.github.com.translator.models.YandexTranslationModel;

/**
 * Created by alexander on 07.04.17.
 */

public interface YandexTranslatorAPI {

    @GET("getLangs")
    Call<LanguagesModel> getLangs(@Query("key") String apiKey, @Query("ui") String ui);

    @POST("translate")
    @FormUrlEncoded
    Call<YandexTranslationModel> translate(@Query("key") String apiKey, @Field("text") String text, @Query("lang") String lang);
}

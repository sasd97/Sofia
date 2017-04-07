package sasd97.github.com.translator.http;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sasd97.github.com.translator.models.LanguagesModel;

/**
 * Created by alexander on 07.04.17.
 */

public interface YandexAPI {

    @POST("getLangs")
    Call<LanguagesModel> getLangs(@Query("key") String apiKey, @Query("ui") String ui);
}

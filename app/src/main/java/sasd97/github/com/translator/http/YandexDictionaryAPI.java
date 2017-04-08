package sasd97.github.com.translator.http;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by alexander on 08.04.17.
 */

public interface YandexDictionaryAPI {

    @GET("lookup")
    Call<?> lookup();
}

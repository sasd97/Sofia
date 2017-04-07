package sasd97.github.com.translator.http;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sasd97.github.com.translator.models.LanguagesModel;

import static sasd97.github.com.translator.http.HttpService.yandexAPI;
import static sasd97.github.com.translator.constants.YandexAPIConstants.API_KEY;

/**
 * Created by alexander on 07.04.17.
 */

public class YandexAPIWrapper {

    private YandexAPIWrapper() {}

    public static void getLangs(@NonNull final HttpResultListener<LanguagesModel> callback) {
        Call<LanguagesModel> getLangsQuery = yandexAPI().getLangs(API_KEY, Locale.getDefault().getCountry());

        getLangsQuery.enqueue(new Callback<LanguagesModel>() {
            @Override
            public void onResponse(Call<LanguagesModel> call, Response<LanguagesModel> response) {
                Log.d("HERE", call.request().url().toString());
                callback.onHttpSuccess(response.body());
            }

            @Override
            public void onFailure(Call<LanguagesModel> call, Throwable t) {
                //todo: replace to
                callback.onHttpCanceled();
            }
        });
    }
}

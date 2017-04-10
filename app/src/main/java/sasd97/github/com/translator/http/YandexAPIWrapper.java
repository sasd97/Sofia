package sasd97.github.com.translator.http;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sasd97.github.com.translator.models.LanguagesModel;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.models.YandexTranslationModel;

import static sasd97.github.com.translator.http.HttpService.translatorAPI;
import static sasd97.github.com.translator.constants.YandexAPIConstants.API_KEY_TRANSLATOR;

/**
 * Created by alexander on 07.04.17.
 */

public class YandexAPIWrapper {

    private YandexAPIWrapper() {}

    public static Call<?> getLangs(@NonNull final HttpResultListener callback) {
        Call<LanguagesModel> getLangsQuery = translatorAPI().getLangs(API_KEY_TRANSLATOR, Locale.getDefault().getCountry());

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

        return getLangsQuery;
    }

    public static Call<?> translate(@NonNull final String text,
                                 @NonNull String language,
                                 @NonNull final HttpResultListener callback) {
        Call<YandexTranslationModel> translateQuery = translatorAPI().translate(API_KEY_TRANSLATOR, text, language);

        translateQuery.enqueue(new Callback<YandexTranslationModel>() {
            @Override
            public void onResponse(Call<YandexTranslationModel> call, Response<YandexTranslationModel> response) {
                Log.d("HERE", call.request().url().toString());

                if (response.body() == null) {
                    callback.onHttpCanceled();
                    return;
                }

                TranslationModel translationModel = new TranslationModel();
                translationModel.setOriginalText(text);
                translationModel.setTranslatedText(response.body().getText().get(0));
                translationModel.setLanguage(response.body().getLanguage());
                translationModel.setFavorite(false);
                translationModel.defaultCreationDate();

                callback.onHttpSuccess(translationModel);
            }

            @Override
            public void onFailure(Call<YandexTranslationModel> call, Throwable t) {
                //todo: replace to
                callback.onHttpCanceled();
            }
        });

        return translateQuery;
    }
}

package sasd97.github.com.translator.http;

import android.support.annotation.NonNull;

import java.util.Locale;

import retrofit2.Call;
import sasd97.github.com.translator.models.Dictionary.DictionaryModel;
import sasd97.github.com.translator.models.LanguagesModel;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.models.YandexTranslationModel;

import static sasd97.github.com.translator.constants.YandexAPIConstants.API_KEY_DICTIONARY;
import static sasd97.github.com.translator.http.HttpService.dictionaryAPI;
import static sasd97.github.com.translator.http.HttpService.translatorAPI;
import static sasd97.github.com.translator.constants.YandexAPIConstants.API_KEY_TRANSLATOR;

/**
 * Created by alexander on 07.04.17.
 */

public class YandexAPIWrapper {

    private static final String TAG = YandexAPIWrapper.class.getCanonicalName();

    private YandexAPIWrapper() {}

    public static Call<?> getLangs(@NonNull final HttpResultListener callback) {
        Call<LanguagesModel> getLangsQuery = translatorAPI().getLangs(API_KEY_TRANSLATOR, Locale.getDefault().getCountry());
        getLangsQuery.enqueue(new ResultWrapper<LanguagesModel>(callback));
        return getLangsQuery;
    }

    public static Call<?> translate(@NonNull final String text,
                                    @NonNull String language,
                                    @NonNull final HttpResultListener callback) {
        Call<YandexTranslationModel> translateQuery = translatorAPI().translate(API_KEY_TRANSLATOR, text, language);

        translateQuery.enqueue(new ResultWrapper<YandexTranslationModel>(callback) {
            @Override
            protected boolean isExecutionBroken() {
                return true;
            }

            @Override
            protected void onBrokenExecution(YandexTranslationModel result) {
                super.onBrokenExecution(result);

                TranslationModel translationModel = new TranslationModel();
                translationModel.setOriginalText(text);
                translationModel.setTranslatedText(result.getText().get(0));
                translationModel.setLanguage(result.getLanguage());
                translationModel.setFavorite(false);
                translationModel.defaultCreationDate();

                callback.onHttpSuccess(translationModel);
            }
        });

        return translateQuery;
    }

    public static Call<?> lookup(@NonNull String text,
                                 @NonNull String language,
                                 @NonNull final HttpResultListener callback) {
        Call<DictionaryModel> lookupRequest = dictionaryAPI().lookup(API_KEY_DICTIONARY, text, language);
        lookupRequest.enqueue(new ResultWrapper<DictionaryModel>(callback));
        return lookupRequest;
    }
}

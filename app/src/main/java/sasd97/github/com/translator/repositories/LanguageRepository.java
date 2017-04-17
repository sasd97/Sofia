package sasd97.github.com.translator.repositories;

import android.support.annotation.NonNull;

import sasd97.github.com.translator.models.SupportedLanguageModel;
import sasd97.github.com.translator.models.TranslationModel;
import sasd97.github.com.translator.utils.ArrayUtils;
import sasd97.github.com.translator.utils.Prefs;

import static sasd97.github.com.translator.constants.PrefsConstants.DESTINATION_LANGUAGE_PREFERENCES;
import static sasd97.github.com.translator.constants.PrefsConstants.TARGET_LANGUAGE_PREFERENCES;

/**
 * Created by alexander on 17/04/2017.
 */

public class LanguageRepository {

    private static final String TAG = LanguageRepository.class.getCanonicalName();
    private static final String LANGUAGE_SEPARATOR = "-";

    private SupportedLanguageModel targetLanguage;
    private SupportedLanguageModel destinationLanguage;

    public LanguageRepository() {

    }

    public static LanguageRepository fromTranslation(TranslationModel translation) {
        LanguageRepository repository = new LanguageRepository();
        if (translation == null) return repository;

        String[] languages = translation.getLanguage().split(LANGUAGE_SEPARATOR);
        if (languages.length != 2) return repository;

        repository.set(
                SupportedLanguageModel.fromString(languages[0]),
                SupportedLanguageModel.fromString(languages[1])
        );

        return repository;
    }

    public SupportedLanguageModel getTargetLanguage() {
        return targetLanguage;
    }

    public LanguageRepository setTargetLanguage(SupportedLanguageModel targetLanguage) {
        this.targetLanguage = targetLanguage;
        return this;
    }

    public LanguageRepository setTargetFromLongName(String longName) {
        targetLanguage = SupportedLanguageModel.fromLongString(longName);
        return this;
    }

    public int obtainTargetIndex(@NonNull String[] array) {
        return ArrayUtils.indexOfCaseInsensitive(array, targetLanguage.name());
    }

    public SupportedLanguageModel getDestinationLanguage() {
        return destinationLanguage;
    }

    public LanguageRepository setDestinationLanguage(SupportedLanguageModel destinationLanguage) {
        this.destinationLanguage = destinationLanguage;
        return this;
    }

    public LanguageRepository setDestinationFromLongName(String longName) {
        destinationLanguage = SupportedLanguageModel.fromLongString(longName);
        return this;
    }

    public int obtainDestinationIndex(@NonNull String[] array) {
        return ArrayUtils.indexOfCaseInsensitive(array, destinationLanguage.name());
    }

    public void set(SupportedLanguageModel targetLanguage,
                    SupportedLanguageModel destinationLanguage) {
        this.targetLanguage = targetLanguage;
        this.destinationLanguage = destinationLanguage;
    }

    public void saveTargetLanguage() {
        Prefs.put(TARGET_LANGUAGE_PREFERENCES, targetLanguage.toString());
    }

    public void saveDestinationLanguage() {
        Prefs.put(DESTINATION_LANGUAGE_PREFERENCES, destinationLanguage.toString());
    }

    public boolean restoreTargetLanguage() {
        if (!Prefs.get().contains(TARGET_LANGUAGE_PREFERENCES)) return false;

        targetLanguage = SupportedLanguageModel.fromString(Prefs.get().getString(TARGET_LANGUAGE_PREFERENCES, null));
        return true;
    }

    public boolean restoreDestinationLanguage() {
        if (!Prefs.get().contains(DESTINATION_LANGUAGE_PREFERENCES)) return false;

        destinationLanguage = SupportedLanguageModel.fromString(Prefs.get().getString(DESTINATION_LANGUAGE_PREFERENCES, null));
        return true;
    }

    public boolean swapLanguages() {
        if (targetLanguage == SupportedLanguageModel.AUTOMATIC) return false;

        SupportedLanguageModel t = targetLanguage;
        targetLanguage = destinationLanguage;
        destinationLanguage = t;
        return true;
    }

    public String obtainCortege() {
        return targetLanguage.getCortege(destinationLanguage);
    }

    @Override
    public String toString() {
        return "LanguageRepository{" +
                "targetLanguage=" + targetLanguage.name() +
                ", destinationLanguage=" + destinationLanguage.name() +
                '}';
    }
}

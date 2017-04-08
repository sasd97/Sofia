package sasd97.github.com.translator.models;

import android.util.Log;

/**
 * Created by alexander on 08.04.17.
 */

public enum SupportedLanguageModel {

    AUTOMATIC("auto"),
    ALBANIAN("sq"),
    ARMENIAN("hy"),
    AZERBAIJANI("az"),
    BELORUSSIAN("be"),
    BULGARIAN("bg"),
    CATALAN("ca"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESTONIAN("et"),
    FINNISH("fi"),
    FRENCH("fr"),
    GERMAN("de"),
    GEORGIAN("ka"),
    GREEK("el"),
    HUNGARIAN("hu"),
    ITALIAN("it"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    MACEDONIAN("mk"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SERBIAN("sr"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SPANISH("es"),
    SWEDISH("sv"),
    TURKISH("tr"),
    UKRAINIAN("uk");

    private final String language;

    private SupportedLanguageModel(final String pLanguage) {
        language = pLanguage;
    }

    public static SupportedLanguageModel fromString(final String pLanguage) {
        for (SupportedLanguageModel l : values()) {
            if (l.toString().equalsIgnoreCase(pLanguage)) {
                return l;
            }
        }
        return null;
    }

    public static SupportedLanguageModel fromLongString(final String pLanguage) {
        for (SupportedLanguageModel l : values()) {
            if (l.name().equalsIgnoreCase(pLanguage)) {
                return l;
            }
        }
        return null;
    }

    public String getCortege(SupportedLanguageModel another) {
        if (this == AUTOMATIC) return another.toString();
        return String.format("%1$s-%2$s", language, another.toString());
    }

    @Override
    public String toString() {
        return language;
    }
}

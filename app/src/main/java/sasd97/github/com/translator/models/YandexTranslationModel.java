package sasd97.github.com.translator.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 08.04.17.
 */

public class YandexTranslationModel {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("lang")
    @Expose
    private String language;

    @SerializedName("text")
    @Expose
    private List<String> text;

    public String getCode() {
        return code;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getText() {
        return text;
    }

    @Override
    public String toString() {
        return "YandexTranslationModel{" +
                "code='" + code + '\'' +
                ", language='" + language + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

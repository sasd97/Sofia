package sasd97.github.com.translator.models.Dictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class ExampleDictionaryModel {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("tr")
    @Expose
    private List<TextDictionaryModel> translations;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TextDictionaryModel> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TextDictionaryModel> translations) {
        this.translations = translations;
    }

    @Override
    public String toString() {
        return "ExampleDictionaryModel{" +
                "text='" + text + '\'' +
                ", translations=" + translations +
                '}';
    }
}

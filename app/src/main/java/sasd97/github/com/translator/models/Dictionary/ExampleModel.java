package sasd97.github.com.translator.models.Dictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class ExampleModel {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("tr")
    @Expose
    private List<TextModel> translations;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TextModel> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TextModel> translations) {
        this.translations = translations;
    }

    @Override
    public String toString() {
        return "ExampleModel{" +
                "text='" + text + '\'' +
                ", translations=" + translations +
                '}';
    }
}

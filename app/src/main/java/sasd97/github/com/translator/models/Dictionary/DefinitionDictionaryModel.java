package sasd97.github.com.translator.models.Dictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class DefinitionDictionaryModel {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("pos")
    @Expose
    private String partOfSpeech;

    @SerializedName("tr")
    @Expose
    private List<TranslationDictionaryModel> translation;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public List<TranslationDictionaryModel> getTranslation() {
        return translation;
    }

    public void setTranslation(List<TranslationDictionaryModel> translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return "DefinitionDictionaryModel{" +
                "text='" + text + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                ", translation=" + translation +
                '}';
    }
}

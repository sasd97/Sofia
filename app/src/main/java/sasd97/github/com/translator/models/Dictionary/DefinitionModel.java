package sasd97.github.com.translator.models.Dictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class DefinitionModel {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("pos")
    @Expose
    private String partOfSpeech;

    @SerializedName("tr")
    @Expose
    private List<TranslationModel> translation;

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

    public List<TranslationModel> getTranslation() {
        return translation;
    }

    public void setTranslation(List<TranslationModel> translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return "DefinitionModel{" +
                "text='" + text + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                ", translation=" + translation +
                '}';
    }
}

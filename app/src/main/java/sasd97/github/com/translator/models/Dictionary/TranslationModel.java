package sasd97.github.com.translator.models.Dictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class TranslationModel {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("pos")
    @Expose
    private String partOfSpeech;

    @SerializedName("syn")
    @Expose
    private List<TextModel> synonyms;

    @SerializedName("mean")
    @Expose
    private List<TextModel> mean;

    @SerializedName("ex")
    @Expose
    private List<ExampleModel> examples;

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

    public List<TextModel> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<TextModel> synonyms) {
        this.synonyms = synonyms;
    }

    public List<TextModel> getMean() {
        return mean;
    }

    public void setMean(List<TextModel> mean) {
        this.mean = mean;
    }

    public List<ExampleModel> getExamples() {
        return examples;
    }

    public void setExamples(List<ExampleModel> examples) {
        this.examples = examples;
    }

    @Override
    public String toString() {
        return "TranslationModel{" +
                "text='" + text + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                ", synonyms=" + synonyms +
                ", mean=" + mean +
                ", examples=" + examples +
                '}';
    }
}

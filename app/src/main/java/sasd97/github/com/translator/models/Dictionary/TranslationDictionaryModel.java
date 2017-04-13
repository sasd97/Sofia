package sasd97.github.com.translator.models.Dictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class TranslationDictionaryModel {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("pos")
    @Expose
    private String partOfSpeech;

    @SerializedName("syn")
    @Expose
    private List<TextDictionaryModel> synonyms;

    @SerializedName("mean")
    @Expose
    private List<TextDictionaryModel> mean;

    @SerializedName("ex")
    @Expose
    private List<ExampleDictionaryModel> examples;

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

    public List<TextDictionaryModel> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<TextDictionaryModel> synonyms) {
        this.synonyms = synonyms;
    }

    public List<TextDictionaryModel> getMean() {
        return mean;
    }

    public void setMean(List<TextDictionaryModel> mean) {
        this.mean = mean;
    }

    public List<ExampleDictionaryModel> getExamples() {
        return examples;
    }

    public void setExamples(List<ExampleDictionaryModel> examples) {
        this.examples = examples;
    }

    @Override
    public String toString() {
        return "TranslationDictionaryModel{" +
                "text='" + text + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                ", synonyms=" + synonyms +
                ", mean=" + mean +
                ", examples=" + examples +
                '}';
    }
}

package sasd97.github.com.translator.models.Dictionary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class TranslationDictionaryModel implements Parcelable {

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

    public TranslationDictionaryModel() {

    }

    protected TranslationDictionaryModel(Parcel in) {
        text = in.readString();
        partOfSpeech = in.readString();
        synonyms = in.createTypedArrayList(TextDictionaryModel.CREATOR);
        mean = in.createTypedArrayList(TextDictionaryModel.CREATOR);
        examples = in.createTypedArrayList(ExampleDictionaryModel.CREATOR);
    }

    public static final Creator<TranslationDictionaryModel> CREATOR = new Creator<TranslationDictionaryModel>() {
        @Override
        public TranslationDictionaryModel createFromParcel(Parcel in) {
            return new TranslationDictionaryModel(in);
        }

        @Override
        public TranslationDictionaryModel[] newArray(int size) {
            return new TranslationDictionaryModel[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(partOfSpeech);
        dest.writeTypedList(synonyms);
        dest.writeTypedList(mean);
        dest.writeTypedList(examples);
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

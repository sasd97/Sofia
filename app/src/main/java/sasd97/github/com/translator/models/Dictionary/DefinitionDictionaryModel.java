package sasd97.github.com.translator.models.Dictionary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class DefinitionDictionaryModel implements Parcelable {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("pos")
    @Expose
    private String partOfSpeech;

    @SerializedName("tr")
    @Expose
    private List<TranslationDictionaryModel> translation;

    public DefinitionDictionaryModel() {
    }

    protected DefinitionDictionaryModel(Parcel in) {
        text = in.readString();
        partOfSpeech = in.readString();
        translation = in.createTypedArrayList(TranslationDictionaryModel.CREATOR);
    }

    public static final Creator<DefinitionDictionaryModel> CREATOR = new Creator<DefinitionDictionaryModel>() {
        @Override
        public DefinitionDictionaryModel createFromParcel(Parcel in) {
            return new DefinitionDictionaryModel(in);
        }

        @Override
        public DefinitionDictionaryModel[] newArray(int size) {
            return new DefinitionDictionaryModel[size];
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

    public List<TranslationDictionaryModel> getTranslation() {
        return translation;
    }

    public void setTranslation(List<TranslationDictionaryModel> translation) {
        this.translation = translation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(partOfSpeech);
        dest.writeTypedList(translation);
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

package sasd97.github.com.translator.models.Dictionary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexander on 13/04/2017.
 */

public class ExampleDictionaryModel implements Parcelable {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("tr")
    @Expose
    private List<TextDictionaryModel> translations;

    public ExampleDictionaryModel() {

    }

    protected ExampleDictionaryModel(Parcel in) {
        text = in.readString();
        translations = in.createTypedArrayList(TextDictionaryModel.CREATOR);
    }

    public static final Creator<ExampleDictionaryModel> CREATOR = new Creator<ExampleDictionaryModel>() {
        @Override
        public ExampleDictionaryModel createFromParcel(Parcel in) {
            return new ExampleDictionaryModel(in);
        }

        @Override
        public ExampleDictionaryModel[] newArray(int size) {
            return new ExampleDictionaryModel[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeTypedList(translations);
    }

    @Override
    public String toString() {
        return "ExampleDictionaryModel{" +
                "text='" + text + '\'' +
                ", translations=" + translations +
                '}';
    }
}
